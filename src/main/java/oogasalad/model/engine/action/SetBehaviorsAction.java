package oogasalad.model.engine.action;

import java.util.Collection;

import oogasalad.model.engine.base.behavior.Behavior;
import oogasalad.model.engine.base.behavior.BehaviorAction;

/**
 * The SetBehaviorsAction class is a behavior action that sets the behaviors of the behavior
 * controller.
 * 
 * @author Christian Bepler
 */

public class SetBehaviorsAction extends BehaviorAction<Collection<Behavior>> {
    @Override
    protected Collection<Behavior> defaultParameter() {
        return null;
    }

    @Override
    public void awake() {}

    @Override
    protected void perform(Collection<Behavior> behaviors) {
        getBehavior().getController().removeAllBehaviors();
        for (Behavior behavior : behaviors) {
            getBehavior().getController().addBehavior(behavior);
        }
    }
}
