package oogasalad.view.scene;

import javafx.scene.Parent;
import javafx.scene.Scene;
import oogasalad.view.config.StyleConfig;
import oogasalad.view.renderer.GameSceneRenderer;

/**
 * Abstract class for a JavaFX window that uses a GameObjectRenderer to render game objects. This
 * class is a template for creating different types of JavaFX windows
 */

public abstract class ViewScene {

  private final Scene myScene;
  private final GameSceneRenderer mySceneRenderer;

  /**
   * Template for a program JavaFX window using a GameObjectRenderer
   *
   * @param root   define type of JavaFX window, such as BorderPane
   * @param width  window width in pixels
   * @param height window height in pixels
   */
  protected ViewScene(Parent root, double width, double height) {
    myScene = new Scene(root, width, height);
    StyleConfig.setStylesheet(myScene, StyleConfig.getCurrentTheme());
    StyleConfig.setStylesheet(myScene, StyleConfig.getCurrentTheme());
    mySceneRenderer = new GameSceneRenderer(myScene);
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
    // Note: Implement if necessary
  }

  protected GameSceneRenderer getSceneRenderer() {
    return mySceneRenderer;
  }

}
