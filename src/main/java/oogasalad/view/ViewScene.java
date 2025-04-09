package oogasalad.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import oogasalad.gui.GameObjectRenderer;

public abstract class ViewScene {
  private final Scene myScene;
  private final GameObjectRenderer myObjectRenderer;

  /**
   * Template for a program JavaFX window using a GameObjectRenderer
   *
   * @param root define type of JavaFX window, such as BorderPane
   * @param width window width in pixels
   * @param height window height in pixels
   */
  public ViewScene(Parent root, double width, double height) {
    myScene = new Scene(root, width, height);
    myObjectRenderer = new GameObjectRenderer(myScene);
  }

  /**
   * Return this window's Scene object
   * @return JavaFX Scene object
   */
  public Scene getScene() {
    return myScene;
  }

  /**
   * Return this window's GameObjectRenderer object
   * @return GameObjectRenderer object
   */
  public GameObjectRenderer getObjectRenderer() {
    return myObjectRenderer;
  }

}
