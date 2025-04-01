package oogasalad.engine.base.event;

import oogasalad.engine.base.architecture.GameObject;

/**
 * The GameActionConstraint class is the base class for all game action constraints. Constraints are
 * conditions that must be met in order for an action to be triggered. For example, a player will
 * only jump when the user presses the jump button and the player is on the ground.
 */

public abstract class GameActionConstraint {
    private final GameAction parentAction;
    private final GameObject parentObject;

    GameActionConstraint(GameAction parentAction) {
        this.parentAction = parentAction;
        this.parentObject = parentAction.getParent();
    }

    /**
     * checks if the constraint is met
     * @return true if the constraint is met, false otherwise
     */
    public abstract boolean check();

    /**
     * Gets the parent action of the constraint
     * 
     * @return the parent action
     */
    public final GameAction getParentAction() {
        return parentAction;
    }

    /**
     * Gets the parent object of the constraint
     * 
     * @return the parent object
     */
    public final GameObject getParentObject() {
        return parentObject;
    }
}
