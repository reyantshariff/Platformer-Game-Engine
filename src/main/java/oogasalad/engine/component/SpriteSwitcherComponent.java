package oogasalad.engine.component;

import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.enumerate.ComponentTag;

import java.util.HashMap;
import java.util.Map;
import oogasalad.engine.base.serialization.SerializableField;

/**
 * A rendering component that allows switching the image of a GameObject
 * based on a logical state. Useful for character animation, button state changes,
 * or visual feedback tied to game state.
 */
public class SpriteSwitcherComponent extends GameComponent {

  /** A mapping from state names to image file paths. */
  private final Map<String, String> stateToImage = new HashMap<>();

  /** The currently active visual state. */
  @SerializableField
  private String currentState = null;

  /**
   * Returns the component tag identifying this as a render-related component.
   * This determines update ordering relative to other components.
   *
   * @return {@link ComponentTag#RENDER}
   */
  @Override
  public ComponentTag componentTag() {
    return ComponentTag.RENDER; // Optional: order it after transform/image logic
  }

  /**
   * Registers a new visual state and its corresponding image path.
   *
   * @param stateName the name of the logical state
   * @param imagePath the path to the image to render when in this state
   */
  public void registerState(String stateName, String imagePath) {
    stateToImage.put(stateName, imagePath);
  }

  /**
   * Sets the current state of the object. If the state changes and has a registered image,
   * updates the image path on the GameObject's {@link Transform} component.
   *
   * @param stateName the new state to switch to
   */
  public void setState(String stateName) {
    if (!stateToImage.containsKey(stateName)) {
      return;
    }

    if (!stateName.equals(currentState)) {
      currentState = stateName;
      getParent().getComponent(Transform.class)
          .setImagePath(stateToImage.get(stateName));
    }
  }

  /**
   * Returns the current visual state of the component.
   *
   * @return the active state name
   */
  public String getState() {
    return currentState;
  }

  /**
   * Returns the image path of the requested state
   *
   * @param stateName name of state as String, used as key for stateToImage map
   * @return image-path String
   */
  public String getImagePath(String stateName) {
    String imagePath = stateToImage.get(stateName);
    if (imagePath == null || imagePath.isEmpty()) {
      throw new IllegalArgumentException("State " + stateName + " has not been registered for this component.");
    }
    return imagePath;
  }
}