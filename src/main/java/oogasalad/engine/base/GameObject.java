package oogasalad.engine.base;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

/**
 * The GameObject class is the base class for all game objects. It is used to define the behavior of
 * game objects. Each game object can have multiple components, and each component can have its own
 * logic and behavior.
 */

public class GameObject {
  private final GameScene parentScene;
  private final UUID id;
  private final Map<Class<? extends GameComponent>, GameComponent> allComponents;

  private String name;

  public GameObject(String name, UUID id, GameScene parentScene) {
    this.name = name;
    this.id = id;
    this.parentScene = parentScene;
    this.allComponents = new HashMap<>();
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
