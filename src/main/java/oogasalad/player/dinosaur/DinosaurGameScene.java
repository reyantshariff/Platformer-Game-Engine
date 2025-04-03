package oogasalad.player.dinosaur;

import oogasalad.ResourceBundles;
import oogasalad.engine.base.architecture.GameScene;
import javafx.scene.Scene;
import oogasalad.engine.base.enumerate.KeyCode;
import oogasalad.engine.base.event.CrouchAction;
import oogasalad.engine.base.event.GameAction;
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
    ResourceBundles.loadBundle("oogasalad.dinosaur.dinosaur");
  }

  @Override
  public void onActivated() {
    Player player = makePlayerTest();
    makeGroundTest();
    makeBirdTest();
    makeCollisionHandlers(player);
  }

  private static void makeCollisionHandlers(Player player) {
    CollisionHandlerComponent collisionHandler = player.addComponent(CollisionHandlerComponent.class);

    // Ground collision
    collisionHandler.registerCollisionBehavior(Base.class, groundUse -> {
      VelocityComponent velocity = player.getComponent(VelocityComponent.class);
      velocity.setVelocityY(ResourceBundles.getDouble("oogasalad.dinosaur.dinosaur", "collision.ground.velocityY"));
    });

    // Bird collision = death
    collisionHandler.registerCollisionBehavior(Bird.class, birdUse -> {
      System.out.println("Player collided with a bird — Game Over!");
      VelocityComponent velocity = player.getComponent(VelocityComponent.class);
      velocity.setVelocityX(ResourceBundles.getDouble("oogasalad.dinosaur.dinosaur", "collision.bird.velocityX"));
      velocity.setVelocityY(ResourceBundles.getDouble("oogasalad.dinosaur.dinosaur", "collision.bird.velocityY"));
    });
  }


  private void makeBirdTest() {
    Bird bird = instantiateObject(Bird.class);

    Transform tBird = bird.addComponent(Transform.class);
    tBird.setX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "bird.startX"));
    tBird.setY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "bird.startY"));
    tBird.setScaleX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "bird.width"));
    tBird.setScaleY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "bird.height"));

    SpriteSwitcherComponent birdSpriteSwitch = bird.addComponent(SpriteSwitcherComponent.class);
    birdSpriteSwitch.registerState("bird", ResourceBundles.getString("oogasalad.dinosaur.dinosaur", "bird.image"));
    birdSpriteSwitch.setState("bird");

    bird.addComponent(ColliderComponent.class);
  }


  private void makeGroundTest() {
    Base ground = instantiateObject(Base.class);

    Transform tGround = ground.addComponent(Transform.class);
    tGround.setX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "ground.startX"));
    tGround.setY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "ground.startY"));
    tGround.setScaleX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "ground.width"));
    tGround.setScaleY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "ground.height"));

    SpriteSwitcherComponent groundSpriteSwitch = ground.addComponent(SpriteSwitcherComponent.class);
    groundSpriteSwitch.registerState("ground", ResourceBundles.getString("oogasalad.dinosaur.dinosaur", "ground.image"));
    groundSpriteSwitch.setState("ground");

    ground.addComponent(ColliderComponent.class);
  }


  private Player makePlayerTest() {
    Player player = instantiateObject(Player.class);

    Transform t = player.addComponent(Transform.class);
    t.setX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "player.startX"));
    t.setY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "player.startY"));
    t.setScaleX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "player.width"));
    t.setScaleY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "player.height"));

    VelocityComponent vel = player.addComponent(VelocityComponent.class);
    vel.setVelocityX(ResourceBundles.getDouble("oogasalad.dinosaur.dinosaur", "player.velocityX"));

    AccelerationComponent ac = player.addComponent(AccelerationComponent.class);
    ac.setAccelY(ResourceBundles.getDouble("oogasalad.dinosaur.dinosaur", "player.accelY"));

    InputHandler input = player.addComponent(InputHandler.class);
    JumpAction jumpAction = new JumpAction();
    jumpAction.registerConstraint(IsGroundedConstraint.class);
    input.registerAction(KeyCode.UP, jumpAction.getClass());

    CrouchAction crouch = new CrouchAction();
    input.registerAction(KeyCode.DOWN, crouch.getClass());

    SpriteSwitcherComponent spriteSwitcher = player.addComponent(SpriteSwitcherComponent.class);
    spriteSwitcher.registerState("run", ResourceBundles.getString("oogasalad.dinosaur.dinosaur", "player.image.run"));
    spriteSwitcher.registerState("crouch", ResourceBundles.getString("oogasalad.dinosaur.dinosaur", "player.image.crouch"));
    spriteSwitcher.registerState("jump", ResourceBundles.getString("oogasalad.dinosaur.dinosaur", "player.image.jump"));
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
