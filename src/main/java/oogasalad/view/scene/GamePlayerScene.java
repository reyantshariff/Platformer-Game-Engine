package oogasalad.view.scene;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.parser.JsonParser;
import oogasalad.model.parser.Parser;
import oogasalad.model.parser.ParsingException;
import oogasalad.view.gui.Gui;

/**
 * Displays the game using a GUI canvas inside a JavaFX scene
 */
public class GamePlayerScene extends ViewScene {
  private final Gui gui;
  private static final String JSON_PATH_PREFIX = "data/GameJsons/";
  private static final Logger logger = LogManager.getLogger(GamePlayerScene.class);

  /**
   * Constructs a new GamePlayerScene to display a game within a JavaFX scene.
   *
   * @param manager  The MainViewManager used to switch scenes.
   * @param gameName The name of the game to be displayed.
   */
  public GamePlayerScene(MainViewManager manager, String gameName, String gameType) {
    super(new StackPane(), 1280, 720);

    StackPane root = (StackPane) getScene().getRoot();

    String correctedGamePath = JSON_PATH_PREFIX + gameType + "/";
    String correctGameName = gameName.replaceAll("\\s+","");


    // Parse the game JSON into a Game object
    String jsonPath = correctedGamePath + correctGameName + ".json";
    Game game;
    try {
      Parser<?> parser = new JsonParser(jsonPath);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode newNode = mapper.createObjectNode();
      game = (Game) parser.parse(newNode);
      logger.debug(game.getAllScenes().values().stream().findFirst().getClass());


    } catch (ParsingException e) {
      throw new IllegalStateException("Failed to parse game JSON file: " + e.getMessage(), e);
    }

    game.goToScene(game.getLevelOrder().getFirst());

    gui = new Gui(game);

    Button returnButton = new Button("MAIN MENU");
    returnButton.setId("returnButton");
    returnButton.setOnAction(e -> {
      deactivate();
      manager.switchToMainMenu();
    });
    StackPane.setAlignment(returnButton, Pos.TOP_RIGHT);

    root.getChildren().addAll(gui.getCanvas(), returnButton);
    gui.getCanvas().requestFocus();
  }

  /**
   * Deactivates the game engine for this scene.
   */
  @Override
  public void deactivate() {
    gui.stop();
  }
}