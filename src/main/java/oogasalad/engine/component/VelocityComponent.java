package oogasalad.engine.component;

import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.serialization.SerializableField;

/**
 * A physics component that applies velocity to a GameObject over time.
 * It updates the object's position based on the velocity vector (X and Y axes)
 * during each game update cycle.
 */
public class VelocityComponent extends GameComponent {

  /** The velocity of the GameObject along the X-axis. */
  @SerializableField
  private double velocityX;

  /** The velocity of the GameObject along the Y-axis. */
  @SerializableField
  private double velocityY;

  /**
   * Returns the component tag identifying this as a physics-related component.
   *
   * @return {@link ComponentTag#PHYSICS}
   */
  @Override
  public ComponentTag componentTag() {
    return ComponentTag.PHYSICS;
  }

  /**
   * Returns the velocity along the X-axis.
   *
   * @return the X-axis velocity
   */
  public double getVelocityX() {
    return velocityX;
  }

  /**
   * Sets the velocity along the X-axis.
   *
   * @param vx the new X-axis velocity
   */
  public void setVelocityX(double vx) {
    this.velocityX = vx;
  }

  /**
   * Returns the velocity along the Y-axis.
   *
   * @return the Y-axis velocity
   */
  public double getVelocityY() {
    return velocityY;
  }

  /**
   * Sets the velocity along the Y-axis.
   *
   * @param vy the new Y-axis velocity
   */
  public void setVelocityY(double vy) {
    this.velocityY = vy;
  }

  /**
   * Updates the GameObject's position using its velocity and the time delta.
   * This moves the object along both axes based on velocity and frame time.
   *
   * @param deltaTime time passed since the last update
   */
  @Override
  public void update(double deltaTime) {
    Transform transform = getParent().getComponent(Transform.class);
    transform.setX(transform.getX() + velocityX * deltaTime);
    transform.setY(transform.getY() + velocityY * deltaTime);
  }
}