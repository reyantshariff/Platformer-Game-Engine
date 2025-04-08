package oogasalad.engine.base.architecture;

import static oogasalad.config.GameConfig.LOGGER;

import java.util.*;
import oogasalad.engine.base.enumerate.ComponentTag;

/**
 * The GameScene class is the base class for all game scenes. It manages the game objects and
 * components within the scene. It is responsible for updating the game objects and components every
 * frame.
 *
 * @author Hsuan-Kai Liao, Christian Bepler
 */
public class GameScene {
  private final UUID id;
  private final Map<UUID, GameObject> allObjects;
  private final Map<ComponentTag, List<GameComponent>> allComponents;
  private final Queue<Runnable> subscribedEvents;

  private String name;
  private Game game;

  public GameScene(String name) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.allObjects = new HashMap<>();
    this.allComponents = new EnumMap<>(ComponentTag.class);
    for (ComponentTag tag : ComponentTag.values()) {
      allComponents.put(tag, new ArrayList<>());
    }
    this.subscribedEvents = new LinkedList<>();
  }

  final void setGame(Game game) {
    this.game = game;
  }

  /**
   * Get the game that this scene belongs to.
   */
  public Game getGame() {
    return game;
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
   * Get the Object based on the UUID
   * @param id the UUID of the object
   */
  public final GameObject getObject(UUID id) {
    return allObjects.get(id);
  }

  /**
   * Get the Object based on the name
   * @param name the name of the object
   */
  public final GameObject getObject(String name) {
    for (GameObject object : allObjects.values()) {
      if (object.getName().equals(name)) {
        return object;
      }
    }
    LOGGER.error("Could not find object with name {}", name);
    throw new IllegalArgumentException("No such object with name " + name);
  }

  /**
   * Getter to return an unmodifiable collection of all the GameObjects
   */
  public final Collection<GameObject> getAllObjects() {
    return Collections.unmodifiableCollection(allObjects.values());
  }

  /**
   * Getter to return an unmodifiable map of all the GameComponents
   */
  public final Map<ComponentTag, List<GameComponent>> getAllComponents() {
    return Collections.unmodifiableMap(allComponents);
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
  final void registerComponent(GameComponent gameComponent) {
    allComponents.get(gameComponent.componentTag()).add(gameComponent);
  }

  /**
   * Unregister the component from the scene.
   * 
   * @param gameComponent the gameComponent to be unregistered
   */
  final void unregisterComponent(GameComponent gameComponent) {
    allComponents.get(gameComponent.componentTag()).remove(gameComponent);
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

    // Register components
    for (GameComponent component : gameObject.getAllComponents().values()) {
      registerComponent(component);
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
    // Unregister components
    for (ComponentTag order : ComponentTag.values()) {
      for (GameComponent component : allComponents.get(order)) {
        if (component.getParent().equals(gameObject)) {
          unregisterComponent(component);
        }
      }
    }

    gameObject.setScene(null);
    allObjects.remove(gameObject.getId());
  }

  /**
   * Change the scene to the specified scene name.
   * @param sceneName the name of the scene to be changed to
   */
  final void changeScene(String sceneName) {
    game.changeScene(sceneName);
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
