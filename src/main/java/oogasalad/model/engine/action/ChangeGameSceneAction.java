package oogasalad.model.engine.action;

import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.behavior.BehaviorAction;

/**
 * Changes the current GameScene within the Game.
 * - If parameter == -1, go to next level
 * - If parameter >= 0, go to level at that index
 */
public class ChangeGameSceneAction extends BehaviorAction<Integer> {

  @Override
  protected void perform(Integer levelIndex) {
    Game game = getBehavior().getController().getParent().getScene().getGame();

    if (levelIndex == -1) {
      game.goToNextLevel();
    } else if (levelIndex >= 0 && levelIndex < game.getLevelOrder().size()) {
      game.goToScene(game.getLevelOrder().get(levelIndex));
    } else {
      throw new IllegalArgumentException("Invalid level index: " + levelIndex);
    }
  }

  @Override
  protected Integer defaultParameter() {
    return -1;
  }
}
