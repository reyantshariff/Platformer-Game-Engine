package oogasalad.engine.base.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oogasalad.engine.base.enumerate.KeyCode;

public class InputMapping {
    private final Map<KeyCode, List<GameAction>> inputActionMap;

    public InputMapping() {
        inputActionMap = new HashMap<>();
    }

    /**
     * Associates an action to an input.
     *
     * @param input the input to map
     * @param action the list of actions to associate with the input
     */
    public final void addMapping(KeyCode input, GameAction action) {
        inputActionMap.computeIfAbsent(input, k -> new java.util.ArrayList<>()).add(action);
    }

    /**
     * Triggers the dispatch method on all actions associated with the input.
     *
     * @param input the input to trigger
     */
    public final void trigger(KeyCode input) {
        List<GameAction> actions = inputActionMap.get(input);
        if (actions != null) {
            for (GameAction action : actions) {
                if (action.checkConstraints()) {
                    action.dispatch();
                }
            }
        }
    }
}
