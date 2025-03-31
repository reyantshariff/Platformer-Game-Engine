package oogasalad.engine.base;

import java.beans.EventHandler;
import java.util.*;

/**
 * The GameScene class is the base class for all game scenes. It manages the game objects and
 * components within the scene. It is responsible for updating the game objects and components every
 * frame.
 */

public abstract class GameScene {
  private final UUID id;
  private final InputMapping inputMapping;
  private final Map<UUID, GameObject> allObjects;
  private final Map<ComponentTag, List<GameComponent>> allComponents;
  private final Queue<KeyCode> inputKeys;

  private String name;

  public GameScene(String name, UUID id) {
    this.id = id;
    this.name = name;
    this.inputMapping = new InputMapping();
    this.allObjects = new HashMap<>();
    this.allComponents = new EnumMap<>(ComponentTag.class);
    for (ComponentTag tag : ComponentTag.values()) {
      allComponents.put(tag, new ArrayList<>());
    }
    this.inputKeys = new LinkedList<>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UUID getId() {
    return id;
  }

  public InputMapping getInputMapping() {
    return inputMapping;
  }

  /**
   * This will be called every frame.
   * 
   * @param deltaTime the elapsed time between two frames
   */
  public void step(double deltaTime) {
    // Update with the following sequence
    // 1. Handle input events
    while (!inputKeys.isEmpty()) {
      KeyCode code = inputKeys.poll();
      inputMapping.trigger(code);
    }

    // 2. Update the components based on the order
    for (ComponentTag order : ComponentTag.values()) {
      if (order == ComponentTag.NONE) continue;
      for (GameComponent component : allComponents.get(order)) {
        component.update(deltaTime);
      }
    }

    // 3. Update the scene actions
    // TODO: Handle Change Scene Action Here
  }

  /**
   * Subscribe the input key for the next frame to execute.
   * Inputs will be handled once and then removed. So make sure to add key events every frame until released.
   */
  public void subscribeInputKey(KeyCode key) {
    inputKeys.add(key);
  }


  /**
   * Register the component from the gameObject onto the scene
   * 
   * @param gameComponent the component to be registered
   */
  public void registerComponent(GameComponent gameComponent) {
    allComponents.get(gameComponent.getComponentTag()).add(gameComponent);
  }

  /**
   * Unregister the component from the scene.
   * 
   * @param gameComponent the gameComponent to be unregistered
   */
  public void unregisterComponent(GameComponent gameComponent) {
    allComponents.get(gameComponent.getComponentTag()).remove(gameComponent);
  }

  /**
   * Instantiate the gameObject based on the specified gameObject subclass.
   * 
   * @param gameObjectClass the gameObject class
   * @return the instantiated gameObject
   */
  public <T extends GameObject> T instantiateObject(Class<T> gameObjectClass) {
    try {
      UUID id = UUID.randomUUID();
      String className = gameObjectClass.getSimpleName();
      String defaultName = className + "_" + id;
      T object = gameObjectClass.getDeclaredConstructor(String.class, UUID.class, GameScene.class).newInstance(defaultName, id, this);
      object.wakeUp();
      allObjects.put(id, object);
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
  public void registerObject(GameObject gameObject) {
    if (allObjects.containsKey(gameObject.getId())) {
      throw new IllegalArgumentException("gameObject already added!");
    }

    gameObject.wakeUp();
    allObjects.put(gameObject.getId(), gameObject);
  }

  /**
   * unregister the gameObject specified.
   * 
   * @param gameObject the gameObject to be destroyed
   */
  public void unregisterObject(GameObject gameObject) {
    for (ComponentTag order : ComponentTag.values()) {
      for (GameComponent component : allComponents.get(order)) {
        if (component.getParent().equals(gameObject)) {
          unregisterComponent(component);
        }
      }
    }

    allObjects.remove(gameObject.getId());
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
