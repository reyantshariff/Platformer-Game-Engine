package oogasalad.model.engine.action;

import oogasalad.model.engine.base.behavior.BehaviorAction;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import oogasalad.view.scene.MainViewManager;

/**
 * ChangeSceneAction is a class that extends BehaviorAction and is used to change the scene.
 *
 * @author Hsuan-Kai Liao
 */
public class ChangeSceneAction extends BehaviorAction<String> {
  @Override
  public ComponentTag ActionType() {
    return ComponentTag.TRANSFORM;
  }

  private MainViewManager mainViewManager;

  @Override
  protected void awake() {
    mainViewManager = MainViewManager.getInstance();
  }

  @Override
  protected void perform(String parameter) {
    if (parameter.equals("nextLevel")) {
      getBehavior().getController().getParent().getScene().getGame().goToNextLevel();
    } else {
      mainViewManager.switchTo(parameter);
    }
  }
}
