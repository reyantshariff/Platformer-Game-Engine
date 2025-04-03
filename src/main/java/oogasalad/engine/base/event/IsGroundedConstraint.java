package oogasalad.engine.base.event;

import oogasalad.engine.component.Transform;

public class IsGroundedConstraint extends GameActionConstraint{

  IsGroundedConstraint(GameAction parentAction) {
    super(parentAction);
  }

  /**
   * checks if the constraint is met
   *
   * @return true if the constraint is met, false otherwise
   */
  @Override
  public boolean check() {
    Transform transform = getParentObject().getComponent(Transform.class);

    //Placeholder assuming ground is at y = 100
    //TODO: Replace this with a IsCollidingWith(Ground) check
    return transform.getY() >= 100;
  }
}
