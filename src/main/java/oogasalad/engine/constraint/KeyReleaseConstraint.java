package oogasalad.engine.constraint;

import oogasalad.engine.base.behavior.BehaviorConstraint;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.enumerate.KeyCode;
import oogasalad.engine.component.InputHandler;

/**
 * KeyHoldConstraint is a class that extends BehaviorConstraint and is used to check if a key is
 * being held down.
 *
 * @author Hsuan-Kai Liao
 */
public class KeyReleaseConstraint extends BehaviorConstraint<KeyCode> {
  @Override
  public ComponentTag ConstraintType() {
    return ComponentTag.INPUT;
  }

  private InputHandler inputHandler;
  
  @Override
  protected void awake() {
    inputHandler = getComponent(InputHandler.class);
  }

  @Override
  public boolean check(KeyCode parameter) {
    return inputHandler!= null && inputHandler.isKeyReleased(parameter);
  }
}
