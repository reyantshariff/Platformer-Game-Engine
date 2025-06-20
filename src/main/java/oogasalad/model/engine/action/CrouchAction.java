package oogasalad.model.engine.action;

import oogasalad.model.engine.base.behavior.BehaviorAction;
import oogasalad.model.engine.component.Transform;

/**
 * The CrouchAction class is used to make a game object crouch or uncrouch by changing its scale and
 * position.
 */

public class CrouchAction extends BehaviorAction<Void> {

  private boolean isCrouched = false;
  private Transform transform;

  /**
   * Default constructor for the CrouchAction class.
   */
  public CrouchAction() {
    super();
  }

  @Override
  protected Void defaultParameter() {
    return null;
  }

  @Override
  protected void awake() {
    transform = getComponent(Transform.class);
  }

  /**
   * Check if the constraint is met.
   */
  @Override
  protected void perform(Void unused) {
    if (!isCrouched) {
      transform.setScaleY(transform.getScaleY() / 2);
      transform.setY(transform.getY() + transform.getScaleY());
    } else {
      transform.setScaleY(transform.getScaleY() * 2);
      transform.setY(transform.getY() - transform.getScaleY() / 2);
    }

    isCrouched = !isCrouched;
  }
}
