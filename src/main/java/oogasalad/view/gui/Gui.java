package oogasalad.view.gui;

import javafx.scene.Group;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.view.player.dinosaur.DinosaurGameScene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import oogasalad.model.ResourceBundles;
import oogasalad.model.engine.base.enumerate.KeyCode;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import oogasalad.model.engine.base.architecture.Game;

/**
 * The GUI class manages the graphical user interface for the OOGASalad game engine. It handles the
 * creation of the game window, rendering game objects, and processing user input.
 *
 * @author Jack F. Regan and Logan Dracos
 */
public class Gui {

  private static final Logger logger = LogManager.getLogger(Gui.class);
  private final Game game;
  private final Stage curStage;
  private GraphicsContext gc;
  private Timeline gameLoop;
  private GameObjectRenderer objectRenderer;

  /**
   * Constructs a new GUI instance for the given game and stage.
   *
   * @param stage The primary stage for the application.
   * @param game  The game instance to be displayed.
   */
  public Gui(Stage stage, Game game) {
    this.game = game;
    this.curStage = stage;
    ResourceBundles.loadBundle("oogasalad.gui.general");
    generateGui(stage);
  }

  /**
   * Method to change the current scene you are on to a new one
   *
   * @param newScene - the new GameScene to push to
   */
  public void switchToScene(GameScene newScene) {
    game.changeScene(newScene.getName());
    Scene fxScene = buildJavaFXScene(newScene);
    curStage.setScene(fxScene);
  }

  /**
   * Generates the graphical user interface for the game.
   *
   * @param stage The primary stage for the application.
   */
  private void generateGui(Stage stage) {
    logger.debug("Generating GUI...");

    DinosaurGameScene dinoScene = new DinosaurGameScene("DinoScene");
    game.addScene(dinoScene);
    game.changeScene(dinoScene.getName());

    Scene newScene = buildJavaFXScene(dinoScene);
    stage.setTitle("OOGASalad Platformer");
    stage.setScene(newScene);
    stage.show();

    startGameLoop();
    logger.debug("GUI generated.");
  }
  /**
   * Starts the game loop, which updates and renders the game at a fixed frame rate.
   */
  private void startGameLoop() {
    if (gameLoop == null) { // Only create Timeline once
      gameLoop = new Timeline();
      gameLoop.setCycleCount(Timeline.INDEFINITE);
      gameLoop.getKeyFrames().add(new KeyFrame(
          Duration.seconds(
              1.0 / ResourceBundles.getDouble("oogasalad.gui.general", "framesPerSecond")),
          event -> step() // Call step method
      ));
      gameLoop.play();
    }
  }

  /**
   * Executes a single step of the game loop, updating the game state and rendering the scene.
   */
  private void step() {
    if (game.getCurrentScene() != null) { // Check if scene is loaded
      game.step(1.0 / ResourceBundles.getDouble("oogasalad.gui.general", "framesPerSecond"));
      objectRenderer.render(gc, game.getCurrentScene());
    } else {
      logger.debug("No game scene loaded. Skipping step.");
    }
  }

  /**
   * Maps a JavaFX KeyCode to an engine KeyCode.
   *
   * @param code The JavaFX KeyCode.
   * @return The engine KeyCode, or null if the mapping fails.
   */
  private KeyCode mapToEngineKeyCode(javafx.scene.input.KeyCode code) {
    try {
      return KeyCode.valueOf(code.name());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }


  private Scene buildJavaFXScene(GameScene sceneToRender) {
    Group root = new Group();
    Scene fxScene = new Scene(root,
        ResourceBundles.getInt("oogasalad.gui.general", "windowWidth"),
        ResourceBundles.getInt("oogasalad.gui.general", "windowHeight"));
    Canvas canvas = new Canvas(
        ResourceBundles.getInt("oogasalad.gui.general", "windowWidth"),
        ResourceBundles.getInt("oogasalad.gui.general", "windowHeight"));

    gc = canvas.getGraphicsContext2D();
    root.getChildren().add(canvas);

    fxScene.getStylesheets().add(getClass()
        .getResource(ResourceBundles.getString("oogasalad.gui.general", "stylesheet"))
        .toExternalForm());

    fxScene.setOnKeyPressed(e -> {
      KeyCode key = mapToEngineKeyCode(e.getCode());
      if (key != null) game.keyPressed(key.getValue());
    });

    fxScene.setOnKeyReleased(e -> {
      KeyCode key = mapToEngineKeyCode(e.getCode());
      if (key != null) game.keyReleased(key.getValue());
    });

    objectRenderer = new GameObjectRenderer(fxScene);
    return fxScene;
  }
}