package oogasalad.model.engine.base.behavior;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import oogasalad.model.engine.action.RemoveBehaviorAction;
import oogasalad.model.engine.component.BehaviorController;

public class RemoveBehaviorActionTest extends ActionsTest<RemoveBehaviorAction> {

    @Override
    public void customSetUp() {
        RemoveBehaviorAction action = getBehavior1().addAction(RemoveBehaviorAction.class);
        setAction(action);
    }

    @Override
    @Test
    public void onPerform_performAction_performAction() {
        assertEquals(1, getObj1().getComponent(BehaviorController.class).getBehaviors().size());
        getAction().onPerform(getBehavior1());
        assertEquals(0, getObj1().getComponent(BehaviorController.class).getBehaviors().size());
    }
}
