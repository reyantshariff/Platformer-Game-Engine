package oogasalad.view.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;
import oogasalad.model.ResourceBundles;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.base.enumerate.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The GUI class manages the canvas-based graphical rendering of the OOGASalad game engine.
 * It is meant to be embedded inside a larger JavaFX UI layout, allowing game rendering to happen
 * alongside JavaFX UI controls.
 *
 * This class handles rendering game scenes, processing input, and running the game loop.
 *
 * @author Jack F. Regan and Logan Dracos
 */
public class Gui {

  private static final Logger logger = LogManager.getLogger(Gui.class);
  private static final String GUI_GENERAL_PATH = "oogasalad.gui.general";

  private final Game game;
  private final Canvas canvas;
  private final GraphicsContext gc;
  private final GameObjectRenderer objectRenderer;
  private Timeline gameLoop;

  /**
   * Constructs a new GUI instance for the given game.
   *
   * @param game The game instance to be displayed.
   */
  public Gui(Game game) {
    this.game = game;
    ResourceBundles.loadBundle(GUI_GENERAL_PATH);

    canvas = new Canvas(
        ResourceBundles.getInt(GUI_GENERAL_PATH, "windowWidth"),
        ResourceBundles.getInt(GUI_GENERAL_PATH, "windowHeight"));


    canvas.setFocusTraversable(true);
    canvas.setOnKeyPressed(this::handleKeyPressed);
    canvas.setOnKeyReleased(this::handleKeyReleased);

    canvas.requestFocus();
    gc = canvas.getGraphicsContext2D();
    objectRenderer = new GameObjectRenderer(null);

    startGameLoop();
  }

  /**
   * Returns the canvas object used for rendering. This should be embedded into a parent layout
   * (e.g., Pane, VBox, StackPane) to display the game.
   *
   * @return the Canvas instance
   */
  public Canvas getCanvas() {
    return canvas;
  }

  /**
   * Starts the game loop, which updates and renders the game at a fixed frame rate.
   */
  private void startGameLoop() {
    if (gameLoop == null) {
      gameLoop = new Timeline();
      gameLoop.setCycleCount(Timeline.INDEFINITE);
      gameLoop.getKeyFrames().add(new KeyFrame(
          Duration.seconds(1.0 / ResourceBundles.getDouble(GUI_GENERAL_PATH, "framesPerSecond")),
          event -> step()));
      gameLoop.play();
    }
  }

  /**
   * Stops an existing game loop
   */
  public void stop() {
    if (gameLoop != null) {
      gameLoop.stop();
      gameLoop = null;
    }
  }

  /**
   * Executes a single step of the game loop, updating the game state and rendering the scene.
   */
  private void step() {
    GameScene current = game.getCurrentScene();
    if (current != null) {
      game.step(1.0 / ResourceBundles.getDouble(GUI_GENERAL_PATH, "framesPerSecond"));
      objectRenderer.render(gc, current);
    } else {
      logger.debug("No game scene loaded. Skipping step.");
    }
  }

  /**
   * Handles a key press event and maps it to the game engine.
   *
   * @param e The JavaFX key event.
   */
  public void handleKeyPressed(javafx.scene.input.KeyEvent e) {
    KeyCode key = mapToEngineKeyCode(e.getCode());
    if (key != null) game.keyPressed(key.getValue());
  }

  /**
   * Handles a key release event and maps it to the game engine.
   *
   * @param e The JavaFX key event.
   */
  public void handleKeyReleased(javafx.scene.input.KeyEvent e) {
    KeyCode key = mapToEngineKeyCode(e.getCode());
    if (key != null) game.keyReleased(key.getValue());
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
}