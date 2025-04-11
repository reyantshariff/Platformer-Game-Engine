package oogasalad.view.scene;

import java.awt.Rectangle;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import oogasalad.view.gui.GameObjectRenderer;

/**
 * Abstract class for a JavaFX window that uses a GameObjectRenderer to render game objects.
 * This class is a template for creating different types of JavaFX windows
 */

public abstract class ViewScene {
  private final Scene myScene;
  protected final GameObjectRenderer myObjectRenderer;

  /**
   * Template for a program JavaFX window using a GameObjectRenderer
   *
   * @param root define type of JavaFX window, such as BorderPane
   * @param width window width in pixels
   * @param height window height in pixels
   */
  public ViewScene(Parent root, double width, double height) {
    myScene = new Scene(root, width, height);

    myScene.getStylesheets().add(getClass().getResource("/oogasalad/view/stylesheets/mainmenu.css").toExternalForm());
    myObjectRenderer = new GameObjectRenderer(myScene);
  }

  /**
   * Return this window's Scene object
   * @return JavaFX Scene object
   */
  public Scene getScene() {
    return myScene;
  }

}
