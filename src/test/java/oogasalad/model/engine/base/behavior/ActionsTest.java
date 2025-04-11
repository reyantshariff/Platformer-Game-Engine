package oogasalad.model.engine.base.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import oogasalad.model.engine.component.Transform;

public abstract class ActionsTest<T extends BehaviorAction> extends BehaviorTest {

    private T action;

    public abstract void customSetUp();

    protected T getAction() {
        return action;
    }

    protected void setAction(T action) {
        this.action = action;
    }

    @Test
    public void getComponent_getParentTransform_returnsParentTransform() {
        Transform transform = getObj1().getComponent(Transform.class);
        assertEquals(transform, action.getComponent(Transform.class));
    }

    @Test
    public abstract void onPerform_performAction_performAction();
}
