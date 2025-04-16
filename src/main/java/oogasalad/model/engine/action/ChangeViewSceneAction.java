package oogasalad.model.engine.action;

import oogasalad.model.engine.base.behavior.BehaviorAction;
import oogasalad.view.scene.MainViewManager;

/**
 * ChangeViewSceneAction is a class that extends BehaviorAction and is used to change the scene.
 *
 * @author Hsuan-Kai Liao
 */
public class ChangeViewSceneAction extends BehaviorAction<String> {
  private MainViewManager mainViewManager;

  @Override
  protected String defaultParameter() {
    return "";
  }

  @Override
  protected void awake() {
    mainViewManager = MainViewManager.getInstance();
  }

  @Override
  protected void perform(String parameter) {
    mainViewManager.switchTo(parameter);
  }
}
