package oogasalad.model.engine.base.behavior;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import oogasalad.model.engine.component.InputHandler;
import oogasalad.model.engine.constraint.KeyHoldConstraint;
import oogasalad.model.engine.base.architecture.KeyCode;

public class KeyHoldConstraintTest extends ConstraintsTest<KeyHoldConstraint> {
    
    @Override
    public void customSetUp() {
        getObj1().addComponent(InputHandler.class);
        KeyHoldConstraint constraint = getBehavior1().addConstraint(KeyHoldConstraint.class);
        constraint.setParameter(KeyCode.A);
        setConstraint(constraint);
    }

    @Override @Test
    public void check_checkPositive_returnsTrue() {
        getGame().keyPressed(KeyCode.A.getValue());
        step();
        assertTrue(getConstraint().onCheck(KeyCode.A));
    }

    @Override @Test
    public void check_checkNegative_returnsFalse() {
        step();
        assertFalse(getConstraint().onCheck(KeyCode.D));
    }
}
