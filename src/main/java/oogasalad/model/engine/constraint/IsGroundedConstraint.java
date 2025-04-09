package oogasalad.model.engine.constraint;

import oogasalad.model.engine.base.behavior.BehaviorConstraint;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import oogasalad.model.engine.base.serialization.SerializableField;
import oogasalad.model.engine.component.Collider;

public class IsGroundedConstraint extends BehaviorConstraint<Void> {
  @SerializableField
  private double tolerance = 2.0;

  @Override
  public ComponentTag ConstraintType() {
    return ComponentTag.COLLISION;
  }

  @Override
  protected boolean check(Void unused) {
    Collider c = getComponent(Collider.class);
    return c.collidesWith("Base") && c.touchingFromAbove("Base", tolerance) && c.horizontallyAligned("Base");
  }
}
