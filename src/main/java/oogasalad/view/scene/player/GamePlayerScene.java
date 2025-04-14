package oogasalad.view.scene.player;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import oogasalad.view.scene.MainViewManager;
import oogasalad.view.scene.ViewScene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.parser.JsonParser;
import oogasalad.model.parser.Parser;
import oogasalad.model.parser.ParsingException;
import oogasalad.view.player.GameRunner;

/**
 * Displays the game using a GUI canvas inside a JavaFX scene
 */
public class GamePlayerScene extends ViewScene {
  private final GameRunner gameRunner;
  private static final Logger logger = LogManager.getLogger(GamePlayerScene.class);

  /**
   * Constructs a new GamePlayerScene to display a game within a JavaFX scene.
   * @param gameFilepath The filepath to the game JSON file to be loaded.
   */
  public GamePlayerScene(String gameFilepath) {
    super(new StackPane(), 1280, 720);

    StackPane root = (StackPane) getScene().getRoot();
    Game game;

    try {
      Parser<?> parser = new JsonParser(gameFilepath);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode newNode = mapper.createObjectNode();
      game = (Game) parser.parse(newNode);
      logger.debug(game.getAllScenes().values().stream().findFirst().getClass());
    } catch (ParsingException e) {
      throw new IllegalStateException("Failed to parse game JSON file: " + e.getMessage(), e);
    }

    game.goToScene(game.getLevelOrder().getFirst());

    gameRunner = new GameRunner(game);

    Button returnButton = new Button("MAIN MENU");
    returnButton.setId("returnButton");
    returnButton.setOnAction(e -> {
      deactivate();
      MainViewManager.getInstance().switchToMainMenu();
    });
    StackPane.setAlignment(returnButton, Pos.TOP_RIGHT);

    root.getChildren().addAll(gameRunner.getCanvas(), returnButton);
    gameRunner.getCanvas().requestFocus();
  }

  /**
   * Deactivates the game engine for this scene.
   */
  @Override
  public void deactivate() {
    gameRunner.stop();
  }
}