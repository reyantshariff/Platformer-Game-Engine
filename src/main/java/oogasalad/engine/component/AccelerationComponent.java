package oogasalad.engine.component;

import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.serialization.SerializableField;

/**
 * Represents a physics component that applies acceleration to its parent GameObject.
 * This component updates the object's velocity over time based on X and Y acceleration.
 */
public class AccelerationComponent extends GameComponent {

  /** Acceleration in the X direction. */
  @SerializableField
  private double accelX;

  /** Acceleration in the Y direction. */
  @SerializableField
  private double accelY;

  /**
   * Returns the X-axis acceleration.
   *
   * @return the X acceleration value
   */
  public double getAccelX() {
    return accelX;
  }

  /**
   * Sets the X-axis acceleration.
   *
   * @param ax the new X acceleration value
   */
  public void setAccelX(double ax) {
    accelX = ax;
  }

  /**
   * Returns the Y-axis acceleration.
   *
   * @return the Y acceleration value
   */
  public double getAccelY() {
    return accelY;
  }

  /**
   * Sets the Y-axis acceleration.
   *
   * @param ay the new Y acceleration value
   */
  public void setAccelY(double ay) {
    accelY = ay;
  }

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
   * Updates the associated GameObject's velocity using current acceleration and deltaTime.
   *
   * @param deltaTime time passed since the last update
   */
  @Override
  public void update(double deltaTime) {
    VelocityComponent velocity = getParent().getComponent(VelocityComponent.class);
    if (velocity == null) return;

    double newVX = velocity.getVelocityX() + accelX * deltaTime;
    double newVY = velocity.getVelocityY() + accelY * deltaTime;

    velocity.setVelocityX(newVX);
    velocity.setVelocityY(newVY);
  }
}
