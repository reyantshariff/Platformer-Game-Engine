package oogasalad.view.gui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TreeTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
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

public class GameObjectRenderer {
  private static final Logger logger = LogManager.getLogger(GameObjectRenderer.class);
  private final Scene myScene;
  private double relativeX;
  private double relativeY;

  /**
   * Constructor for GameObjectRenderer
   * 
   * @param scene the scene to render the game objects in
   */
  public GameObjectRenderer(Scene scene) {
    myScene = scene;
  }

  /**
   * Renders the game objects in the given scene onto the canvas.
   *
   * @param gc The graphics context of the canvas.
   * @param scene The game scene to render.
   */
  public void render(GraphicsContext gc, GameScene scene) {
    String baseName = "oogasalad.gui.general";
    Integer windowX = ResourceBundles.getInt(baseName, "windowX");
    Integer windowY = ResourceBundles.getInt(baseName, "windowY");
    Double windowWidth = ResourceBundles.getDouble(baseName, "windowWidth");
    Double windowHeight = ResourceBundles.getDouble(baseName, "windowHeight");
    gc.clearRect(windowX, windowY, windowWidth, windowHeight);

    try {
      Camera camera = scene.getCamera();
      Transform cameraTransform = camera.getComponent(Transform.class);
      Collection<GameObject> allObjects = scene.getAllObjectsInView();
      relativeX = cameraTransform.getX();
      relativeY = cameraTransform.getY();
      for (GameObject obj : allObjects) {
        renderGameObject(gc, obj);
      }
    } catch (NullPointerException | IllegalArgumentException e) {
      logger.warn("No camera found in scene");
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

      if (hasSprite && clazz.equals(Transform.class))
        continue;

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
   *
   * @param component
   * @param gc
   */
  private void renderTextComponent(Text component, GraphicsContext gc) {
    javafx.scene.text.Text text = new javafx.scene.text.Text(component.getText());
    applyStyleSheet(text, String.valueOf(component.getStyleClass()));
    WritableImage snapshot = text.snapshot(null, null);
    gc.drawImage(snapshot, component.getX() - relativeX, component.getY() - relativeY);
  }

  /**
   * renders a javaFX Image object
   *
   * @param component
   * @param gc
   */
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
    } catch (Exception e) {
      logger.error("Failed to render image: " + component.getImagePath());
    }
  }

  /**
   * renders a javafx Rectangle object
   *
   * @param component
   * @param gc
   */
  private void renderTransform(Transform component, GraphicsContext gc) {
    gc.fillRect(component.getX() - relativeX, component.getY() - relativeY, component.getScaleX(),
        component.getScaleY());
  }

  private void applyStyleSheet(Node node, String styleSheet) {
    node.getStyleClass().add(styleSheet);
    Group tempRoot = new Group(node);
    Scene tempScene = new Scene(tempRoot);
    tempScene.getStylesheets().addAll(myScene.getStylesheets());
  }
}
