package oogasalad.model.builder.actions;

import oogasalad.model.builder.EditorAction;
import oogasalad.model.builder.TransformState;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.component.Transform;

/**
 * Class that tracks when GameObjects are resized
 *@author Reyan Shariff
 */

public class ResizeObjectAction implements EditorAction {
/**
 * Class constructor
 * @param object - GameObject
 * @param fromState - Transform State pre resizing
 * @param toState - Transform State post resizing
 * */

  private final GameObject object;
  private final TransformState fromState;
  private final TransformState toState;

  public ResizeObjectAction(GameObject object, TransformState fromState, TransformState toState) {
    this.object = object;
    this.fromState = fromState;
    this.toState = toState;
  }

  @Override
  public void undo() {
    Transform t = object.getComponent(Transform.class);
    t.setX(fromState.x());
    t.setY(fromState.y());
    t.setScaleX(fromState.width());
    t.setScaleY(fromState.height());
  }

  @Override
  public void redo() {
    Transform t = object.getComponent(Transform.class);
    t.setX(toState.x());
    t.setY(toState.y());
    t.setScaleX(toState.width());
    t.setScaleY(toState.height());
  }
}