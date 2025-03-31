package oogasalad.engine.base;

/**
 * The GameComponent class is the base class for all game components. It is used to define the
 * behavior of game objects. Each game object can have multiple components, and each component can
 * have its own logic and behavior.
 */

public abstract class GameComponent {
  private GameObject parent;
  private ComponentTag componentTag;

  protected GameComponent(ComponentTag componentTag) {
    this.componentTag = componentTag;
  }

  /**
   * This method is called before the object calls its update method for the first time
   */
  public abstract void start();

  /**
   * This method is called every frame. It is used to update the object and perform any necessary
   * game logic.
   *
   * @param deltaTime The time since the last frame, in seconds.
   */
  public void update(double deltaTime) {}

  /**
   * Gets the component tag
   * @return the component tag
   */
  public ComponentTag getComponentTag() {
    return componentTag;
  }

  protected GameObject getParent() {
    return parent;
  }
}
