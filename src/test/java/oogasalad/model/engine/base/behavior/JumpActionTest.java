package oogasalad.model.engine.base.behavior;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import oogasalad.model.engine.action.JumpAction;
import oogasalad.model.engine.component.PhysicsHandler;

public class JumpActionTest extends ActionsTest<JumpAction> {

    @Override
    public void customSetUp() {
        getObj1().addComponent(PhysicsHandler.class);
        JumpAction action = getBehavior1().addAction(JumpAction.class);
        setAction(action);
    }

    @Override
    @Test
    public void onPerform_performAction_performAction() {
        double jumpForce = 10.0;
        assertEquals(0.0, getObj1().getComponent(PhysicsHandler.class).getVelocityY());
        getAction().onPerform(jumpForce);
        assertEquals(-jumpForce, getObj1().getComponent(PhysicsHandler.class).getVelocityY());
    }

}
