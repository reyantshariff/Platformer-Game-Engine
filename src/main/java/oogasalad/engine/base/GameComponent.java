package oogasalad.engine.base;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * The GameComponent class is the base class for all game components. It is used to define the
 * behavior of game objects. Each game object can have multiple components, and each component can
 * have its own logic and behavior.
 */
public abstract class GameComponent implements Serializable {
  private GameObject parent;

  /**
   * This method is called after all objects have been created and initialized. It is used to set up
   * references to other objects and components. If an object is made mid-game, this method is called
   * right after the constructor.
   */
  public void awake() {}

  /**
   * This method is called before the object calls its update method for the first time
   * This should be over
   */
  public void start() {}

  /**
   * This method is called every frame. It is used to update the object and perform any necessary
   * game logic.
   *
   * @param deltaTime The time since the last frame, in seconds.
   */
  public void update(double deltaTime) {}

  /**
   * This is the actual updating order of the component.
   * @return the specified component tag
   */
  public abstract ComponentTag componentTag();

  /**
   * Get the parent gameObject of the component
   */
  protected GameObject getParent() {
    return parent;
  }

  void setParent(GameObject parent) {
    this.parent = parent;
  }
}
