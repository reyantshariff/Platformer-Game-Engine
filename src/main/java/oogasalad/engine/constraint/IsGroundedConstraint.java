package oogasalad.engine.constraint;

import oogasalad.engine.base.behavior.BehaviorConstraint;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.component.Collider;
import oogasalad.engine.component.Transform;
import oogasalad.engine.base.architecture.GameObject;

public class IsGroundedConstraint extends BehaviorConstraint<Void> {

  @Override
  public ComponentTag ConstraintType() {
    return ComponentTag.COLLISION;
  }

  @Override
  protected boolean check(Void unused) {
    Transform t = getComponent(Transform.class);
    Collider c = getComponent(Collider.class);
    GameObject self = getComponent(Transform.class).getParent();

    double selfBottom = t.getY() + t.getScaleY();

    for (GameObject other : self.getScene().getAllObjects()) {
      if (other == self) continue;

      try {
        Transform tOther = other.getComponent(Transform.class);

        boolean horizontallyAligned =
            t.getX() + t.getScaleX() > tOther.getX() &&
                t.getX() < tOther.getX() + tOther.getScaleX();

        boolean touchingFromAbove = Math.abs(selfBottom - tOther.getY()) < 2;

        if (horizontallyAligned && touchingFromAbove) {
          return true;
        }
      } catch (Exception ignored) {}
    }

    return false;
  }
}
