package oogasalad.menu;

import java.util.Locale;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import oogasalad.engine.base.architecture.GameScene;
import oogasalad.view.configuration.ResourceBundles;

public class MenuGameScene extends GameScene {

  private Scene scene;

  public MenuGameScene (String name) {
    super(name);
    ResourceBundles.loadBundle("general", new Locale("view"));
  }

  /**
   * Activate the main menu screen.
   */
  @Override
  public void onActivated() {
    Group root = new Group();
    scene = new Scene(root, ResourceBundles.getDouble("general", "windowWidth"),  ResourceBundles.getDouble("general", "windowHeight")); // Set scene size

    Canvas canvas = new Canvas(ResourceBundles.getDouble("general", "windowWidth"),  ResourceBundles.getDouble("general", "windowHeight"));
    root.getChildren().add(canvas);

    // Create Game Objects
       // Player Button
       // Builder Button
       // Social Hub Button

    // Register Game Objects
       // registerObject(button)

    GraphicsContext gc = canvas.getGraphicsContext2D();

    gc.setFill(Color.WHITE);
    gc.fillRect(ResourceBundles.getDouble("general", "windowX"), ResourceBundles.getDouble("general", "windowY"), ResourceBundles.getDouble("general", "windowWidth"),  ResourceBundles.getDouble("general", "windowHeight"));
  }

  /**
   * Deactivate the main menu screen.
   */
  @Override
  public void onDeactivated() {
    // Remove Components
    //
  }
}