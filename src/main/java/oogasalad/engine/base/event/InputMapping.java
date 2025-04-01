package oogasalad.engine.base.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import oogasalad.engine.base.enumerate.KeyCode;

/**
 * This is the mapping that stores all the bindings of key and actions. The actions should be bound
 * to a specific key in order to make it controllable. All actions bound with the pressed key will
 * be called dispatch() if their constraints are met.
 *
 * @author Hsuan-Kai Liao, Christian Bepler
 */
public class InputMapping {
    private final Map<KeyCode, List<GameAction>> inputActionMap;

    public InputMapping() {
        inputActionMap = new HashMap<>();
    }

    /**
     * Associates an action to an input.
     *
     * @param input the input to map
     * @param action the action to associate with the input
     */
    public void addMapping(KeyCode input, GameAction action) {
        inputActionMap.computeIfAbsent(input, k -> new java.util.ArrayList<>()).add(action);
    }

    /**
     * Get the current unmodifiable key-action mappings
     */
    public Map<KeyCode, List<GameAction>> getMapping() {
        return inputActionMap.entrySet().stream()
            .collect(Collectors.toUnmodifiableMap(
                Map.Entry::getKey,
                e -> List.copyOf(e.getValue())
            ));
    }

    /**
     * Remove the action from the input mapping
     * NOTE: assumption is made that each action will only have one key binding
     *
     * @param action the action to be removed
     */
    public void removeMapping(GameAction action) {
        for (List<GameAction> actions : inputActionMap.values()) {
            for (int i = 0; i < actions.size(); i++) {
                if (actions.get(i) == action) {
                    actions.remove(i);
                    return;
                }
            }
        }
    }

    /**
     * Triggers the dispatch method on all actions associated with the input.
     *
     * @param input the input to trigger
     */
    public void trigger(KeyCode input) {
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
