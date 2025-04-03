package oogasalad.engine.base.event;

import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.base.serialization.SerializableField;
import oogasalad.engine.component.Transform;

public class CrouchAction extends GameAction {

  private boolean isCrouched = false;

  public CrouchAction(GameObject parent) {
    super(parent);
  }

  /**
   * Trigger the action Implementations of this method should call checkConstraints before executing
   * the body
   */
  @Override
  public void dispatch() {
    Transform transform = getParent().getComponent(Transform.class);

    if(!isCrouched){
      transform.setScaleY(transform.getScaleY()/2);
      transform.setY(transform.getY() + transform.getScaleY());
    } else {
      transform.setScaleY(transform.getScaleY()*2);
      transform.setY(transform.getY() - transform.getScaleY()/2);
    }

    isCrouched = !isCrouched;
  }
}
