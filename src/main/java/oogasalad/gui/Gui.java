package oogasalad.gui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.component.ImageComponent;
import oogasalad.engine.component.TextComponent;
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
 * The GUI class manages the graphical user interface for the OOGASalad game engine. It handles the
 * creation of the game window, rendering game objects, and processing user input.
 *
 * @author Jack F. Regan and Logan Dracos
 */
public class Gui {

  private static final Logger logger = LogManager.getLogger(Gui.class);
  private Game game;
  private GraphicsContext gc;
  private Timeline gameLoop;
  private Scene scene;

  /**
   * Constructs a new GUI instance for the given game and stage.
   *
   * @param stage The primary stage for the application.
   * @param game  The game instance to be displayed.
   */
  public Gui(Stage stage, Game game) {
    this.game = game;
    ResourceBundles.loadBundle("oogasalad.gui.general");
    generateGui(stage);
  }

  /**
   * Generates the graphical user interface for the game.
   *
   * @param stage The primary stage for the application.
   */
  private void generateGui(Stage stage) {
    logger.debug("Generating GUI...");

    Group root = new Group();
    scene = new Scene(root, ResourceBundles.getInt("oogasalad.gui.general", "windowWidth"),
        ResourceBundles.getInt("oogasalad.gui.general", "windowHeight"));
    Canvas canvas = new Canvas(ResourceBundles.getInt("oogasalad.gui.general", "windowWidth"),
        ResourceBundles.getInt("oogasalad.gui.general", "windowHeight"));

    gc = canvas.getGraphicsContext2D();
    root.getChildren().add(canvas);

    // Apply css styling
    scene.getStylesheets().add(getClass().getResource(ResourceBundles.getString("oogasalad.gui.general", "stylesheet")).toExternalForm());

    game.addScene(MainMenuGameScene.class, "Mainmenu");
    // game.addScene(DinosaurGameScene.class, "Dinosaur");
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
      render(gc, game.getCurrentScene());
    } else {
      logger.debug("No game scene loaded. Skipping step.");
    }
  }

  /**
   * Renders the game objects in the given scene onto the canvas.
   *
   * @param gc    The graphics context of the canvas.
   * @param scene The game scene to render.
   */
  private void render(GraphicsContext gc, GameScene scene) {
    gc.clearRect(ResourceBundles.getInt("oogasalad.gui.general", "windowX"),
        ResourceBundles.getInt("oogasalad.gui.general", "windowY"),
        ResourceBundles.getDouble("oogasalad.gui.general", "windowWidth"),
        ResourceBundles.getDouble("oogasalad.gui.general", "windowHeight"));

    for (GameObject obj : scene.getAllObjects()) {
      for (Map.Entry<Class<? extends GameComponent>, GameComponent> entry : obj.getAllComponents()
          .entrySet()) {
        GameComponent component = entry.getValue();
        Class<? extends GameComponent> clazz = entry.getKey();
        try {
          String renderMethod = "render" + clazz.getSimpleName();
          Method method = this.getClass()
              .getDeclaredMethod(renderMethod, component.getClass(), GraphicsContext.class);
          method.setAccessible(true);
          method.invoke(this, component, gc);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
          logger.error("No such component render method exists");
        }
      }
    }
  }

  /**
   * renders a javaFX Text object
   *
   * @param component
   * @param gc
   */
  private void renderTextComponent(TextComponent component, GraphicsContext gc) {
    Text text = new Text(component.getText());
    applyStyleSheet(text, component.getStyleClass());
    WritableImage snapshot = text.snapshot(null, null);
    gc.drawImage(snapshot, component.getX(), component.getY());
  }

  /**
   * renders a javaFX Image object
   *
   * @param component
   * @param gc
   */
  private void renderImageComponent(ImageComponent component, GraphicsContext gc) {
    Image image = new Image(component.getImagePath());
    gc.drawImage(image, component.getX(), component.getY());
  }

  /**
   * renders a javafx Rectangle object
   *
   * @param component
   * @param gc
   */
  private void renderTransform(Transform component, GraphicsContext gc) {
    gc.fillRect(component.getX(), component.getY(), component.getScaleX(), component.getScaleY());
  }

  private void applyStyleSheet(Node node, String styleSheet) {
    node.getStyleClass().add(styleSheet);
    Group tempRoot = new Group(node);
    Scene tempScene = new Scene(tempRoot);
    tempScene.getStylesheets().addAll(scene.getStylesheets());
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