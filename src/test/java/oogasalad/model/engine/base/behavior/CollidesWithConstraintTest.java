package oogasalad.model.engine.base.behavior;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import oogasalad.model.engine.constraint.CollidesWithConstraint;
import oogasalad.model.engine.component.Transform;

public class CollidesWithConstraintTest extends ConstraintsTest<CollidesWithConstraint> {

    private Transform transform1;
    private Transform transform2;
    
    @Override
    public void customSetUp() {
        CollidesWithConstraint constraint = getBehavior1().addConstraint(CollidesWithConstraint.class);
        setConstraint(constraint);
        transform1 = getObj1().getComponent(Transform.class);
        transform2 = getObj2().getComponent(Transform.class);
    }

    @Override @Test
    public void check_checkPosotive_returnsTrue() {
        transform1.setX(transform2.getX());
        transform1.setY(transform2.getY());
        step();
        assertTrue(getConstraint().onCheck(getObj2().getTag()));
    }

    @Override @Test
    public void check_checkNegative_returnsFalse() {
        transform1.setX(0);
        transform1.setY(0);
        transform2.setX(1000);
        transform2.setY(1000);
        step();
        assertFalse(getConstraint().onCheck(getObj2().getTag()));
    }
}
