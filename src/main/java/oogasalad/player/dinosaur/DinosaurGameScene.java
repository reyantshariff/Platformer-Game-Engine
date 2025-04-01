package oogasalad.player.dinosaur;

import oogasalad.engine.base.architecture.GameScene;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DinosaurGameScene extends GameScene {
  
  private Scene scene;

  public DinosaurGameScene(String name) {
    super(name);
  }

  @Override
  public void onActivated() {
    Group root = new Group();
    scene = new Scene(root, 1280, 720); // Set scene size

    Canvas canvas = new Canvas(1280, 720);
    root.getChildren().add(canvas);

    // Create Game Objects
      // Dinosaur
      // Track
      // Score
      // Bird
      // Cactus
      // Game Over
      // Restart

    // Load values from properties file using propertiesLoader
    /*
    for (GameComponent comp : player.getAllComponents()) {
      if (comp instanceof Serializable) {
        PropertiesLoader.loadFromFile((Serializable) comp, "data/player.properties");
      }
    }
     */

    // Add Game Components
      // dinosaur.addComponent(Transform.class)

    // Register Game Objects
      // registerObject(dinosaur)



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
