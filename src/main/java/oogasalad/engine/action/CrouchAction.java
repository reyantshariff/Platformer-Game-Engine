package oogasalad.engine.action;

import oogasalad.engine.base.behavior.BehaviorAction;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.component.Transform;

public class CrouchAction extends BehaviorAction {

  private boolean isCrouched = false;

  public CrouchAction() {
    super();
  }

  /**
   * The type of the constraint. This is used to classify the constraints being checked. Note: This
   * method MUST be override.
   *
   * @return the type of the constraint
   */
  @Override
  public ComponentTag ActionType() {
    return ComponentTag.INPUT;
  }

  /**
   * Check if the constraint is met.
   *
   * @param parameter the parameter to check against
   * @return true if the constraint is met, false otherwise
   */
  @Override
  protected void perform(Object parameter) {
    Transform transform;
    try{
      transform = (Transform) getComponent(Transform.class);
    } catch (Exception e) {
      return;
    }


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
