package oogasalad.view.scene;

import javafx.scene.Parent;
import javafx.scene.Scene;
import oogasalad.view.config.StyleConfig;
import oogasalad.view.gui.GameObjectRenderer;

/**
 * Abstract class for a JavaFX window that uses a GameObjectRenderer to render game objects. This
 * class is a template for creating different types of JavaFX windows
 */

public abstract class ViewScene {

  private final StyleConfig styleConfig;
  private final Scene myScene;
  private final GameObjectRenderer myObjectRenderer;

  /**
   * Template for a program JavaFX window using a GameObjectRenderer
   *
   * @param root   define type of JavaFX window, such as BorderPane
   * @param width  window width in pixels
   * @param height window height in pixels
   */
  public ViewScene(Parent root, double width, double height) {
    myScene = new Scene(root, width, height);
    styleConfig = new StyleConfig(myScene);


    myObjectRenderer = new GameObjectRenderer(myScene);
  }

  /**
   * Return this window's Scene object
   *
   * @return JavaFX Scene object
   */
  public Scene getScene() {
    return myScene;
  }

  /**
   * Run all necessary actions when exiting a scene. Should be implemented by subclasses.
   */
  public void deactivate() {
    // Implement if necessary
  }

  /**
   * Return the GameObjectRenderer for this scene
   *
   * @return GameObjectRenderer object
   */
  protected GameObjectRenderer getObjectRenderer() {
    return myObjectRenderer;
  }

  /**
   * Return the StyleConfig for this scene
   *
   * @return StyleConfig object
   */
  protected StyleConfig getStyleConfig() {
    return styleConfig;
  }

}
