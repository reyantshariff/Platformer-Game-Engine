package oogasalad.menu;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import oogasalad.engine.base.architecture.GameScene;

public class MenuGameScene extends GameScene {

  private Scene scene;

  public MenuGameScene (String name) {
    super(name);
  }

  @Override
  public void onActivated() {
    Group root = new Group();
    scene = new Scene(root, 1280, 720); // Set scene size

    Canvas canvas = new Canvas(1280, 720);
    root.getChildren().add(canvas);

    // Create Game Objects
       // Player Button
       // Builder Button
       // Social Hub Button

    // Register Game Objects
       // registerObject(button)

    GraphicsContext gc = canvas.getGraphicsContext2D();

    gc.setFill(Color.WHITE);
    gc.fillRect(0, 0, 1280, 720);
  }

  @Override
  public void onDeactivated() {
    // Remove Components
    //
  }
}