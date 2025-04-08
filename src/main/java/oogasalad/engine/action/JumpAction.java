package oogasalad.engine.action;

import oogasalad.engine.base.behavior.BehaviorAction;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.component.PhysicsHandler;

public class JumpAction extends BehaviorAction<Double> {

  private PhysicsHandler physicsHandler;

  @Override
  public ComponentTag ActionType() {
    return ComponentTag.INPUT;
  }

  @Override
  public void awake(){
    physicsHandler = getComponent(PhysicsHandler.class);
  }

  @Override
  protected void perform(Double jumpForce) {
    physicsHandler.setVelocityY(-jumpForce);
  }
}
