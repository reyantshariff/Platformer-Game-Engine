package oogasalad.model.engine.constraint;

import oogasalad.model.engine.base.behavior.BehaviorConstraint;
import oogasalad.model.engine.component.InputHandler;

/**
 * Constraint that is satisfied if the object's InputHandler registers a mouse click this frame.
 */
public class MouseClickConstraint extends BehaviorConstraint<Void> {

  private InputHandler inputHandler;

  /**
   * This method is called to get the default parameter.
   *
   * @return the default parameter
   */
  @Override
  protected Void defaultParameter() {
    return null;
  }

  @Override
  protected void awake() {
    inputHandler = getComponent(InputHandler.class);
  }

  /**
   * @param unused the parameter to check against (void since this constraint doesn't take input
   * @return - if mouse is clicked by asking inputHandler
   */
  @Override
  public boolean check(Void unused) {
    return inputHandler != null && inputHandler.isMouseClicked();
  }
}
