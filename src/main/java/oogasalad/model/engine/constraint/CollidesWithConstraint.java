package oogasalad.model.engine.constraint;

import oogasalad.model.engine.base.behavior.BehaviorConstraint;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import oogasalad.model.engine.component.Collider;

public class CollidesWithConstraint extends BehaviorConstraint<String> {
  private Collider collider;

  @Override
  protected void awake() {
    collider = getComponent(Collider.class);
  }

  @Override
  protected boolean check(String tag) {
    return collider.collidesWith(tag);
  }

  @Override
  public ComponentTag ConstraintType() {
    return ComponentTag.COLLISION;
  }
}
