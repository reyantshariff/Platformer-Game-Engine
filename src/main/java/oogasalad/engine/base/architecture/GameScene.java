package oogasalad.engine.base.architecture;

import java.util.*;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.enumerate.KeyCode;
import oogasalad.engine.base.event.GameAction;
import oogasalad.engine.base.event.InputMapping;

/**
 * The GameScene class is the base class for all game scenes. It manages the game objects and
 * components within the scene. It is responsible for updating the game objects and components every
 * frame.
 *
 * @author Hsuan-Kai Liao, Christian Bepler
 */
public abstract class GameScene {
  private final UUID id;
  private final InputMapping inputMapping;
  private final Map<UUID, GameObject> allObjects;
  private final Map<ComponentTag, List<GameComponent>> allComponents;
  private final Queue<KeyCode> inputKeys;
  private final Queue<Runnable> subscribedEvents;

  private String name;

  /**
   * Constructor for basic game scene, assigning a unique ID and initializing all lists for objects, components, input keys,
   * and subscribed events
   *
   * @param name - Name of game scene
   */
  public GameScene(String name) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.inputMapping = new InputMapping();
    this.allObjects = new HashMap<>();
    this.allComponents = new EnumMap<>(ComponentTag.class);
    for (ComponentTag tag : ComponentTag.values()) {
      allComponents.put(tag, new ArrayList<>());
    }
    this.inputKeys = new LinkedList<>();
    this.subscribedEvents = new LinkedList<>();
  }

  /**
   * Get the name of the scene
   */
  public final String getName() {
    return name;
  }

  /**
   * Set the name of the scene
   */
  public final void setName(String name) {
    this.name = name;
  }

  /**
   * Get the UUID of the scene
   */
  public final UUID getId() {
    return id;
  }

  /**
   * Get the input mapping of the scene
   */
  public final InputMapping getInputMapping() {
    return inputMapping;
  }

  /**
   * This will be called every frame.
   * @param deltaTime the elapsed time between two frames
   */
  final void step(double deltaTime) {
    // Update with the following sequence
    // 1. Handle all the subscribed events
    while (!subscribedEvents.isEmpty()) {
      subscribedEvents.poll().run();
    }

    // 2. Handle input events
    while (!inputKeys.isEmpty()) {
      inputMapping.trigger(inputKeys.poll());
    }

    // 3. Update the components based on the order
    for (ComponentTag order : ComponentTag.values()) {
      if (order == ComponentTag.NONE) continue;
      for (GameComponent component : allComponents.get(order)) {
        component.update(deltaTime);
      }
    }

    // 4. Update the scene actions
    // TODO: Handle Change Scene Action Here
  }

  public List<GameObject> getAllObjects() {
    return new ArrayList<>(allObjects.values());
  }

  /**
   * Subscribe the input key for the next frame to execute.
   * Inputs will be handled once and then removed. So make sure to add key events every frame until released.
   * @param key the key to be subscribed
   */
  public final void subscribeInputKey(KeyCode key) {
    inputKeys.add(key);
  }

  /**
   * Subscribe the runnable event to the next frame to execute.
   * Events will only be called once and then removed from the subscribed list.
   * @param event the event to be subscribed
   */
  public final void subscribeEvent(Runnable event) {
    subscribedEvents.add(event);
  }

  /**
   * Register the component from the gameObject onto the scene
   * 
   * @param gameComponent the component to be registered
   */
  public final void registerComponent(GameComponent gameComponent) {
    allComponents.get(gameComponent.componentTag()).add(gameComponent);
  }

  /**
   * Unregister the component from the scene.
   * 
   * @param gameComponent the gameComponent to be unregistered
   */
  public final void unregisterComponent(GameComponent gameComponent) {
    allComponents.get(gameComponent.componentTag()).remove(gameComponent);
  }

  /**
   * Instantiate the gameObject based on the specified gameObject subclass.
   * 
   * @param gameObjectClass the gameObject class
   * @return the instantiated gameObject
   */
  public final <T extends GameObject> T instantiateObject(Class<T> gameObjectClass) {
    try {
      String className = gameObjectClass.getSimpleName();
      T object = gameObjectClass.getDeclaredConstructor(String.class).newInstance((Object) null);
      object.wakeUp();
      object.setScene(this);
      allObjects.put(object.getId(), object);
      return object;
    } catch (Exception e) {
      throw new RuntimeException("Failed to add component", e);
    }

  }

  /**
   * Register the existing gameObject to the scene.
   * 
   * @param gameObject the gameObject to be registered.
   */
  public final void registerObject(GameObject gameObject) {
    if (allObjects.containsKey(gameObject.getId())) {
      throw new IllegalArgumentException("gameObject already added!");
    }

    gameObject.wakeUp();
    gameObject.setScene(this);
    allObjects.put(gameObject.getId(), gameObject);
  }

  /**
   * unregister the gameObject specified.
   * 
   * @param gameObject the gameObject to be destroyed
   */
  public final void unregisterObject(GameObject gameObject) {
    unregisterObjectComponents(gameObject);
    unregisterInputBindings(gameObject);
    gameObject.setScene(null);
    allObjects.remove(gameObject.getId());
  }

  private void unregisterObjectComponents(GameObject gameObject) {
    for (ComponentTag order : ComponentTag.values()) {
      List<GameComponent> components = allComponents.get(order);
      components.removeIf(component -> {
        if (component.getParent().equals(gameObject)) {
          unregisterComponent(component);
          return true; // Remove it from the list if needed
        }
        return false;
      });
    }
  }

  private void unregisterInputBindings(GameObject gameObject) {
    Map<KeyCode, List<GameAction>> keyActionMap = inputMapping.getMapping();
    List<GameAction> actionsToBeRemoved = new ArrayList<>();
    for (List<GameAction> actions : keyActionMap.values()) {
      for (GameAction action : actions) {
        if (action.getParent().equals(gameObject)) {
          actionsToBeRemoved.add(action);
        }
      }
    }
    actionsToBeRemoved.forEach(inputMapping::removeMapping);
  }


  /**
   * Event that will be called when the gameScene is set to active.
   */
  public void onActivated() {};

  /**
   * Event that will be called when the gameScene is set to inactive.
   */
  public void onDeactivated() {};

}
