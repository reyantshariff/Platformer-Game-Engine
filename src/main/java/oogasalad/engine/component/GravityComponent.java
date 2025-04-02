package oogasalad.engine.component;

import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.serialization.Serializable;
import oogasalad.engine.base.serialization.SerializableField;

public class GravityComponent extends GameComponent implements Serializable {
  @SerializableField
  private double gravity;

  /**
   * This is the actual updating order of the component.
   *
   * @return the specified component tag
   */
  @Override
  public ComponentTag componentTag() {
    return ComponentTag.PHYSICS;
  }

  @Override
  public void update(double deltaTime) {
    VelocityComponent velocity = getParent().getComponent(VelocityComponent.class);
    velocity.setVelocityY(velocity.getVelocityY() + gravity * deltaTime);
  }
}
