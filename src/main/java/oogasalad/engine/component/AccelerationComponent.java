package oogasalad.engine.component;

import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.serialization.SerializableField;

public class AccelerationComponent extends GameComponent {

  @SerializableField
  private double accelX;

  @SerializableField
  private double accelY;

  public double getAccelX() { return accelX; }
  public void setAccelX(double ax) { accelX = ax; }

  public double getAccelY() { return accelY; }
  public void setAccelY(double ay) { accelY = ay; }

  @Override
  public ComponentTag componentTag() {
    return ComponentTag.PHYSICS;
  }

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
