package oogasalad.engine.base.event;

import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.base.serialization.SerializableField;
import oogasalad.engine.component.Transform;

public class CrouchAction extends GameAction {

  @SerializableField
  private double crouchedHeight = 10;
  @SerializableField
  private double normalHeight = 20;

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
      transform.setScaleY(crouchedHeight);
      transform.setY(transform.getY() + (normalHeight - crouchedHeight));
    } else {
      transform.setScaleY(normalHeight);
      transform.setY(transform.getY() - (normalHeight - crouchedHeight));
    }

    isCrouched = !isCrouched;
  }
}
