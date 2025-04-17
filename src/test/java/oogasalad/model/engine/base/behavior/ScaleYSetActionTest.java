package oogasalad.model.engine.base.behavior;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import oogasalad.model.engine.action.ScaleYSetAction;
import oogasalad.model.engine.component.Transform;

public class ScaleYSetActionTest extends ActionsTest<ScaleYSetAction> {

    @Override
    @Test
    public void customSetUp() {
        ScaleYSetAction action = getBehavior1().addAction(ScaleYSetAction.class);
        setAction(action);
    }

    @Override
    @Test
    public void onPerform_performAction_performAction() {
        double scaleY = 10.0;
        assertEquals(100.0, getObj1().getComponent(Transform.class).getScaleY());
        getAction().onPerform(scaleY);
        assertEquals(scaleY, getObj1().getComponent(Transform.class).getScaleY());
    }
}
