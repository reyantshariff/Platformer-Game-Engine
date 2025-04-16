package oogasalad.model.engine.base.architecture;

import java.util.*;
import oogasalad.model.engine.component.Transform;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import java.lang.reflect.InvocationTargetException;

import static oogasalad.model.config.GameConfig.LOGGER;

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

  private GameScene parentScene;
  private String name;
  private String tag;

  /**
   * Constructor for GameObject
   *
   * @param tag the tag of the object
   */
  public GameObject(String tag) {
    this.id = UUID.randomUUID();
    this.name = this.getClass().getSimpleName() + "_" + this.id;
    this.tag = tag;
    this.allComponents = new HashMap<>();
    this.componentAwakeInitializer = new ArrayList<>();
  }

  /**
   * Constructor for GameObject
   *
   * @param name the name of the object
   * @param tag  the tag of the object
   */
  public GameObject(String name, String tag) {
    this(tag);
    this.name = name;
  }

  final void wakeUp() {
    componentAwakeInitializer.forEach(Runnable::run);
    componentAwakeInitializer.clear();
  }

  /**
   * Add the component to the gameObject based on its class.
   *
   * @param componentClass the component class specified
   * @return the added component instance
   * @apiNote Every component class should only have one instance per object.
   */
  public final <T extends GameComponent> T addComponent(Class<T> componentClass) {
    if (allComponents.containsKey(componentClass)) {
      throw new IllegalArgumentException("Component already exists");
    }
    try {
      T component = componentClass.getDeclaredConstructor().newInstance();
      return configureParentAndPutComponent(component);
    } catch (InstantiationException | IllegalAccessException |
             NoSuchMethodException | InvocationTargetException e) {
      LOGGER.error("Could not add component {}", componentClass.getName());
      throw new ComponentAddException("Failed to add component", e);
    }
  }

  /**
   * Add a component to the gameObject that was parsed
   *
   * @param component the instantiated GameComponent you wish to add (with configured values)
   * @return the added component instance
   * @apiNote Every component class should only have one instance per object.
   */

  public final <T extends GameComponent> T addComponent(T component) {
    Class<? extends GameComponent> componentClass = component.getClass();

    if (allComponents.containsKey(componentClass)) {
      throw new IllegalArgumentException("Component already exists");
    }

    return configureParentAndPutComponent(component);
  }

  private <T extends GameComponent> T configureParentAndPutComponent(T component) {
    component.setParent(this);

    if (parentScene == null) {
      componentAwakeInitializer.add(component::awake);
    } else {
      parentScene.subscribeEvent(component::awake);
      parentScene.registerComponent(component);
    }

    allComponents.put(component.getClass(), component);
    return component;
  }


  /**
   * Get the component based on the input component class type.
   *
   * @param componentClass the component class specified
   * @return the component instance
   */
  public final <T extends GameComponent> T getComponent(Class<T> componentClass) {
    if (!allComponents.containsKey(componentClass)) {
      throw new IllegalArgumentException("Component does not exist");
    }
    return componentClass.cast(allComponents.get(componentClass));
  }

  /**
   * Get the components based on the input component tag.
   *
   * @param tag the component tag specified
   * @return the components
   */
  public final Collection<GameComponent> getComponents(ComponentTag tag) {
    List<GameComponent> components = new ArrayList<>();
    for (GameComponent component : allComponents.values()) {
      if (component.componentTag() == tag) {
        components.add(component);
      }
    }
    return components;
  }

  /**
   * @param componentClass - Component looking for
   * @return - If current object has that component
   */
  public final boolean hasComponent(Class<? extends GameComponent> componentClass) {
    return allComponents.containsKey(componentClass);
  }

  /**
   * Returns all the components
   *
   * @return - a Map of some extended gameComponent to the GameComponent, representing all
   * components
   */
  public final Map<Class<? extends GameComponent>, GameComponent> getAllComponents() {
    return allComponents;
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

    // Check if the component is Transform
    if (componentClass.equals(Transform.class)) {
      LOGGER.error("Cannot remove Transform component");
      throw new IllegalArgumentException("Cannot remove Transform component");
    }

    GameComponent componentToRemove = allComponents.get(componentClass);
    componentToRemove.onRemove();

    if (parentScene != null) {
      parentScene.unregisterComponent(componentToRemove);
    }

    componentToRemove.setParent(null);
    allComponents.remove(componentClass);
  }

  /**
   * Create a new GameObject that has identical components to this GameObject
   *
   * @return new GameObject with same components but different UUID
   */
  @Override
  public GameObject clone() {

    GameObject copy = new GameObject(this.getName(), this.getTag());

    for (GameComponent comp : this.getAllComponents().values()) {
      // Assuming each component implements a deepCopy method.
      GameComponent compCopy = comp.copy();
      copy.addComponent(compCopy);
    }

    return copy;
  }

  /**
   * Change the scene to the specified scene name.
   *
   * @param sceneName the name of the scene to be changed to
   */
  final void changeScene(String sceneName) {
    parentScene.changeScene(sceneName);
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
    if (parentScene == null) {
      throw new MissingParentSceneException("GameObject does not have a parent scene");
    }
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

  /**
   * Setter for the parent scene
   *
   * @param parentScene - the scene in which the objects will be added to
   */
  public void setParentScene(GameScene parentScene) {
    this.parentScene = parentScene;
  }

}
