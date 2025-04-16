package oogasalad.model.engine.base.behavior;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import oogasalad.model.engine.component.Transform;


public abstract class ConstraintsTest<T extends BehaviorConstraint<?>> extends BehaviorBaseTest {
    private T constraint;

    public abstract void customSetUp();

    protected T getConstraint() {
        return constraint;
    }

    protected void setConstraint(T constraint) {
        this.constraint = constraint;
    }

    @Test
    public void getComponent_getParentTransform_returnsParentTransform() {
        Transform transform = getObj1().getComponent(Transform.class);
        assertEquals(transform, constraint.getComponent(Transform.class));
    }

    @Test
    public abstract void check_checkPositive_returnsTrue();

    @Test
    public abstract void check_checkNegative_returnsFalse();

}
