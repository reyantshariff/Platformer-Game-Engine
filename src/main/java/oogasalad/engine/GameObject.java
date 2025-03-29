package oogasalad.engine;

import java.util.Map;

/**
 * The GameObject class is the base class for all game objects. It is used to define the behavior of
 * game objects. Each game object can have multiple components, and each component can have its own
 * logic and behavior.
 */

public abstract class GameObject {
  private GameScene parentScene;
  private String name;
  private Map<Class<? extends GameComponent>, GameComponent> allComponents;

  /**
   * Add the component to the gameObject based on its class.
   * 
   * @apiNote Every component class should only have one instance per object.
   * @param componentClass the component class specified
   * @return the added component instance
   */
  public <T extends GameComponent> T addComponent(Class<T> componentClass) {
    return null;
  }

  /**
   * Get the component based on
   * 
   * @param componentClass the component class specified
   * @return the component instance
   */
  public <T extends GameComponent> T getComponent(Class<T> componentClass) {
    return null;
  }

  /**
   * Remove the component based on its class.
   * 
   * @param componentClass the component class specified
   */
  public <T extends GameComponent> void removeComponent(Class<T> componentClass) {

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
   * Returns the parent scene of the object.
   * 
   * @return the parent scene of the object
   */
  public GameScene getScene() {
    return parentScene;
  }

}
