package oogasalad.model.engine.constraint;

import oogasalad.model.engine.base.behavior.BehaviorConstraint;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import oogasalad.model.engine.base.enumerate.KeyCode;
import oogasalad.model.engine.component.InputHandler;

/**
 * KeyHoldConstraint is a class that extends BehaviorConstraint and is used to check if a key is
 * being held down.
 *
 * @author Hsuan-Kai Liao
 */
public class KeyHoldConstraint extends BehaviorConstraint<KeyCode> {
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
    return inputHandler!= null && inputHandler.isKeyHold(parameter);
  }
}
