package oogasalad.model.engine.base.behavior;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import oogasalad.model.engine.component.InputHandler;
import oogasalad.model.engine.constraint.MouseClickConstraint;

public class MouseClickConstraintTest extends ConstraintsTest<MouseClickConstraint> {

  @Override
  public void customSetUp() {
    getObj1().addComponent(InputHandler.class);
    MouseClickConstraint constraint = getBehavior1().addConstraint(MouseClickConstraint.class);
    setConstraint(constraint);
  }

  @Override
  @Test
  public void check_checkPositive_returnsTrue() {
    getObj1().getComponent(InputHandler.class).registerMouseClick(250.0, 300.0);
    assertTrue(getConstraint().onCheck(null));
  }

  @Override
  @Test
  public void check_checkNegative_returnsFalse() {
    step();
    assertFalse(getConstraint().onCheck(null));
  }
}
