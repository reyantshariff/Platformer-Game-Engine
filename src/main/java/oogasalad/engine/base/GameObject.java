package oogasalad.engine.base;

import java.util.*;

/**
 * The GameObject class is the base class for all game objects. It is used to define the behavior of
 * game objects. Each game object can have multiple components, and each component can have its own
 * logic and behavior.
 */

public class GameObject {
  private final UUID id;
  private final Map<Class<? extends GameComponent>, GameComponent> allComponents;
  private final List<Runnable> awakeMethods;

  private GameScene parentScene;
  private String name;

  public GameObject(String name) {
    this.id = UUID.randomUUID();
    this.name = name == null ? "" : this.getClass().getSimpleName() + "_" + this.id;
    this.allComponents = new HashMap<>();
    this.awakeMethods = new ArrayList<>();
  }

  void wakeUp() {
    awakeMethods.forEach(Runnable::run);
  }

  void setParentScene(GameScene parentScene) {
    this.parentScene = parentScene;
  }

  /**
   * Add the component to the gameObject based on its class.
   * 
   * @apiNote Every component class should only have one instance per object.
   * @param componentClass the component class specified
   * @return the added component instance
   */
  public <T extends GameComponent> T addComponent(Class<T> componentClass) {
    if (allComponents.containsKey(componentClass)) {
      throw new IllegalArgumentException("Component already exists");
    }
    try {
      T component = componentClass.getDeclaredConstructor().newInstance();
      component.setParent(this);
      awakeMethods.add(component::awake);
      allComponents.put(componentClass, component);
      parentScene.registerComponent(component);
      return component;
    } catch (Exception e) {
      throw new RuntimeException("Failed to add component", e);
    }
  }

  /**
   * Get the component based on
   * 
   * @param componentClass the component class specified
   * @return the component instance
   */
  public <T extends GameComponent> T getComponent(Class<T> componentClass) {
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
  public <T extends GameComponent> void removeComponent(Class<T> componentClass) {
    if (!allComponents.containsKey(componentClass)) {
      throw new IllegalArgumentException("Component does not exist");
    }

    parentScene.unregisterComponent(allComponents.get(componentClass));
    allComponents.remove(componentClass);
  }

  /**
   * Returns the name of the object.
   * 
   * @return the name of the object
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the ID of the object.
   * 
   * @return the ID of the object
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the parent scene of the object.
   * 
   * @return the parent scene of the object
   */
  public GameScene getScene() {
    return parentScene;
  }

  /**
   * Sets the name of the object.
   * 
   * @param name the name of the object
   */
  public void setName(String name) {
    this.name = name;
  }
}
