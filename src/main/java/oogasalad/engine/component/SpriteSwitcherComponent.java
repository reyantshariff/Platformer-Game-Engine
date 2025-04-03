package oogasalad.engine.component;

import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.base.enumerate.ComponentTag;

import java.util.HashMap;
import java.util.Map;

public class SpriteSwitcherComponent extends GameComponent {

  private final Map<String, String> stateToImage = new HashMap<>();
  private String currentState = null;

  @Override
  public ComponentTag componentTag() {
    return ComponentTag.RENDER; // Optional: order it after transform/image logic
  }

  public void registerState(String stateName, String imagePath) {
    stateToImage.put(stateName, imagePath);
  }

  public void setState(String stateName) {
    if (!stateToImage.containsKey(stateName)) {
      System.err.println("Unknown sprite state: " + stateName);
      return;
    }

    if (!stateName.equals(currentState)) {
      currentState = stateName;
      getParent().getComponent(Transform.class)
          .setImagePath(stateToImage.get(stateName));
    }
  }

  public String getCurrentState() {
    return currentState;
  }
}
