package oogasalad.engine.component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import oogasalad.engine.base.architecture.GameComponent;

import java.util.List;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.enumerate.KeyCode;
import oogasalad.engine.base.event.GameAction;

/**
 * This is the class that handle all the event and binds them to the specified keybindings. This
 * component is strongly related to the actions class which you should override the specific
 * dispatch() method and register the action with a binding key to the gameObject through this
 * component.
 *
 * @author Hsuan-Kai Liao, Christian Bepler
 */
public class InputHandler extends GameComponent {
    @Override
    public ComponentTag componentTag() { return ComponentTag.INPUT; }

    private List<GameAction> actions = new ArrayList<>();

    @Override
    public void onRemove() {
        for (GameAction action : actions) {
            getParent().getScene().getInputMapping().removeMapping(action);
        }
        actions.clear();
    }

    /**
     * Register the game action to the specific keyCode based on the actionClass
     * @param input the input keycode
     * @param actionClass the specified game action class
     */
    public <T extends GameAction> void registerAction(KeyCode input, Class<T> actionClass) {
        try {
            T action = actionClass.getDeclaredConstructor().newInstance();

            // Set the parent of the action
            Field parentField = GameAction.class.getDeclaredField("parent");
            parentField.setAccessible(true);
            parentField.set(action, this);

            actions.add(action);
            getParent().getScene().getInputMapping().addMapping(input, action);
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
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i).getClass().equals(actionClass)) {
                getParent().getScene().getInputMapping().removeMapping(actions.get(i));
                actions.remove(i);
                return;
            }
        }
        throw new IllegalArgumentException("Action does not exist");
    }

}
