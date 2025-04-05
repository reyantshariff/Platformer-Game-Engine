package oogasalad.engine.base.architecture;

import java.util.*;
import static oogasalad.config.GameConfig.LOGGER;

/**
 * The GameObject class is the base class for all game objects. It is used to define the behavior of
 * game objects. Each game object can have multiple components, and each component can have its own
 * logic and behavior.
 *
 * @author Hsuan-Kai Liao, Christian Bepler
 */

public class GameObject {
  private final UUID id;
  private final Map<Class<? extends GameComponent>, GameComponent> allComponents;
  private final List<Runnable> componentAwakeInitializer;
  private final List<Runnable> componentStartInitializer;

  private GameScene parentScene;
  private String name;
  private String tag;

  public GameObject(String name, String tag) {
    this.id = UUID.randomUUID();
    this.name = name == null ? "" : this.getClass().getSimpleName() + "_" + this.id;
    this.tag = tag;
    this.allComponents = new HashMap<>();
    this.componentAwakeInitializer = new ArrayList<>();
    this.componentStartInitializer = new ArrayList<>();
  }

  final void wakeUp() {
    componentAwakeInitializer.forEach(Runnable::run);
    componentAwakeInitializer.clear();
  }

  final void startUp() {
    componentStartInitializer.forEach(Runnable::run);
    componentStartInitializer.clear();
  }

  /**
   * Add the component to the gameObject based on its class.
   * 
   * @apiNote Every component class should only have one instance per object.
   * @param componentClass the component class specified
   * @return the added component instance
   */
  public final <T extends GameComponent> T addComponent(Class<T> componentClass) {
    if (allComponents.containsKey(componentClass)) {
      throw new IllegalArgumentException("Component already exists");
    }
    try {
      T component = componentClass.getDeclaredConstructor().newInstance();
      component.setParent(this);

      // Awake method subscription
      if (parentScene == null) {
        componentAwakeInitializer.add(component::awake);
      } else {
        component.awake();
      }

      // Start method subscription
      if (parentScene == null) {
        componentStartInitializer.add(component::start);
      } else {
        parentScene.subscribeEvent(component::start);
      }

      allComponents.put(componentClass, component);
      parentScene.registerComponent(component); // May need a null checker. Run GameObjectParserTest to see more info.
      return component;
    } catch (Exception e) {
      LOGGER.error("Could not add component {}", componentClass.getName());
      throw new RuntimeException("Failed to add component", e);
    }
  }


  /**
   * Get the component based on
   * 
   * @param componentClass the component class specified
   * @return the component instance
   */
  public final <T extends GameComponent> T getComponent(Class<T> componentClass) {
    if (!allComponents.containsKey(componentClass)) {
      throw new IllegalArgumentException("Component does not exist");
    }
    return (T) allComponents.get(componentClass);
  }

  /**
   * Remove the component based on its class.
   * 
   * @param componentClass the component class specified
   */
  public final <T extends GameComponent> void removeComponent(Class<T> componentClass) {
    if (!allComponents.containsKey(componentClass)) {
      throw new IllegalArgumentException("Component does not exist");
    }

    GameComponent componentToRemove = allComponents.get(componentClass);
    componentToRemove.onRemove();
    parentScene.unregisterComponent(componentToRemove);
    allComponents.remove(componentClass);
  }

  /**
   * Returns the name of the object.
   * 
   * @return the name of the object
   */
  public final String getName() {
    return name;
  }

  /**
   * Returns the ID of the object.
   * 
   * @return the ID of the object
   */
  public final UUID getId() {
    return id;
  }

  /**
   * Returns the parent scene of the object.
   * 
   * @return the parent scene of the object
   */
  public final GameScene getScene() {
    return parentScene;
  }

  final void setScene(GameScene parentScene) {
    this.parentScene = parentScene;
  }

  /**
   * Sets the name of the object.
   * 
   * @param name the name of the object
   */
  public final void setName(String name) {
    this.name = name;
  }

  /**
   * Returns all the components
   *
   * @return - a Map of some extended gameComponent to the GameComponent, representing all components
   */
  public final Map<Class<? extends GameComponent>, GameComponent> getAllComponents() {
    return allComponents;
  }

  /**
   * Setter for the tag of the game object
   *
   * @param tag - the new classification for the GameObject that is set
   */
  public void setTag(String tag) {
    this.tag = tag;
  }

  /**
   * Getter for the tag of the game object
   *
   * @return - the classification for the GameObject
   */
  public String getTag() {
    return tag;
  }

}
