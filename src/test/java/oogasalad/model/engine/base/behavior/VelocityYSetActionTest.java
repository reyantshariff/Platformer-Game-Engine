package oogasalad.model.engine.base.behavior;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import oogasalad.model.engine.action.VelocityYSetAction;
import oogasalad.model.engine.component.PhysicsHandler;

public class VelocityYSetActionTest extends ActionsTest<VelocityYSetAction> {

    @Override
    public void customSetUp() {
        getObj1().addComponent(PhysicsHandler.class);
        VelocityYSetAction action = getBehavior1().addAction(VelocityYSetAction.class);
        setAction(action);
    }

    @Override
    @Test
    public void onPerform_performAction_performAction() {
        double velocityY = 10.0;
        assertEquals(0.0, getObj1().getComponent(PhysicsHandler.class).getVelocityY());
        getAction().onPerform(velocityY);
        assertEquals(velocityY, getObj1().getComponent(PhysicsHandler.class).getVelocityY());
    }

}
