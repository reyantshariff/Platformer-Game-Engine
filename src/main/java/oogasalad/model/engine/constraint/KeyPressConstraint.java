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
public class KeyPressConstraint extends BehaviorConstraint<KeyCode> {

  private InputHandler inputHandler;

  @Override
  protected KeyCode defaultParameter() {
    return KeyCode.NONE;
  }

  @Override
  protected void awake() {
    inputHandler = getComponent(InputHandler.class);
  }

  @Override
  public boolean check(KeyCode parameter) {
    return inputHandler!= null && inputHandler.isKeyPressed(parameter);
  }
}
