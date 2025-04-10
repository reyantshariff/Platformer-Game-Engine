package oogasalad.view.scene;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.parser.GameParser;
import oogasalad.model.parser.ParsingException;
import oogasalad.view.gui.Gui;
import oogasalad.view.player.dinosaur.DinosaurGameScene;

/**
 * Displays the game using a GUI canvas inside a JavaFX scene
 */
public class GamePlayerScene extends ViewScene {
  private final GameParser gameParser = new GameParser();
  /**
   * Constructs a new GamePlayerScene to display a game within a JavaFX scene.
   *
   * @param manager   The MainViewManager used to switch scenes.
   * @param gameName  The name of the game to be displayed.
   */
  public GamePlayerScene(MainViewManager manager, String gameName) {
    super(new StackPane(), 1280, 720);

    StackPane root = (StackPane) getScene().getRoot();


    // Parse the game JSON into a Game object
    String jsonPath = "doc/plan/data/FullDino.json";
    Game game;
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode rootNode = mapper.readTree(new File(jsonPath));
      GameParser parser = new GameParser();
      game = parser.parse(rootNode);


    } catch (IOException | ParsingException e) {
      throw new RuntimeException("Failed to parse game JSON file: " + e.getMessage(), e);
    }

    // Automatically activate the first available scene
    GameScene firstScene = game.getAllScenes().values().stream()
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No scenes found in the parsed game."));
    game.changeScene(firstScene.getName());

    // Display the game using Gui
    Gui gui = new Gui(game);

    /*
    GameScene scene = switch (gameName) {
      case "Dino Game" -> new DinosaurGameScene("Dino");
      default -> new DinosaurGameScene("Default");
    };


    game.addScene(scene);
    game.changeScene(scene.getName());
    */

    Button returnButton = new Button("Main Menu");
    returnButton.setOnAction(e -> {
      gui.stop();
      manager.switchToMainMenu();
    });

    StackPane.setAlignment(returnButton, Pos.TOP_RIGHT);
    returnButton.setStyle("-fx-background-color: white; -fx-font-weight: bold;");

    root.getChildren().addAll(gui.getCanvas(), returnButton);
  }
}