package oogasalad.engine.component;

import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.serialization.SerializableField;

public class VelocityComponent extends GameComponent {
  @SerializableField
  private double velocityX;
  @SerializableField
  private double velocityY;

  /**
   * This is the actual updating order of the component.
   *
   * @return the specified component tag
   */
  @Override
  public ComponentTag componentTag() {
    return ComponentTag.PHYSICS;
  }

  public double getVelocityX() { return  velocityX;}
  public void setVelocityX(double vx){ velocityX = vx; }

  public double getVelocityY() { return velocityY;}
  public void setVelocityY(double vy){ velocityY = vy; }

  @Override
  public void update(double deltaTime){
    Transform transform = getParent().getComponent(Transform.class);
    transform.setX(transform.getX() + velocityX * deltaTime);
    transform.setY(transform.getY() + velocityY * deltaTime);
  }
}
