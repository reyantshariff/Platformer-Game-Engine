package oogasalad.engine.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputMapping {
    private Map<String, List<GameAction>> inputActionMap;

    public InputMapping() {
        inputActionMap = new HashMap<>();
    }

    /**
     * Associates an action to an input.
     *
     * @param input the input to map
     * @param actions the list of actions to associate with the input
     */
    public void addMapping(String input, GameAction action) {
        inputActionMap.computeIfAbsent(input, k -> new java.util.ArrayList<>()).add(action);
    }

    /**
     * Triggers the dispatch method on all actions associated with the input.
     *
     * @param input the input to trigger
     */
    public void trigger(String input) {
        List<GameAction> actions = inputActionMap.get(input);
        if (actions != null) {
            for (GameAction action : actions) {
                action.dispatch();
            }
        }
    }
}
