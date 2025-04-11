package oogasalad.view.scene;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.parser.JsonParser;
import oogasalad.model.parser.Parser;
import oogasalad.model.parser.ParsingException;
import oogasalad.view.gui.Gui;

/**
 * Displays the game using a GUI canvas inside a JavaFX scene
 */
public class GamePlayerScene extends ViewScene {
  private static final String JSON_PATH_PREFIX = "data/GameJsons/";
  /**
   * Constructs a new GamePlayerScene to display a game within a JavaFX scene.
   *
   * @param manager   The MainViewManager used to switch scenes.
   * @param gameName  The name of the game to be displayed.
   */
  public GamePlayerScene(MainViewManager manager, String gameName) {
    super(new StackPane(), 1280, 720);

    StackPane root = (StackPane) getScene().getRoot();

    String correctGameName = gameName.replaceAll("\\s+","");


    // Parse the game JSON into a Game object
    String jsonPath = JSON_PATH_PREFIX + correctGameName+ ".json";
    Game game;
    try {
      Parser<?> parser = new JsonParser(jsonPath);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode newNode = mapper.createObjectNode();
      game = (Game) parser.parse(newNode);

    } catch (ParsingException e) {
      throw new IllegalStateException("Failed to parse game JSON file: " + e.getMessage(), e);
    }

    // Automatically activate the first available scene
    GameScene firstScene = game.getAllScenes().values().stream()
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No scenes found in the parsed game."));
    game.changeScene(firstScene.getName());

    Gui gui = new Gui(game);

    Button returnButton = new Button("Main Menu");
    returnButton.setOnAction(e -> {
      gui.stop();
      manager.switchToMainMenu();
    });

    StackPane.setAlignment(returnButton, Pos.TOP_RIGHT);
    returnButton.setStyle("-fx-background-color: white; -fx-font-weight: bold;");

    root.getChildren().addAll(gui.getCanvas(), returnButton);
    gui.getCanvas().requestFocus();
  }
}