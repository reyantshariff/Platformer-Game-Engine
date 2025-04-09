package oogasalad.model.engine.action;

import oogasalad.model.engine.base.behavior.BehaviorAction;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import oogasalad.model.engine.component.Transform;

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

  private Transform transform;

  @Override
  protected void awake() {
    transform = getComponent(Transform.class);
  }

  @Override
  protected void perform(String parameter) {
    transform.changeScene(parameter);
  }
}
