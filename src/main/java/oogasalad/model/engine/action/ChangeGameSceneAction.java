package oogasalad.model.engine.action;

import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.behavior.BehaviorAction;

/**
 * Changes the current GameScene within the Game. - If parameter == -2, go to last level - If
 * parameter == -1, go to next level - If parameter >= 0, go to level at that index
 */
public class ChangeGameSceneAction extends BehaviorAction<Integer> {

  private static final int LAST_LEVEL = -2;
  private static final int NEXT_LEVEL = -1;

  @Override
  protected void perform(Integer levelIndex) {
    Game game = getBehavior().getController().getParent().getScene().getGame();

    if (levelIndex == LAST_LEVEL) {
      goToLastLevel(game);
    } else if (levelIndex == NEXT_LEVEL) {
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
    return NEXT_LEVEL;
  }
}
