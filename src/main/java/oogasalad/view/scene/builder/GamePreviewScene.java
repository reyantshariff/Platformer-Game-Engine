package oogasalad.view.scene.builder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import oogasalad.model.config.GameConfig;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.parser.JsonParser;
import oogasalad.model.parser.Parser;
import oogasalad.model.parser.ParsingException;
import oogasalad.view.scene.GameDisplayScene;

/**
 * Preview a level from the Builder view using this Scene. Directly load in a GameScene to be
 * displayed.
 */
public class GamePreviewScene extends GameDisplayScene {

  private static final String PREVIEW_GAME_FILEPATH = "src/main/gameFiles/temp.json";

  /**
   * A LevelPreviewScene is similar to GamePlayerScene but is meant for previewing a
   * level-in-progress from the builder.
   */
  private GamePreviewScene() {

  }

  /**
   * Play the preview to start the game.
   */
  public void preview() {
    getRoot().getChildren().clear();
    getRoot().getChildren().add(getReturnButton());

    try {
      Parser<?> parser = new JsonParser(PREVIEW_GAME_FILEPATH);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode newNode = mapper.createObjectNode();
      getGameRunner().setGame((Game) parser.parse(newNode));
      getGameRunner().play();
    } catch (ParsingException e) {
      throw new IllegalStateException(GameConfig.getText("failureToParse", e.getMessage()), e);
    }

    getRoot().getChildren().addFirst(getGameRunner().getCanvas());
    getGameRunner().getCanvas().requestFocus();
  }

  /**
   * Pause the game preview.
   */
  public void pause() {
    getGameRunner().pause();
  }

  /**
   * Resume the game preview.
   */
  public void resume() {
    getGameRunner().play();
  }


}
