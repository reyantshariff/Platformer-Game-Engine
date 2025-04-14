package oogasalad.view.scene.builder;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.parser.JsonParser;
import oogasalad.model.parser.ParsingException;
import oogasalad.view.player.GameRunner;
import oogasalad.view.scene.MainViewManager;
import oogasalad.view.scene.ViewScene;

/**
 * Preview a level from the Builder view using this Scene. Directly load in a GameScene to be
 * displayed.
 */
public class LevelPreviewScene extends ViewScene {

  // For displaying the running game preview
  private final GameRunner gameRunner;

  /**
   * A LevelPreviewScene is similar to GamePlayerScene but is meant for previewing a
   * level-in-progress from the builder.
   *
   * @param builderScene the BuilderScene that created this LevelPreviewScene
   * @param gameNode     the Game object being previewed, stored as a JSON node according to
   *                     JsonParser
   */
  public LevelPreviewScene(BuilderScene builderScene, JsonNode gameNode) {
    super(new StackPane(), 1280, 720);

    StackPane root = (StackPane) getScene().getRoot();

    JsonParser jsonParser = new JsonParser("src/main/gameFiles/temp.json");
    Game game;
    try {
      game = jsonParser.parse(gameNode);
    } catch (ParsingException e) {
      throw new IllegalStateException("Failed to parse game JSON file: " + e.getMessage(), e);
    }

    gameRunner = new GameRunner(game);

    Button returnButton = new Button("Return to Builder");
    returnButton.setOnAction(e -> {
      deactivate();
      MainViewManager.getInstance().switchTo("BuilderScene"); // return to the builder scene
    });

    StackPane.setAlignment(returnButton, Pos.TOP_RIGHT);
    returnButton.setStyle("-fx-background-color: white; -fx-font-weight: bold;");

    root.getChildren().addAll(gameRunner.getCanvas(), returnButton);
  }

  /**
   * Deactivates the game engine for this scene.
   */
  @Override
  public void deactivate() {
    gameRunner.stop();
  }
}
