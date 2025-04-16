package oogasalad.model.engine.action;

import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.behavior.BehaviorAction;

/**
 * Changes the current GameScene within the Game.
 * - If parameter == -2, go to last level
 * - If parameter == -1, go to next level
 * - If parameter >= 0, go to level at that index
 */
public class ChangeGameSceneAction extends BehaviorAction<Integer> {

  @Override
  protected void perform(Integer levelIndex) {
    Game game = getBehavior().getController().getParent().getScene().getGame();

    if (levelIndex == -2) {
      goToLastLevel(game);
    } else if (levelIndex == -1) {
      goToNextLevel(game);
    } else if (isValidLevelIndex(levelIndex, game)) {
      goToSpecificLevel(levelIndex, game);
    } else {
      throw new IllegalArgumentException("Invalid level index: " + levelIndex);
    }
  }

  private void goToLastLevel(Game game) {
    game.goToScene(game.getLevelOrder().getLast());
  }

  private void goToNextLevel(Game game) {
    game.goToNextLevel();
  }

  private void goToSpecificLevel(int levelIndex, Game game) {
    game.goToScene(game.getLevelOrder().get(levelIndex));
  }

  private boolean isValidLevelIndex(int levelIndex, Game game) {
    return levelIndex >= 0 && levelIndex < game.getLevelOrder().size();
  }

  @Override
  protected Integer defaultParameter() {
    return -1;
  }
}
