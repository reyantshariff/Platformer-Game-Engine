package oogasalad.model.engine.action;

import oogasalad.model.engine.base.behavior.Behavior;
import oogasalad.model.engine.base.behavior.BehaviorAction;

/**
 * The RemoveBehaviorAction class is a behavior action that removes a behavior from the behavior
 * controller.
 * 
 * @author Christian Bepler
 */

public class RemoveBehaviorAction extends BehaviorAction<Behavior> {

    @Override
    protected Behavior defaultParameter() {
        return new Behavior();
    }


    @Override
    protected void perform(Behavior behavior) {
        getBehavior().getController().removeBehavior(behavior);
    }
}
