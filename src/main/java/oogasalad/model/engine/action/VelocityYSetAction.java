package oogasalad.model.engine.action;

import oogasalad.model.engine.base.behavior.BehaviorAction;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import oogasalad.model.engine.component.PhysicsHandler;

/**
 * VelocityXSetAction is a class that extends BehaviorAction and is used to set the X velocity of
 * the object.
 *
 * @author Hsuan-Kai Liao
 */
public class VelocityYSetAction extends BehaviorAction<Double> {

  private PhysicsHandler physicsHandler;

  @Override
  protected Double defaultParameter() {
    return 0.0;
  }

  @Override
  protected void awake() {
    physicsHandler = getComponent(PhysicsHandler.class);
  }

  @Override
  protected void perform(Double parameter) {
    physicsHandler.setVelocityY(parameter);
  }
}
