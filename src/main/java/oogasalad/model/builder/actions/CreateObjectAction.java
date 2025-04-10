package oogasalad.model.builder.actions;

import oogasalad.model.builder.EditorAction;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.architecture.GameObject;

/**
 * Record Class that tracks when GameObjects are registered
 *@author Reyan Shariff
 */

public class CreateObjectAction implements EditorAction {
  private GameObject placedObject;
  private Game game;

  /**
   * Constructor for CreateObjectAction
   *
   * @param game - Game that the object is being placed in
   * @param obj - GameObject that is being placed
   */
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