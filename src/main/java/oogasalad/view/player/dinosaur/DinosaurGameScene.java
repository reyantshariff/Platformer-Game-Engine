package oogasalad.view.player.dinosaur;

import javafx.scene.Scene;
import oogasalad.model.ResourceBundles;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.base.behavior.Behavior;
import oogasalad.model.engine.component.PhysicsHandler;
import oogasalad.model.engine.action.JumpAction;
import oogasalad.model.engine.base.enumerate.KeyCode;
import oogasalad.model.engine.component.BehaviorController;
import oogasalad.model.engine.component.Collider;
import oogasalad.model.engine.component.InputHandler;
import oogasalad.model.engine.component.Transform;
import oogasalad.model.engine.constraint.KeyPressConstraint;
import oogasalad.model.engine.prefab.Player;
import oogasalad.model.engine.prefab.dinosaur.Base;
import oogasalad.model.engine.prefab.dinosaur.Bird;

public class DinosaurGameScene extends GameScene {

  private Scene scene;

  public DinosaurGameScene(String name) {
    super(name);
    ResourceBundles.loadBundle("oogasalad.dinosaur.dinosaur");
    onActivated();
  }

  @Override
  public void onActivated() {
    makePlayerTest();
    makeGroundTest();
    makeBirdTest();
  }


  private void makeBirdTest() {
    Bird bird = new Bird("Bird");

    Transform tBird = bird.getComponent(Transform.class);
    tBird.setX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "bird.startX"));
    tBird.setY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "bird.startY"));
    tBird.setScaleX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "bird.width"));
    tBird.setScaleY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "bird.height"));


    bird.addComponent(Collider.class);

    registerObject(bird);
  }


  private void makeGroundTest() {
    Base ground = new Base("Ground");

    Transform tGround = ground.getComponent(Transform.class);
    tGround.setX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "ground.startX"));
    tGround.setY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "ground.startY"));
    tGround.setScaleX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "ground.width"));
    tGround.setScaleY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "ground.height"));

    ground.addComponent(Collider.class);

    registerObject(ground);
  }


  private void makePlayerTest() {
    Player player = new Player("Dinosaur");
    registerObject(player);
    Transform t = player.getComponent(Transform.class);

    t.setX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "player.startX"));
    t.setY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "player.startY"));
    t.setScaleX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "player.width"));
    t.setScaleY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "player.height"));

    Collider c = player.addComponent(Collider.class);



    player.addComponent(InputHandler.class);

    PhysicsHandler physicsHandler = player.addComponent(PhysicsHandler.class);

    physicsHandler.setVelocityX(ResourceBundles.getDouble("oogasalad.dinosaur.dinosaur", "player.velocityX"));
    physicsHandler.setAccelerationY(ResourceBundles.getDouble("oogasalad.dinosaur.dinosaur", "player.accelY"));

    BehaviorController controller = player.addComponent(BehaviorController.class);

    Behavior jumpBehavior = controller.addBehavior();

    jumpBehavior.addAction(JumpAction.class).setParameter(ResourceBundles.getDouble("oogasalad.dinosaur.dinosaur", "player.jumpPower"));
    jumpBehavior.addConstraint(KeyPressConstraint.class).setParameter(KeyCode.SPACE);
  }


  @Override
  public void onDeactivated() {
    // Remove Components
    //
  }
}
