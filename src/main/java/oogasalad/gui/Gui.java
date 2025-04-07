package oogasalad.gui;

import javafx.scene.Group;
import javafx.scene.image.Image;
import oogasalad.engine.component.ImageComponent;
import oogasalad.player.dinosaur.MainMenuGameScene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import oogasalad.ResourceBundles;
import oogasalad.engine.base.enumerate.KeyCode;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import oogasalad.engine.base.architecture.Game;
import oogasalad.engine.base.architecture.GameScene;
import oogasalad.engine.component.Transform;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.player.dinosaur.DinosaurGameScene;

/**
 * Our main GUI class that handles the game, graphics context, timeline, and scene.
 * We use a resource bundle to modularize literals, and we have a logger implemented.
 */
public class GUI {
  private static final Logger logger = LogManager.getLogger(GUI.class);

  private static final String GUI_BUNDLE = "oogasalad.gui.general";
  private static final String WINDOW_WIDTH_KEY = "windowWidth";
  private static final String WINDOW_HEIGHT_KEY = "windowHeight";

  private Game game;
  private GraphicsContext gc;
  private Timeline gameLoop;
  private Scene scene;

  /**
   * Constructor for our GUI class
   *
   * @param stage - Stage for GUI to render
   * @param game - Game to be able to render objects
   */
  public GUI(Stage stage, Game game) {
    this.game = game;
    ResourceBundles.loadBundle("GUI_BUNDLE");
    generateGUI(stage);
  }

  private void generateGUI(Stage stage) {
    logger.debug("Generating GUI...");

    Group root = new Group();
    scene = new Scene(root, ResourceBundles.getInt("GUI_BUNDLE", "WINDOW_WIDTH_KEY"),
        ResourceBundles.getInt("GUI_BUNDLE", "WINDOW_HEIGHT_KEY"));
    Canvas canvas = new Canvas(ResourceBundles.getInt("GUI_BUNDLE", "WINDOW_WIDTH_KEY"),
        ResourceBundles.getInt("GUI_BUNDLE", "WINDOW_HEIGHT_KEY"));
    gc = canvas.getGraphicsContext2D();
    root.getChildren().add(canvas);

    // Apply css styling
    // scene.getStylesheets().add(getClass().getResource("/oogasalad/gui/style.css").toExternalForm());

    //game.addScene(MainMenuGameScene.class, "Mainmenu");
    game.addScene(DinosaurGameScene.class, "Dinosaur");
    startGameLoop();

    stage.setTitle("OOGASalad Platformer");
    stage.setScene(scene);
    stage.show();

    scene.setOnKeyPressed(e -> {
      KeyCode key = mapToEngineKeyCode(e.getCode());
      if (key != null && game.getCurrentScene() != null) {
        game.getCurrentScene().subscribeInputKey(key);
      } else if (game.getCurrentScene() == null) {
        logger.error("Current Game Scene is null");
      }
    });

    logger.debug("GUI generated.");
  }

  private void startGameLoop() {
    if (gameLoop == null) { // Only create Timeline once
      gameLoop = new Timeline();
      gameLoop.setCycleCount(Timeline.INDEFINITE);
      gameLoop.getKeyFrames().add(new KeyFrame(
          Duration.seconds(
              1.0 / ResourceBundles.getDouble("GUI_BUNDLE", "framesPerSecond")),
          event -> step() // Call step method
      ));
      gameLoop.play();
    }
  }

  private void step() {
    if (game.getCurrentScene() != null) { // Check if scene is loaded
      game.step(1.0 / ResourceBundles.getDouble("GUI_BUNDLE", "framesPerSecond"));
      render(gc, game.getCurrentScene());
    } else {
      logger.debug("No game scene loaded. Skipping step.");
    }
  }

  private void render(GraphicsContext gc, GameScene scene) {
    gc.clearRect(ResourceBundles.getInt("GUI_BUNDLE", "windowX"),
        ResourceBundles.getInt("GUI_BUNDLE", "windowY"),
        ResourceBundles.getDouble("GUI_BUNDLE", "WINDOW_WIDTH_KEY"),
        ResourceBundles.getDouble("GUI_BUNDLE", "WINDOW_HEIGHT_KEY"));

    for (GameObject obj : scene.getAllObjects()) {
        Transform transform = obj.getComponent(Transform.class);
        if (transform != null && transform.getImagePath()!=null) {
          Image image = new Image(getClass().getResourceAsStream(transform.getImagePath()));
          gc.drawImage(image, transform.getX(), transform.getY(), transform.getScaleX(), transform.getScaleY());
        }
    }
  }

  private KeyCode mapToEngineKeyCode(javafx.scene.input.KeyCode code) {
    try {
      return KeyCode.valueOf(code.name());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  /**
   * Sets the color scheme to user input
   *
   * @param scheme - Name of scheme
   */
  public void setColorScheme(String scheme) {
    scene.getStylesheets().removeIf(s -> s.endsWith("-scheme.css")); // Remove existing scheme
    scene.getStylesheets().add(getClass().getResource(scheme + "-scheme.css").toExternalForm()); // Add new scheme
  }
}