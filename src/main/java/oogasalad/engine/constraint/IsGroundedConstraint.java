package oogasalad.engine.constraint;

import oogasalad.engine.base.behavior.BehaviorConstraint;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.serialization.SerializableField;
import oogasalad.engine.component.Collider;
import oogasalad.engine.component.Transform;
import oogasalad.engine.base.architecture.GameObject;

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
