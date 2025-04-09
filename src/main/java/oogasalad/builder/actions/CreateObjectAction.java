package oogasalad.builder.actions;

import oogasalad.builder.EditorAction;
import oogasalad.engine.base.architecture.Game;
import oogasalad.engine.base.architecture.GameObject;

/**
 * Record Class that tracks when GameObjects are registered
 *@author Reyan Shariff
 */

public class CreateObjectAction implements EditorAction {
  private GameObject placedObject;
  private Game game;

  public CreateObjectAction(Game game, GameObject obj) {
    this.game = game;
    this.placedObject = obj;
  }

  @Override
  public void undo() {
    game.getCurrentScene().unregisterObject(placedObject);
  }

  @Override
  public void redo() {
    game.getCurrentScene().registerObject(placedObject);
  }
}