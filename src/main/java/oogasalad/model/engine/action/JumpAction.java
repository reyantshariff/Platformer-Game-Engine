package oogasalad.model.engine.action;

import oogasalad.model.engine.base.behavior.BehaviorAction;
import oogasalad.model.engine.component.PhysicsHandler;

/**
 * The JumpAction class is used to make a game object jump by setting its vertical velocity.
 */

public class JumpAction extends BehaviorAction<Double> {

  private PhysicsHandler physicsHandler;

  @Override
  protected Double defaultParameter() {
    return 0.0;
  }

  @Override
  public void awake() {
    physicsHandler = getComponent(PhysicsHandler.class);
  }

  @Override
  protected void perform(Double jumpForce) {
    physicsHandler.setVelocityY(-jumpForce);
  }
}
