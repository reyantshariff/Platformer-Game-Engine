package oogasalad.view.renderer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import oogasalad.model.ResourceBundles;
import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.component.SpriteRenderer;
import oogasalad.model.engine.component.Transform;
import oogasalad.model.engine.component.Camera;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The GameObjectRenderer class is responsible for rendering game objects and their components
 */

public class GameSceneRenderer {

  private static final Logger logger = LogManager.getLogger(GameSceneRenderer.class);
  private final Scene myScene;
  private double relativeX;
  private double relativeY;

  private static final String UNUSED = "unused";

  /**
   * Constructor for GameObjectRenderer
   *
   * @param scene the scene to render the game objects in
   */
  public GameSceneRenderer(Scene scene) {
    myScene = scene;
  }

  /**
   * For scenes that have a camera: renders the game objects in the given scene onto the canvas
   *
   * @param gc    The graphics context of the canvas.
   * @param scene The game scene to render.
   */
  public void renderWithCamera(GraphicsContext gc, GameScene scene) {
    Camera camera = scene.getCamera();
    if (camera == null) {
      logger.error("Camera not found in scene");
      return;
    }
    Transform cameraTransform = camera.getComponent(Transform.class);
    if (cameraTransform == null) {
      logger.error("Camera transform not found");
      return;
    }
    relativeX = cameraTransform.getX();
    relativeY = cameraTransform.getY();

    renderWithoutCamera(gc, scene, null);
  }

  /**
   * For scenes WITHOUT a camera: renders the game objects in the given scene onto the canvas.
   *
   * @param gc    The graphics context of the canvas.
   * @param scene The game scene to render.
   */
  public void renderWithoutCamera(GraphicsContext gc, GameScene scene, GameObject SelectedGameObject) {
    // TODO: This should not be fixed, this should get from the actual scene
    String baseName = "oogasalad.gui.general";
    int windowX = ResourceBundles.getInt(baseName, "windowX");
    int windowY = ResourceBundles.getInt(baseName, "windowY");
    double windowWidth = ResourceBundles.getDouble(baseName, "windowWidth");
    double windowHeight = ResourceBundles.getDouble(baseName, "windowHeight");
    gc.clearRect(windowX, windowY, windowWidth, windowHeight);

    Collection<GameObject> objects;
    if (scene.getCamera() != null) {
      objects = scene.getAllObjectsInView();
    } else {
      objects = scene.getAllObjects(); // if no camera component in scene, get all objects in scene
    }

    for (GameObject obj : objects) {
      renderGameObject(gc, obj);
    }

    if (SelectedGameObject != null) {
      renderSelectionOverlay(gc, SelectedGameObject);
    }
  }

  private void renderGameObject(GraphicsContext gc, GameObject obj) {
    boolean hasSprite = obj.hasComponent(SpriteRenderer.class);

    if (obj.hasComponent(Camera.class)) {
      return;
    }

    for (Map.Entry<Class<? extends GameComponent>, GameComponent> entry : obj.getAllComponents()
        .entrySet()) {
      Class<? extends GameComponent> clazz = entry.getKey();

      if (hasSprite && clazz.equals(Transform.class)) {
        continue;
      }

      GameComponent component = entry.getValue();
      try {
        String renderMethod = "render" + clazz.getSimpleName();
        Method method =
            this.getClass().getDeclaredMethod(renderMethod, clazz, GraphicsContext.class);
        method.setAccessible(true);
        method.invoke(this, component, gc);
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
        logger.info("No such component render method exists: " + clazz.getSimpleName());
      }
    }

  }

  /**
   * renders a javaFX Text object
   */
  @SuppressWarnings(UNUSED)
  private void renderTextComponent(Text component, GraphicsContext gc) {
    javafx.scene.text.Text text = new javafx.scene.text.Text(component.getText());
    applyStyleSheet(text, String.valueOf(component.getStyleClass()));
    WritableImage snapshot = text.snapshot(null, null);
    gc.drawImage(snapshot, component.getX() - relativeX, component.getY() - relativeY);
  }

  /**
   * renders a javaFX Image object
   */
  @SuppressWarnings(UNUSED)
  private void renderSpriteRenderer(SpriteRenderer component, GraphicsContext gc) {
    GameObject obj = component.getParent();
    Transform transform = obj.getComponent(Transform.class);

    try {
      Image image = new Image(component.getImagePath());
      gc.drawImage(image, transform.getX() + component.getOffsetX() - relativeX,
          transform.getY() + component.getOffsetY() - relativeY, transform.getScaleX(), // width
          // (scale)
          transform.getScaleY() // height (scale)
      );
    } catch (IllegalArgumentException e) {
      logger.error("Failed to render image: " + component.getImagePath());
    }
  }

  /**
   * renders a javafx Rectangle object
   */
  @SuppressWarnings(UNUSED)
  private void renderTransform(Transform component, GraphicsContext gc) {
    gc.fillRect(component.getX() - relativeX, component.getY() - relativeY, component.getScaleX(),
        component.getScaleY());
  }

  private void applyStyleSheet(Node node, String styleSheet) {
    if (myScene == null) {
      logger.error("Could not apply stylesheet: scene is null.");
      return;
    }

    node.getStyleClass().add(styleSheet);
    Group tempRoot = new Group(node);
    Scene tempScene = new Scene(tempRoot);
    tempScene.getStylesheets().addAll(myScene.getStylesheets());
  }

  private void renderSelectionOverlay(GraphicsContext gc, GameObject obj) {
    if (!obj.hasComponent(Transform.class)) return;

    Transform t = obj.getComponent(Transform.class);
    double x = t.getX();
    double y = t.getY();
    double w = t.getScaleX();
    double h = t.getScaleY();

    gc.setStroke(Color.LIGHTBLUE);
    gc.setLineWidth(2);
    gc.strokeRect(x, y, w, h);

    double handleSize = 8;
    double[][] positions = {
        {x, y}, {x + w / 2, y}, {x + w, y},
        {x + w, y + h / 2}, {x + w, y + h},
        {x + w / 2, y + h}, {x, y + h}, {x, y + h / 2}
    };

    gc.setFill(Color.LIGHTBLUE);
    for (double[] pos : positions) {
      gc.fillRect(pos[0] - handleSize / 2, pos[1] - handleSize / 2, handleSize, handleSize);
    }
  }

}
