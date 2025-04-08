package oogasalad.player.dinosaur;

import javafx.scene.Scene;
import oogasalad.ResourceBundles;
import oogasalad.engine.base.architecture.GameScene;
import oogasalad.engine.base.behavior.Behavior;
import oogasalad.engine.constraint.IsGroundedConstraint;
import oogasalad.engine.action.JumpAction;
import oogasalad.engine.base.enumerate.KeyCode;
import oogasalad.engine.base.event.CrouchAction;
import oogasalad.engine.component.BehaviorController;
import oogasalad.engine.component.Collider;
import oogasalad.engine.component.InputHandler;
import oogasalad.engine.component.Transform;
import oogasalad.engine.prefab.Player;
import oogasalad.engine.prefab.dinosaur.Base;
import oogasalad.engine.prefab.dinosaur.Bird;

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
  }


  private void makeBirdTest() {
    Bird bird = instantiateObject(Bird.class);

    Transform tBird = bird.addComponent(Transform.class);
    tBird.setX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "bird.startX"));
    tBird.setY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "bird.startY"));
    tBird.setScaleX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "bird.width"));
    tBird.setScaleY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "bird.height"));

    SpriteSwitcher birdSpriteSwitch = bird.addComponent(SpriteSwitcher.class);
    birdSpriteSwitch.registerState("bird",
        ResourceBundles.getString("oogasalad.dinosaur.dinosaur", "bird.image"));
    birdSpriteSwitch.setState("bird");

    bird.addComponent(Collider.class);
  }


  private void makeGroundTest() {
    Base ground = instantiateObject(Base.class);

    Transform tGround = ground.addComponent(Transform.class);
    tGround.setX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "ground.startX"));
    tGround.setY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "ground.startY"));
    tGround.setScaleX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "ground.width"));
    tGround.setScaleY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "ground.height"));

    SpriteSwitcher groundSpriteSwitch = ground.addComponent(SpriteSwitcher.class);
    groundSpriteSwitch.registerState("ground",
        ResourceBundles.getString("oogasalad.dinosaur.dinosaur", "ground.image"));
    groundSpriteSwitch.setState("ground");

    ground.addComponent(Collider.class);
  }


  private Player makePlayerTest() {
    Player player = new Player("Dinosaur");

    Transform t = player.addComponent(Transform.class);
    t.setX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "player.startX"));
    t.setY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "player.startY"));
    t.setScaleX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "player.width"));
    t.setScaleY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "player.height"));

    BehaviorController controller = player.addComponent(BehaviorController.class);

    Behavior jumpBehavior = controller.addBehavior();

    jumpBehavior.addConstraint(IsGroundedConstraint.class);
    jumpBehavior.addAction(JumpAction.class).setParameter(200.0);
    jumpBehavior.addConstraint(IsGroundedConstraint.class);


    InputHandler input = player.addComponent(InputHandler.class);


    input.registerAction(KeyCode.UP, new GameAction(player) {
      @Override
      public void dispatch() {
        jumpBehavior.execute(); // This runs constraint checks + actions
      }
    });



    CrouchAction crouch = new CrouchAction();
    input.registerAction(KeyCode.DOWN, crouch.getClass());

    SpriteSwitcher spriteSwitcher = player.addComponent(SpriteSwitcher.class);
    spriteSwitcher.registerState("run",
        ResourceBundles.getString("oogasalad.dinosaur.dinosaur", "player.image.run"));
    spriteSwitcher.registerState("crouch",
        ResourceBundles.getString("oogasalad.dinosaur.dinosaur", "player.image.crouch"));
    spriteSwitcher.registerState("jump",
        ResourceBundles.getString("oogasalad.dinosaur.dinosaur", "player.image.jump"));
    spriteSwitcher.setState("run");

    Collider collider = player.addComponent(Collider.class);

    collider.registerCollisionBehavior(Base.class, groundUse -> {
      VelocityComponent velocity = player.getComponent(VelocityComponent.class);
      velocity.setVelocityY(ResourceBundles.getDouble(
          "oogasalad.dinosaur.dinosaur", "collision.ground.velocityY"
      ));
    });

    collider.registerCollisionBehavior(Bird.class, birdUse -> {
      VelocityComponent velocity = player.getComponent(VelocityComponent.class);
      velocity.setVelocityX(ResourceBundles.getDouble(
          "oogasalad.dinosaur.dinosaur", "collision.bird.velocityX"
      ));
      velocity.setVelocityY(ResourceBundles.getDouble(
          "oogasalad.dinosaur.dinosaur", "collision.bird.velocityY"
      ));
    });

    registerObject(player);

    return player;
  }


  @Override
  public void onDeactivated() {
    // Remove Components
    //
  }
}
