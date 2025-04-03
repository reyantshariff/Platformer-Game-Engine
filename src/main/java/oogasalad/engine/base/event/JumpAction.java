package oogasalad.engine.base.event;

import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.base.serialization.Serializable;
import oogasalad.engine.base.serialization.SerializableField;
import oogasalad.engine.component.VelocityComponent;

public class JumpAction extends GameAction implements Serializable {
  @SerializableField
  private double jumpForce = -180.0;

  public JumpAction() {
    super();
  }

  /**
   * Trigger the action Implementations of this method should call checkConstraints before executing
   * the body
   */
  @Override
  public void dispatch() {
    if(!checkConstraints()) return;

    VelocityComponent velocity = getParent().getComponent(VelocityComponent.class);
    velocity.setVelocityY(jumpForce);
  }
}
