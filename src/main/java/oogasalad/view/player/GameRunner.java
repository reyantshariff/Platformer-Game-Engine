package oogasalad.view.player;

import static oogasalad.model.config.GameConfig.LOGGER;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;
import oogasalad.model.config.GameConfig;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.base.enumerate.KeyCode;
import oogasalad.model.engine.component.InputHandler;
import oogasalad.view.renderer.GameSceneRenderer;

/**
 * The GUI class manages the canvas-based graphical rendering of the OOGASalad game engine. It is
 * meant to be embedded inside a larger JavaFX UI layout, allowing game rendering to happen
 * alongside JavaFX UI controls. This class handles rendering game scenes, processing input, and
 * running the game loop.
 *
 * @author Jack F. Regan and Logan Dracos
 */
public class GameRunner {

  private final Canvas canvas;
  private final GraphicsContext gc;
  private final GameSceneRenderer objectRenderer;
  private Timeline gameLoop;
  private Game game;

  /**
   * Constructs a new GUI instance for the given game.
   */
  public GameRunner() {

    canvas = new Canvas(GameConfig.getNumber("windowWidth"), GameConfig.getNumber("windowHeight"));
    canvas.setFocusTraversable(true);
    canvas.setOnKeyPressed(this::handleKeyPressed);
    canvas.setOnKeyReleased(this::handleKeyReleased);

    canvas.setOnMouseClicked(e -> handleMouseClick(e.getX(), e.getY()));

    canvas.requestFocus();
    gc = canvas.getGraphicsContext2D();
    objectRenderer = new GameSceneRenderer(null);

    setUpGameLoop();
  }

  private void handleMouseClick(double x, double y) {
    GameScene scene = game.getCurrentScene();
    if (scene == null) {
      return;
    }

    for (GameObject obj : scene.getAllObjects()) {
      if (obj.hasComponent(InputHandler.class)) {
        obj.getComponent(InputHandler.class).registerMouseClick(x, y);
      }
    }
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
  private void setUpGameLoop() {
    if (gameLoop == null) {
      gameLoop = new Timeline();
      gameLoop.setCycleCount(Timeline.INDEFINITE);
      gameLoop.getKeyFrames().add(new KeyFrame(
          Duration.seconds(1.0 / GameConfig.getNumber("framesPerSecond")),
          event -> step())
      );
    }
  }

  /**
   * Set the Game instance for the game runner.
   *
   * @param game The given game instance
   */
  public void setGame(Game game) {
    this.game = game;
  }

  /**
   * Stops an existing game loop
   */
  public void pause() {
    if (gameLoop != null) {
      gameLoop.pause();
    }
  }

  /**
   * Resumes an existing game loop.
   */
  public void play() {
    if (gameLoop != null) {
      gameLoop.play();
    }
  }

  /**
   * Executes a single step of the game loop, updating the game state and rendering the scene.
   */
  public void step() {
    GameScene current = game.getCurrentScene();
    if (current != null) {
      game.step(1.0 / GameConfig.getNumber("framesPerSecond"));

      if (current.hasCamera()) {
        objectRenderer.renderWithCamera(gc, current);
      } else {
        objectRenderer.renderWithoutCamera(gc, current, null);
      }

    } else {
      LOGGER.error(GameConfig.getText("noSuchScene"));
    }
  }

  /**
   * Handles a key press event and maps it to the game engine.
   *
   * @param e The JavaFX key event.
   */
  public void handleKeyPressed(javafx.scene.input.KeyEvent e) {
    KeyCode key = mapToEngineKeyCode(e.getCode());
    if (key != null) {
      game.keyPressed(key.getValue());
    }
  }

  /**
   * Handles a key release event and maps it to the game engine.
   *
   * @param e The JavaFX key event.
   */
  public void handleKeyReleased(javafx.scene.input.KeyEvent e) {
    KeyCode key = mapToEngineKeyCode(e.getCode());
    if (key != null) {
      game.keyReleased(key.getValue());
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
}