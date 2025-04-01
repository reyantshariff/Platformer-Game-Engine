package oogasalad.engine.base.architecture;

import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.serialization.Serializable;

/**
 * The GameComponent class is the base class for all game components. It is used to define the
 * behavior of game objects. Each game object can have multiple components, and each component can
 * have its own logic and behavior.
 *
 * @author Hsuan-Kai Liao, Christian Bepler
 */
public abstract class GameComponent implements Serializable {
  private GameObject parent;

  /**
   * This method is called after all objects have been created and initialized. It is used to set up
   * references to other objects and components. If an object is made mid-game, this method is called
   * right after the constructor.
   * NOTE: This method should be override if needed.
   */
  public void awake() {}

  /**
   * This method is called before the object calls its update method for the first time
   * NOTE: This method should be override if needed.
   */
  public void start() {}

  /**
   * This method is called when the component is removed.
   * NOTE: This method should be override if needed.
   */
  public void onRemove() {}

  /**
   * This method is called every frame. It is used to update the object and perform any necessary
   * game logic.
   * NOTE: This method should be override if needed.
   * @param deltaTime The time since the last frame, in seconds.
   */
  public void update(double deltaTime) {}

  /**
   * Add the component to the gameObject based on its class.
   *
   * @apiNote Every component class should only have one instance per object.
   * @param componentClass the component class specified
   * @return the added component instance
   */
  protected final <T extends GameComponent> T addComponent(Class<T> componentClass) {
    if (parent != null) {
      return parent.addComponent(componentClass);
    }

    throw new IllegalArgumentException("Parent gameObject not exist!");
  }

  /**
   * Get the component based on
   *
   * @param componentClass the component class specified
   * @return the component instance
   */
  protected final <T extends GameComponent> T getComponent(Class<T> componentClass) {
    if (parent != null) {
      return parent.getComponent(componentClass);
    }

    throw new IllegalArgumentException("Parent gameObject not exist!");
  }

  /**
   * Remove the component based on its class.
   *
   * @param componentClass the component class specified
   */
  protected final <T extends GameComponent> void removeComponent(Class<T> componentClass) {
    if (parent != null) {
      parent.removeComponent(componentClass);
    }

    throw new IllegalArgumentException("Parent gameObject not exist!");
  }

  /**
   * This is the actual updating order of the component.
   * NOTE: This method MUST be override.
   * @return the specified component tag
   */
  public abstract ComponentTag componentTag();

  /**
   * Get the parent gameObject of the component
   */
  public final GameObject getParent() {
    return parent;
  }

  /**
   * Set the parent gameObject of the component
   */
  final void setParent(GameObject parent) {
    this.parent = parent;
  }
}
