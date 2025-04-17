package oogasalad.model.engine.action;

import oogasalad.model.engine.base.behavior.Behavior;
import oogasalad.model.engine.base.behavior.BehaviorAction;

/**
 * The AddBehaviorAction class is a behavior action that adds a behavior to the behavior controller.
 * 
 * @author Christian Bepler
 */

public class AddBehaviorAction extends BehaviorAction<Behavior> {

    @Override
    protected Behavior defaultParameter() {
        return new Behavior();
    }

    @Override
    protected void perform(Behavior behavior) {
        getBehavior().getController().addBehavior(behavior);
    }
}
