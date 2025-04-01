package oogasalad.engine.component;

import oogasalad.engine.base.architecture.GameComponent;

import java.util.List;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.enumerate.KeyCode;
import oogasalad.engine.base.event.GameAction;

/**
 * This is the class that handle all the event and binds them to the specified keybindings
 */
public class InputHandler extends GameComponent {
    @Override
    public ComponentTag componentTag() { return ComponentTag.NONE; }

    private List<GameAction> actions;

    /**
     * Register the game action to the specific keyCode
     * @param input the input keycode
     * @param action the action that binds to that key
     */
    public void registerAction(KeyCode input, GameAction action) {
        getParent().getScene().getInputMapping().addMapping(input, action);
    }

    /**
     * Add the action to this game object
     * @param actionClass the specified game action class
     */
    public <T extends GameAction> void addAction(Class<T> actionClass) {
        try {
            actions.add(actionClass.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            throw new RuntimeException("Failed to add action", e);
        }
    }

    /**
     * Get the game action based on the input class
     * @param actionClass the specified game action class
     * @return the subscribed action
     */
    public <T extends GameAction> T getAction(Class<T> actionClass) {
        for (GameAction action : actions) {
            if (action.getClass().equals(actionClass)) {
                return (T) action;
            }
        }
        throw new IllegalArgumentException("Action does not exist");
    }

    /**
     * Remove the game action based on the specified input class
     * @param actionClass the specified game action class
     */
    public <T extends GameAction> void removeAction(Class<T> actionClass) {
        for (GameAction action : actions) {
            if (action.getClass().equals(actionClass)) {
                actions.remove(action);
                return;
            }
        }
        throw new IllegalArgumentException("Action does not exist");
    }

}
