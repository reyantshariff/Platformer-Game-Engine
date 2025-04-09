package oogasalad.gui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import oogasalad.ResourceBundles;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.base.architecture.GameScene;
import oogasalad.engine.component.SpriteRenderer;
import oogasalad.engine.component.Transform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameObjectRenderer {
  private static final Logger logger = LogManager.getLogger(GameObjectRenderer.class);
  private final Scene myScene;

  public GameObjectRenderer(Scene scene) {
    myScene = scene;
  }

  /**
   * Renders the game objects in the given scene onto the canvas.
   *
   * @param gc    The graphics context of the canvas.
   * @param scene The game scene to render.
   */
  public void render(GraphicsContext gc, GameScene scene) {
    gc.clearRect(ResourceBundles.getInt("oogasalad.gui.general", "windowX"),
        ResourceBundles.getInt("oogasalad.gui.general", "windowY"),
        ResourceBundles.getDouble("oogasalad.gui.general", "windowWidth"),
        ResourceBundles.getDouble("oogasalad.gui.general", "windowHeight"));

    for (GameObject obj : scene.getAllObjects()) {
      renderGameObject(gc, obj);
    }
  }

  private void renderGameObject(GraphicsContext gc, GameObject obj) {
    for (Map.Entry<Class<? extends GameComponent>, GameComponent> entry : obj.getAllComponents()
        .entrySet()) {
      GameComponent component = entry.getValue();
      Class<? extends GameComponent> clazz = entry.getKey();
      try {
        String renderMethod = "render" + clazz.getSimpleName();
        Method method = this.getClass()
            .getDeclaredMethod(renderMethod, clazz, GraphicsContext.class);
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
    gc.drawImage(snapshot, component.getX(), component.getY());
  }

  /**
   * renders a javaFX Image object
   *
   * @param component
   * @param gc
   */
  private void renderImageComponent(SpriteRenderer component, GraphicsContext gc) {
    Image image = new Image(component.getImagePath());
    Transform transform = component.getParent().getComponent(Transform.class);
    gc.drawImage(image, transform.getX() + component.getOffsetX(),
        transform.getY() + component.getOffsetY());
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
    tempScene.getStylesheets().addAll(myScene.getStylesheets());
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
