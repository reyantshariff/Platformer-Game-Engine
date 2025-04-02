package oogasalad.gui;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import oogasalad.ResourceBundles;
import oogasalad.engine.base.enumerate.KeyCode;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import oogasalad.engine.base.architecture.Game;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.base.architecture.GameScene;
import oogasalad.engine.component.Transform;

public class GUI {

  public GUI(Stage stage, Game game) {
    ResourceBundles.loadBundle("oogasalad.gui.general");
    generateGUI(stage, game);
  }

  private void generateGUI(Stage stage, Game game) {
    // Initialize JavaFX Scene
    Group root = new Group();
    Scene scene = new Scene(root, ResourceBundles.getInt("oogasalad.gui.general", "windowWidth"), ResourceBundles.getInt("oogasalad.gui.general", "windowHeight")); // size your game window here
    Canvas canvas = new Canvas(ResourceBundles.getInt("oogasalad.gui.general", "windowWidth"), ResourceBundles.getInt("ooogasalad.gui.general", "windowHeight"));
    GraphicsContext gc = canvas.getGraphicsContext2D();
    root.getChildren().add(canvas);

    stage.setTitle("OOGASalad Platformer");
    stage.setScene(scene);
    stage.show();
    // Map Key Codes
    scene.setOnKeyPressed(e -> {
      KeyCode key = mapToEngineKeyCode(e.getCode());
      if (key != null && game.getCurrentScene() != null) {
        game.getCurrentScene().subscribeInputKey(key);
      }
    });
    // Enter Timeline loop
    Timeline gameLoop = new Timeline();
    gameLoop.setCycleCount(Timeline.INDEFINITE);
    gameLoop.getKeyFrames().add(new KeyFrame(
        Duration.seconds((double) 1 / ResourceBundles.getDouble("oogasalad.gui.general", "framesPerSecond")),
        event -> {
          game.step((double) 1 / ResourceBundles.getDouble("oogasalad.gui.general", "framesPerSecond"));
          render(gc, game.getCurrentScene());
        }
    ));
    gameLoop.play();
  }
  private void render(GraphicsContext gc, GameScene scene) {
    gc.clearRect(ResourceBundles.getInt("oogasalad.gui.general", "windowX"), ResourceBundles.getInt("oogasalad.gui.general", "windowY"), ResourceBundles.getDouble("oogasalad.gui.general", "windowWidth"), ResourceBundles.getDouble("oogasalad.gui.general", "windowHeight"));

    // Simple draw loop — assume all GameObjects have Transform and (eventually) Sprite
    for (GameObject obj : scene.getAllObjects()) {
      Transform transform = obj.getComponent(Transform.class);
      if (transform != null) {
        gc.fillRect(transform.getX(), transform.getY(), transform.getScaleX(),
            transform.getScaleY());
      }
    }
  }
  private KeyCode mapToEngineKeyCode(javafx.scene.input.KeyCode code) {
    try {
      return KeyCode.valueOf(code.name());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

}
