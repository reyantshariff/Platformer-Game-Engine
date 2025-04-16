package oogasalad.view.scene.player;

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
public class GamePlayerScene extends GameDisplayScene {

  /**
   * A LevelPreviewScene is similar to GamePlayerScene but is meant for previewing a
   * level-in-progress from the builder.
   */
  private GamePlayerScene() {
  }

  /**
   * Play the preview to start the game.
   */
  public void play(String filepath) {
    getRoot().getChildren().clear();
    getRoot().getChildren().add(getReturnButton());

    try {
      Parser<?> parser = new JsonParser(filepath);
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
}
