package oogasalad.model.builder.actions;

import oogasalad.model.builder.EditorAction;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.component.Transform;

/**
 * Record Class that tracks when GameObjects are moved
 *@author Reyan Shariff
 */

public class MoveObjectAction implements EditorAction {
  private final GameObject object;
  private final double fromX, fromY;
  private final double toX, toY;

  public MoveObjectAction(GameObject object, double fromX, double fromY, double toX, double toY) {
    this.object = object;
    this.fromX = fromX;
    this.fromY = fromY;
    this.toX = toX;
    this.toY = toY;
  }

  @Override
  public void undo() {
    object.getComponent(Transform.class).setX(fromX);
    object.getComponent(Transform.class).setY(fromY);
  }

  @Override
  public void redo()
  {
    object.getComponent(Transform.class).setX(toX);
    object.getComponent(Transform.class).setY(toY);
  }
}
