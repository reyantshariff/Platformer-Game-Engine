package oogasalad.engine.component;

import java.util.HashSet;
import java.util.Set;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.enumerate.KeyCode;
import oogasalad.engine.base.serialization.SerializableMethod;

/**
 * Handles raw keyboard input with custom key codes.
 *
 * @author Hsuan-Kai Liao
 */
public final class InputHandler extends GameComponent {
    private final Set<Integer> currentKeys = new HashSet<>();
    private final Set<Integer> previousKeys = new HashSet<>();

    @Override
    public ComponentTag componentTag() {
        return ComponentTag.NONE;
    }

    @Override
    public void update(double deltaTime) {
        previousKeys.clear();
        previousKeys.addAll(currentKeys);
        currentKeys.clear();
        currentKeys.addAll(getParent().getScene().getGame().getCurrentInputKeys());
    }

    /**
     * Returns true if the key was just pressed this frame.
     */
    @SerializableMethod
    public boolean isKeyPressed(KeyCode keyCode) {
        return currentKeys.contains(keyCode.getValue()) && !previousKeys.contains(keyCode.getValue());
    }

    /**
     * Returns true if the key is being held down.
     */
    @SerializableMethod
    public boolean isKeyHold(KeyCode keyCode) {
        return currentKeys.contains(keyCode.getValue());
    }

    /**
     * Returns true if the key was just released this frame.
     */
    @SerializableMethod
    public boolean isKeyReleased(KeyCode keyCode) {
        return !currentKeys.contains(keyCode.getValue()) && previousKeys.contains(keyCode.getValue());
    }
}
