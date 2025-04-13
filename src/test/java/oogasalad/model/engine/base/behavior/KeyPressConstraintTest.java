package oogasalad.model.engine.base.behavior;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import oogasalad.model.engine.base.enumerate.KeyCode;
import oogasalad.model.engine.component.InputHandler;
import oogasalad.model.engine.constraint.KeyPressConstraint;

public class KeyPressConstraintTest extends ConstraintsTest<KeyPressConstraint> {
    @Override
    public void customSetUp() {
        getObj1().addComponent(InputHandler.class);
        KeyPressConstraint constraint = getBehavior1().addConstraint(KeyPressConstraint.class);
        constraint.setParameter(KeyCode.A);
        setConstraint(constraint);
    }

    @Override
    @Test
    public void check_checkPosotive_returnsTrue() {
        getGame().keyPressed(KeyCode.A.getValue());
        step();
        assertTrue(getConstraint().onCheck(KeyCode.A));
    }

    @Override
    @Test
    public void check_checkNegative_returnsFalse() {
        step();
        assertFalse(getConstraint().onCheck(KeyCode.D));
    }
}
