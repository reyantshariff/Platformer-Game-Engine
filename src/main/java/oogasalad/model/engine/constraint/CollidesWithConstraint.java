package oogasalad.model.engine.constraint;

import oogasalad.model.engine.base.behavior.BehaviorConstraint;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import oogasalad.model.engine.component.Collider;

/**
 * This class defines a constraint that checks if a GameObject's Collider component
 * collides with another GameObject that has a Collider component with a specified tag.
 * It extends the BehaviorConstraint class, using a String to specify the target tag.
 */
public class CollidesWithConstraint extends BehaviorConstraint<String> {
  private Collider collider;

  @Override
  protected void awake() {
    System.out.println("CollidesWithConstraint awake");
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