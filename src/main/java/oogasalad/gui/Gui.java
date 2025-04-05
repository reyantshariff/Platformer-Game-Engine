<<<<<<< HEAD:src/main/java/oogasalad/gui/GUI.java
=======
package oogasalad.gui;

import javafx.scene.Group;
import javafx.scene.image.Image;
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
<<<<<<< HEAD:src/main/java/oogasalad/gui/GUI.java
 * Our main GUI class that handles the game, graphics context, timeline, and scene.
 * We use a resource bundle to modularize literals, and we have a logger implemented.
 */
public class GUI {
  private static final Logger logger = LogManager.getLogger(GUI.class);

  private static final String GUI_BUNDLE = "oogasalad.gui.general";
  private static final String WINDOW_WIDTH_KEY = "windowWidth";
  private static final String WINDOW_HEIGHT_KEY = "windowHeight";

=======
 * The GUI class manages the graphical user interface for the OOGASalad game engine. It handles the
 * creation of the game window, rendering game objects, and processing user input.
 *
 * @author Jack F. Regan and Logan Dracos
 */
public class Gui {

  private static final Logger logger = LogManager.getLogger(Gui.class);
>>>>>>> 78df8b2 (SALAD-01: clean up pipeline):src/main/java/oogasalad/gui/Gui.java
  private Game game;
  private GraphicsContext gc;
  private Timeline gameLoop;
  private Scene scene;

  /**
<<<<<<< HEAD:src/main/java/oogasalad/gui/GUI.java
   * Constructor for our GUI class
   *
   * @param stage - Stage for GUI to render
   * @param game - Game to be able to render objects
   */
  public GUI(Stage stage, Game game) {
    this.game = game;
    ResourceBundles.loadBundle("GUI_BUNDLE");
    generateGUI(stage);
=======
   * Constructs a new GUI instance for the given game and stage.
   *
   * @param stage The primary stage for the application.
   * @param game  The game instance to be displayed.
   */
  public Gui(Stage stage, Game game) {
    this.game = game;
    ResourceBundles.loadBundle("oogasalad.gui.general");
    generateGui(stage);
>>>>>>> 78df8b2 (SALAD-01: clean up pipeline):src/main/java/oogasalad/gui/Gui.java
  }

  /**
   * Generates the graphical user interface for the game.
   *
   * @param stage The primary stage for the application.
   */
  private void generateGui(Stage stage) {
    logger.debug("Generating GUI...");

    Group root = new Group();
    scene = new Scene(root, ResourceBundles.getInt("GUI_BUNDLE", "WINDOW_WIDTH_KEY"),
        ResourceBundles.getInt("GUI_BUNDLE", "WINDOW_HEIGHT_KEY"));
    Canvas canvas = new Canvas(ResourceBundles.getInt("GUI_BUNDLE", "WINDOW_WIDTH_KEY"),
        ResourceBundles.getInt("GUI_BUNDLE", "WINDOW_HEIGHT_KEY"));
    gc = canvas.getGraphicsContext2D();
    root.getChildren().add(canvas);

    // Apply css styling

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

  /**
   * Starts the game loop, which updates and renders the game at a fixed frame rate.
   */
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

  /**
   * Executes a single step of the game loop, updating the game state and rendering the scene.
   */
  private void step() {
    if (game.getCurrentScene() != null) { // Check if scene is loaded
      game.step(1.0 / ResourceBundles.getDouble("GUI_BUNDLE", "framesPerSecond"));
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
   void render(GraphicsContext gc, GameScene scene) {
    gc.clearRect(ResourceBundles.getInt("GUI_BUNDLE", "windowX"),
        ResourceBundles.getInt("GUI_BUNDLE", "windowY"),
        ResourceBundles.getDouble("GUI_BUNDLE", "WINDOW_WIDTH_KEY"),
        ResourceBundles.getDouble("GUI_BUNDLE", "WINDOW_HEIGHT_KEY"));

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
<<<<<<< HEAD:src/main/java/oogasalad/gui/GUI.java

  /**
   * Sets the color scheme to user input
   *
   * @param scheme - Name of scheme
   */
  public void setColorScheme(String scheme) {
    scene.getStylesheets().removeIf(s -> s.endsWith("-scheme.css")); // Remove existing scheme
    scene.getStylesheets().add(getClass().getResource(scheme + "-scheme.css").toExternalForm()); // Add new scheme
  }
=======
>>>>>>> 78df8b2 (SALAD-01: clean up pipeline):src/main/java/oogasalad/gui/Gui.java
}
>>>>>>> 7a58b6c (SALAD-01: clean up pipeline):src/main/java/oogasalad/gui/Gui.java
