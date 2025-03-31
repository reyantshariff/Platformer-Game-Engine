package oogasalad.engine.gameComponent;

import java.util.List;
import java.util.ArrayList;
import oogasalad.engine.base.ComponentTag;
import oogasalad.engine.base.GameComponent;
import oogasalad.engine.base.GameAction;


public class PlayerController extends GameComponent {
    public static final ComponentTag TAG = ComponentTag.NONE;

    private List<GameAction> actions;

    public PlayerController() {
        super(TAG);
        actions = new ArrayList<>();
    }

    @Override
    public void start() {
    }

    public <T extends GameAction> void addAction(Class<T> actionClass) {
        try {
            actions.add(actionClass.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            throw new RuntimeException("Failed to add action", e);
        }
    }

    public <T extends GameAction> T getAction(Class<T> actionClass) {
        for (GameAction action : actions) {
            if (action.getClass().equals(actionClass)) {
                return (T) action;
            }
        }
        throw new IllegalArgumentException("Action does not exist");
    }

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
