package oogasalad.model.engine.base.behavior;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import oogasalad.model.engine.action.CrouchAction;
import oogasalad.model.engine.component.Transform;

public class CrouchActionTest extends ActionsTest<CrouchAction> {

    @Override
    public void customSetUp() {
        CrouchAction action = getBehavior1().addAction(CrouchAction.class);
        setAction(action);
    }

    @Override
    @Test
    public void onPerform_performAction_performAction() {
        double initialY = getObj1().getComponent(Transform.class).getY();
        double initalScaleY = getObj1().getComponent(Transform.class).getScaleY();
        double crouchScaleY = initalScaleY / 2;
        double crouchY = initialY + crouchScaleY;
        assertEquals(initialY, getObj1().getComponent(Transform.class).getY());
        assertEquals(initalScaleY, getObj1().getComponent(Transform.class).getScaleY());

        getAction().onPerform(null);
        assertEquals(crouchY, getObj1().getComponent(Transform.class).getY());
        assertEquals(crouchScaleY, getObj1().getComponent(Transform.class).getScaleY());

        getAction().onPerform(null);
        assertEquals(initialY, getObj1().getComponent(Transform.class).getY());
        assertEquals(initalScaleY, getObj1().getComponent(Transform.class).getScaleY());
    }

}
