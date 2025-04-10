package oogasalad.model.builder.actions;

import oogasalad.model.builder.EditorAction;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.architecture.GameObject;

/**
 * Record Class that tracks when GameObjects are unregistered
 *@author Reyan Shariff
 */

public class DeleteObjectAction implements EditorAction {
  private final Game game;
  private final GameObject object;

  /**
   * Constructor for DeleteObjectAction
   *
   * @param game - Game that the object is being placed in
   * @param object - GameObject that is being placed
   */
  public DeleteObjectAction(Game game, GameObject object) {
    this.game = game;
    this.object = object;
  }

  @Override
  public void undo() {
    game.getCurrentScene().registerObject(object);
  }

  @Override
  public void redo()
  {
    game.getCurrentScene().unregisterObject(object);
  }
}

