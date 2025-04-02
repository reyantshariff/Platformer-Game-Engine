package oogasalad.player.dinosaur;

import oogasalad.engine.base.architecture.GameScene;
import javafx.scene.Scene;
import oogasalad.engine.base.enumerate.KeyCode;
import oogasalad.engine.base.event.GameAction;
import oogasalad.engine.base.event.JumpAction;
import oogasalad.engine.component.AccelerationComponent;
import oogasalad.engine.component.InputHandler;
import oogasalad.engine.component.Transform;
import oogasalad.engine.component.VelocityComponent;
import oogasalad.engine.prefabs.Player;

public class DinosaurGameScene extends GameScene {
  private Scene scene;

  public DinosaurGameScene(String name) {
    super(name);
  }

  @Override
  public void onActivated() {
    Player player = instantiateObject(Player.class);

    Transform t = player.addComponent(Transform.class);
    t.setX(100);
    t.setY(400);
    t.setScaleX(20);
    t.setScaleY(20);

    VelocityComponent vel = player.addComponent(VelocityComponent.class);
    vel.setVelocityX(40.0);

    AccelerationComponent ac = player.addComponent(AccelerationComponent.class);
    ac.setAccelY(100.0);

    InputHandler input = player.addComponent(InputHandler.class);
    JumpAction jumpAction = new JumpAction(player);
    input.registerAction(KeyCode.valueOf("SPACE"), jumpAction);


    Player ground = instantiateObject(Player.class);

    Transform tGround = ground.addComponent(Transform.class);
    tGround.setX(0);
    tGround.setY(550); // Bottom of a 600px screen
    tGround.setScaleX(800); // Full width
    tGround.setScaleY(50);  // 50px tall

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
  }

  @Override
  public void onDeactivated() {
    // Remove Components
    //
  }
}
