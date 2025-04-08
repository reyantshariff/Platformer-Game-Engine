package oogasalad.engine.action;

import oogasalad.engine.base.behavior.BehaviorAction;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.component.PhysicsHandler;

/**
 * VelocityXSetAction is a class that extends BehaviorAction and is used to set the X velocity of
 * the object.
 *
 * @author Hsuan-Kai Liao
 */
public class VelocityXSetAction extends BehaviorAction<Double> {
  @Override
  public ComponentTag ActionType() {
    return ComponentTag.PHYSICS;
  }

  private PhysicsHandler physicsHandler;

  @Override
  protected void awake() {
    physicsHandler = getComponent(PhysicsHandler.class);
  }

  @Override
  protected void perform(Double parameter) {
    physicsHandler.setVelocityX(parameter);
  }
}
