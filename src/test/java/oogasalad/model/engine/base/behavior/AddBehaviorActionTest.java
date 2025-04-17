package oogasalad.model.engine.base.behavior;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import oogasalad.model.engine.action.AddBehaviorAction;
import oogasalad.model.engine.component.BehaviorController;

public class AddBehaviorActionTest extends ActionsTest<AddBehaviorAction> {

    @Override
    public void customSetUp() {
        AddBehaviorAction action = getBehavior1().addAction(AddBehaviorAction.class);
        setAction(action);
    }

    @Override
    @Test
    public void onPerform_performAction_performAction() {
        Behavior behavior = new Behavior();
        assertEquals(1, getObj1().getComponent(BehaviorController.class).getBehaviors().size());
        getAction().onPerform(behavior);
        assertEquals(2, getObj1().getComponent(BehaviorController.class).getBehaviors().size());
    }
}
