package oogasalad.model.engine.constraint;

import oogasalad.model.engine.base.behavior.BehaviorConstraint;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import oogasalad.model.engine.component.Collider;

/**
 * This class defines a constraint that checks if a GameObject's Collider component
 * is touching another GameObject's Collider component from above, within a specified tolerance.
 * It extends the BehaviorConstraint class, using a String to specify the target tag.
 */
public class TouchingFromAboveConstraint extends BehaviorConstraint<String> {
  private Collider collider;
  private static final double TOLERANCE = 5.0;

  @Override
  protected String defaultParameter() {
    return "";
  }

  @Override
  protected void awake() {
    collider = getComponent(Collider.class);
  }

  @Override
  protected boolean check(String tag) {
    return collider.touchingFromAbove(tag, TOLERANCE);
  }

}