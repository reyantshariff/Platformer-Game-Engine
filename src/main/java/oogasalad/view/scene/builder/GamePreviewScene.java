package oogasalad.view.scene.builder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import oogasalad.model.config.GameConfig;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.parser.JsonParser;
import oogasalad.model.parser.Parser;
import oogasalad.model.parser.ParsingException;
import oogasalad.view.player.GameRunner;
import oogasalad.view.scene.MainViewManager;
import oogasalad.view.scene.ViewScene;

/**
 * Preview a level from the Builder view using this Scene. Directly load in a GameScene to be
 * displayed.
 */
public class GamePreviewScene extends ViewScene {

  private static final String PREVIEW_GAME_FILEPATH = "src/main/gameFiles/temp.json";

  private final StackPane root;
  private final Button returnButton;
  private final GameRunner gameRunner;

  /**
   * A LevelPreviewScene is similar to GamePlayerScene but is meant for previewing a
   * level-in-progress from the builder.
   */
  private GamePreviewScene() {
    super(new StackPane(), GameConfig.getNumber("windowWidth"), GameConfig.getNumber("windowHeight"));
    gameRunner = new GameRunner();

    root = (StackPane) getScene().getRoot();

    returnButton = new Button(GameConfig.getText("returnToBuilderButton"));
    returnButton.setOnAction(e -> {
      deactivate();
      MainViewManager.getInstance().switchTo("Builder");
    });

    StackPane.setAlignment(returnButton, Pos.TOP_RIGHT);
    root.getChildren().add(returnButton);
  }

  /**
   * Play the preview to start the game.
   */
  public void preview() {
    root.getChildren().clear();
    root.getChildren().add(returnButton);

    try {
      Parser<?> parser = new JsonParser(PREVIEW_GAME_FILEPATH);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode newNode = mapper.createObjectNode();
      gameRunner.setGame((Game) parser.parse(newNode));
      gameRunner.play();
    } catch (ParsingException e) {
      throw new IllegalStateException(GameConfig.getText("failureToParse", e.getMessage()), e);
    }

    root.getChildren().addFirst(gameRunner.getCanvas());
    gameRunner.getCanvas().requestFocus();
  }

  /**
   * Pause the game preview.
   */
  public void pause() {
    gameRunner.pause();
  }

  /**
   * Resume the game preview.
   */
  public void resume() {
    gameRunner.play();
  }

  /**
   * Deactivates the game engine for this scene.
   */
  @Override
  public void deactivate() {
    gameRunner.pause();
  }
}
