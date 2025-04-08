package oogasalad.gui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Text;
import oogasalad.ResourceBundles;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.base.architecture.GameScene;
import oogasalad.engine.component.ImageComponent;
import oogasalad.engine.component.TextComponent;
import oogasalad.engine.component.Transform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameObjectRenderer {
  private static final Logger logger = LogManager.getLogger(GameObjectRenderer.class);

  public GameObjectRenderer() { }

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
            .getDeclaredMethod(renderMethod, component.getClass(), GraphicsContext.class);
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
  private void renderTextComponent(TextComponent component, GraphicsContext gc) {
    Text text = new Text(component.getText());
    applyStyleSheet(text, component.getStyleClass());
    WritableImage snapshot = text.snapshot(null, null);
    gc.drawImage(snapshot, component.getX(), component.getY());
  }

  /**
   * renders a javaFX Image object
   */
  private void renderImageComponent(ImageComponent component, GraphicsContext gc) {
    Image image = new Image(component.getImagePath());
    gc.drawImage(image, component.getX(), component.getY());
  }

  /**
   * renders a javafx Rectangle object
   */
  private void renderTransform(Transform component, GraphicsContext gc) {
    gc.fillRect(component.getX(), component.getY(), component.getScaleX(), component.getScaleY());
  }

  private void applyStyleSheet(Node node, String styleSheet) {
    node.getStyleClass().add(styleSheet);
    //Group tempRoot = new Group(node);
    //Scene tempScene = new Scene(tempRoot);
    //tempScene.getStylesheets().addAll(scene.getStylesheets());
    // TODO: refactor code to enable stylesheet application
  }
}
