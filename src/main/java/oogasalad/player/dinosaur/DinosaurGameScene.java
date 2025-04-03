package oogasalad.player.dinosaur;

import oogasalad.engine.base.architecture.GameScene;
import javafx.scene.Scene;
import oogasalad.engine.base.enumerate.KeyCode;
import oogasalad.engine.base.event.IsGroundedConstraint;
import oogasalad.engine.base.event.JumpAction;
import oogasalad.engine.component.AccelerationComponent;
import oogasalad.engine.component.ColliderComponent;
import oogasalad.engine.component.CollisionHandlerComponent;
import oogasalad.engine.component.InputHandler;
import oogasalad.engine.component.Transform;
import oogasalad.engine.component.VelocityComponent;
import oogasalad.engine.prefabs.Base;
import oogasalad.engine.prefabs.Bird;
import oogasalad.engine.prefabs.Player;

public class DinosaurGameScene extends GameScene {
  private Scene scene;

  public DinosaurGameScene(String name) {
    super(name);
  }

  @Override
  public void onActivated() {
    Player player = makePlayerTest();

    makeGroundTest();

    makeBirdTest();

    makeCollisionHandlers(player);

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

  private static void makeCollisionHandlers(Player player) {
    CollisionHandlerComponent collisionHandler = player.addComponent(CollisionHandlerComponent.class);

    // Ground collision
    collisionHandler.registerCollisionBehavior(Base.class, groundUse -> {
      VelocityComponent velocity = player.getComponent(VelocityComponent.class);
      velocity.setVelocityY(0.1);
    });

    // Bird collision = death
    collisionHandler.registerCollisionBehavior(Bird.class, birdUse -> {
      System.out.println("Player collided with a bird — Game Over!");
      VelocityComponent velocity = player.getComponent(VelocityComponent.class);
      velocity.setVelocityX(0);
      velocity.setVelocityY(-50);
    });
  }

  private void makeBirdTest() {
    Bird bird = instantiateObject(Bird.class);

    Transform tBird = bird.addComponent(Transform.class);
    tBird.setX(300);
    tBird.setY(500);
    tBird.setScaleX(30);
    tBird.setScaleY(30);

    bird.addComponent(ColliderComponent.class);
  }

  private void makeGroundTest() {
    Base ground = instantiateObject(Base.class);

    Transform tGround = ground.addComponent(Transform.class);
    tGround.setX(0);
    tGround.setY(550);
    tGround.setScaleX(800);
    tGround.setScaleY(50);

    ground.addComponent(ColliderComponent.class);
  }

  private Player makePlayerTest() {
    Player player = instantiateObject(Player.class);

    Transform t = player.addComponent(Transform.class);
    t.setX(100);
    t.setY(500);
    t.setScaleX(20);
    t.setScaleY(20);

    VelocityComponent vel = player.addComponent(VelocityComponent.class);
    vel.setVelocityX(40.0);

    AccelerationComponent ac = player.addComponent(AccelerationComponent.class);
    ac.setAccelY(100.0);

    InputHandler input = player.addComponent(InputHandler.class);
    JumpAction jumpAction = new JumpAction(player);
    jumpAction.registerConstraint(IsGroundedConstraint.class);
    input.registerAction(KeyCode.valueOf("SPACE"), jumpAction);

    player.addComponent(ColliderComponent.class);

    return player;
  }

  @Override
  public void onDeactivated() {
    // Remove Components
    //
  }
}
