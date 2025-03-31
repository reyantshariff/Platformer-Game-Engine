package oogasalad.engine.base;

/**
 * The GameAction class is the base class for all game actions. Actions are event that are called only under
 * certain conditions. For example, a player will only jump when the user presses the jump button.
 */

public abstract class GameAction {

    private GameObject parent;

    public GameAction(GameObject parent) {
        this.parent = parent;
    }

    /**
     * Trigger the action
     */
    public abstract void dispatch();

    /**
     * Get the parent of the action
     * 
     * @return the parent of the action
     */
    public GameObject getParent() {
        return parent;
    }
}
