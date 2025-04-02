package oogasalad.engine.base.event;

import java.util.ArrayList;
import java.util.List;
import oogasalad.engine.base.architecture.GameObject;

/**
 * The GameAction class is the base class for all game actions. Actions are event that are called
 * only under certain conditions. For example, a player will only jump when the user presses the
 * jump button.
 */

public abstract class GameAction {

    private final GameObject parent;
    private final List<GameActionConstraint> constraints;

    public GameAction(GameObject parent) {
        this.parent = parent;
        this.constraints = new ArrayList<>();
    }

    /**
     * Trigger the action
     * Implementations of this method should call checkConstraints before executing the body
     */
    public abstract void dispatch();

    /**
     * Get the parent of the action
     * 
     * @return the parent of the action
     */
    public final GameObject getParent() {
        return parent;
    }

    public final void registerConstraint(Class<? extends GameActionConstraint> constraintClass) {
        try {
            constraints
                    .add(constraintClass.getDeclaredConstructor(GameAction.class).newInstance(this));
        } catch (Exception e) {
            throw new RuntimeException("Failed to register constraint", e);
        }
    }

    public final void unregisterConstraint(Class<? extends GameActionConstraint> constraintClass) {
        for (GameActionConstraint constraint : constraints) {
            if (constraint.getClass().equals(constraintClass)) {
                constraints.remove(constraint);
                return;
            }
        }
        throw new IllegalArgumentException("Constraint does not exist");
    }

    /**
     * Check all constraints of the action
     * 
     * @return true if all constraints are met, false otherwise
     */
    public final boolean checkConstraints() {
        for (GameActionConstraint constraint : constraints) {
            if (!constraint.check()) {
                return false;
            }
        }
        return true;
    }
}
