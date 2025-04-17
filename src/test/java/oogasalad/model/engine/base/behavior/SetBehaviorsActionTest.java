package oogasalad.model.engine.base.behavior;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import oogasalad.model.engine.action.SetBehaviorsAction;
import oogasalad.model.engine.component.BehaviorController;

public class SetBehaviorsActionTest extends ActionsTest<SetBehaviorsAction> {

    @Override
    public void customSetUp() {
        SetBehaviorsAction action = getBehavior1().addAction(SetBehaviorsAction.class);
        setAction(action);
    }

    @Override
    @Test
    public void onPerform_performAction_performAction() {
        assertEquals(1, getObj1().getComponent(BehaviorController.class).getBehaviors().size());
        getAction().onPerform(new ArrayList<Behavior>());
        assertEquals(0, getObj1().getComponent(BehaviorController.class).getBehaviors().size());
        List<Behavior> behaviors = new ArrayList<>();
        behaviors.add(new Behavior());
        behaviors.add(new Behavior());
        getAction().onPerform(behaviors);
        assertEquals(2, getObj1().getComponent(BehaviorController.class).getBehaviors().size());
    }
}
