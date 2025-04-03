package oogasalad.player.dinosaur;

import oogasalad.engine.base.architecture.GameScene;
import javafx.scene.Scene;
import oogasalad.engine.base.enumerate.KeyCode;
import oogasalad.engine.base.event.CrouchAction;
import oogasalad.engine.base.event.IsGroundedConstraint;
import oogasalad.engine.base.event.JumpAction;
import oogasalad.engine.component.AccelerationComponent;
import oogasalad.engine.component.ColliderComponent;
import oogasalad.engine.component.CollisionHandlerComponent;
import oogasalad.engine.component.InputHandler;
import oogasalad.engine.component.SpriteSwitcherComponent;
import oogasalad.engine.component.Transform;
import oogasalad.engine.component.VelocityComponent;
import oogasalad.engine.prefabs.dinosaur.Base;
import oogasalad.engine.prefabs.dinosaur.Bird;
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
    tBird.setX(500);
    tBird.setY(500);
    tBird.setScaleX(50);
    tBird.setScaleY(50);

    SpriteSwitcherComponent birdSpriteSwitch = bird.addComponent(SpriteSwitcherComponent.class);
    birdSpriteSwitch.registerState("bird", "/oogasalad/dinosaur/Bird1.png");
    birdSpriteSwitch.setState("bird");

    bird.addComponent(ColliderComponent.class);
  }

  private void makeGroundTest() {
    Base ground = instantiateObject(Base.class);

    Transform tGround = ground.addComponent(Transform.class);
    tGround.setX(0);
    tGround.setY(550);
    tGround.setScaleX(1200);
    tGround.setScaleY(50);

    SpriteSwitcherComponent groundSpriteSwitch = ground.addComponent(SpriteSwitcherComponent.class);
    groundSpriteSwitch.registerState("ground", "/oogasalad/dinosaur/Track.png");
    groundSpriteSwitch.setState("ground");


    ground.addComponent(ColliderComponent.class);
  }

  private Player makePlayerTest() {
    Player player = instantiateObject(Player.class);

    Transform t = player.addComponent(Transform.class);
    t.setX(100);
    t.setY(450);
    t.setScaleX(60);
    t.setScaleY(80);

    VelocityComponent vel = player.addComponent(VelocityComponent.class);
    vel.setVelocityX(80.0);

    AccelerationComponent ac = player.addComponent(AccelerationComponent.class);
    ac.setAccelY(150.0);

    InputHandler input = player.addComponent(InputHandler.class);
    JumpAction jumpAction = new JumpAction(player);
    jumpAction.registerConstraint(IsGroundedConstraint.class);
    input.registerAction(KeyCode.valueOf("UP"), jumpAction);

    CrouchAction crouch = new CrouchAction(player);
    jumpAction.registerConstraint(IsGroundedConstraint.class);
    input.registerAction(KeyCode.DOWN, crouch);

    SpriteSwitcherComponent spriteSwitcher = player.addComponent(SpriteSwitcherComponent.class);
    spriteSwitcher.registerState("run", "/oogasalad/dinosaur/DinoRun1.png");
    spriteSwitcher.setState("run");

    player.addComponent(ColliderComponent.class);

    return player;
  }

  @Override
  public void onDeactivated() {
    // Remove Components
    //
  }
}
