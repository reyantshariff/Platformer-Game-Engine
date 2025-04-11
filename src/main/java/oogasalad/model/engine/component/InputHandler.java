package oogasalad.model.engine.component;

import java.util.HashSet;
import java.util.Set;
import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import oogasalad.model.engine.base.enumerate.KeyCode;

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
    return ComponentTag.INPUT;
  }

  @Override
  public void update(double deltaTime) {
    previousKeys.clear();
    previousKeys.addAll(currentKeys);
    currentKeys.clear();
    currentKeys.addAll(getParent().getScene().getGame().getCurrentInputKeys());
  }

  /**
   * @return - Current keys being pressed
   */
  public Set<Integer> getPressedKeys() {
    return currentKeys;
  }

  /**
   * Method for testing functinoality of inputHandler without having to set up
   * an entire game
   *
   * @param pressedKeys - Keys to be pressed
   */
  void setPressedKeys(Set<Integer> pressedKeys) {
    this.currentKeys.clear();
    this.currentKeys.addAll(pressedKeys);
  }

  /**
   * Returns true if the key was just pressed this frame.
   */
  public boolean isKeyPressed(KeyCode keyCode) {
    if (keyCode == null) return false;
    return currentKeys.contains(keyCode.getValue()) && !previousKeys.contains(keyCode.getValue());
  }

  /**
   * Returns true if the key is being held down.
   */
  public boolean isKeyHold(KeyCode keyCode) {
    if (keyCode == null) return false;
    return currentKeys.contains(keyCode.getValue());
  }

  /**
   * Returns true if the key was just released this frame.
   */
  public boolean isKeyReleased(KeyCode keyCode) {
    if (keyCode == null) return false;
    return !currentKeys.contains(keyCode.getValue()) && previousKeys.contains(keyCode.getValue());
  }

  /**
   * Method to clear all previous and current keys (inputs)
   */
  public void resetInputState() {
    currentKeys.clear();
    previousKeys.clear();
  }
}
