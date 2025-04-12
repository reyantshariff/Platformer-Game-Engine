package oogasalad.model.builder.actions;

import oogasalad.model.builder.EditorAction;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.component.Transform;

public class ResizeObjectAction implements EditorAction {
  private final GameObject object;
  private final double fromX, fromY;
  private final double toX, toY;
  private final double prevWidth, currWidth;
  private final double prevHeight, currHeight;

  public ResizeObjectAction(GameObject object, double fromX, double fromY, double prevWidth, double prevHeight, double toX, double toY, double currWidth, double currHeight) {
    this.object = object;
    this.fromX = fromX;
    this.fromY = fromY;
    this.toX = toX;
    this.toY = toY;
    this.prevWidth = prevWidth;
    this.prevHeight = prevHeight;
    this.currWidth = currWidth;
    this.currHeight = currHeight;
  }

  @Override
  public void undo() {
    object.getComponent(Transform.class).setX(fromX);
    object.getComponent(Transform.class).setY(fromY);
    object.getComponent(Transform.class).setScaleX(prevWidth);
    object.getComponent(Transform.class).setScaleY(prevHeight);
  }

  @Override
  public void redo()
  {
    object.getComponent(Transform.class).setX(toX);
    object.getComponent(Transform.class).setY(toY);
    object.getComponent(Transform.class).setScaleX(currWidth);
    object.getComponent(Transform.class).setScaleY(currHeight);
  }
}
