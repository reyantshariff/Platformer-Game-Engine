package oogasalad.engine.base.event;

import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.component.VelocityComponent;

public class JumpAction extends GameAction{
  private static final double JUMP_FORCE = -500.0;

  public JumpAction(GameObject parent) {
    super(parent);
  }

  /**
   * Trigger the action Implementations of this method should call checkConstraints before executing
   * the body
   */
  @Override
  public void dispatch() {
    if(!checkConstraints()) return;

    VelocityComponent velocity = getParent().getComponent(VelocityComponent.class);
    velocity.setVelocityY(JUMP_FORCE);
  }
}
