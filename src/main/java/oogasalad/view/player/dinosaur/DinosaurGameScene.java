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

/**
 * The DinosaurGameScene class is a game scene for the dinosaur game. It is responsible for creating
 * the game objects and setting up the game scene.
 */

public class DinosaurGameScene extends GameScene {

  private static final String DINOSAUR_SCENE_BUNDLE = "oogasalad.dinosaur.dinosaur";

  private Scene scene;

  /**
   * Constructor for DinosaurGameScene
   *
   * @param name the name of the scene
   */
  public DinosaurGameScene(String name) {
    super(name);
    ResourceBundles.loadBundle(DINOSAUR_SCENE_BUNDLE);
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
    tBird.setX(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "bird.startX"));
    tBird.setY(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "bird.startY"));
    tBird.setScaleX(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "bird.width"));
    tBird.setScaleY(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "bird.height"));


    bird.addComponent(Collider.class);

    registerObject(bird);
  }


  private void makeGroundTest() {
    Base ground = new Base("Ground");

    Transform tGround = ground.getComponent(Transform.class);
    tGround.setX(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "ground.startX"));
    tGround.setY(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "ground.startY"));
    tGround.setScaleX(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "ground.width"));
    tGround.setScaleY(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "ground.height"));

    ground.addComponent(Collider.class);

    registerObject(ground);
  }


  private void makePlayerTest() {
    Player player = new Player("Dinosaur");
    registerObject(player);
    Transform t = player.getComponent(Transform.class);

    t.setX(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "player.startX"));
    t.setY(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "player.startY"));
    t.setScaleX(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "player.width"));
    t.setScaleY(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "player.height"));

    Collider c = player.addComponent(Collider.class);



    player.addComponent(InputHandler.class);

    PhysicsHandler physicsHandler = player.addComponent(PhysicsHandler.class);

    physicsHandler.setVelocityX(ResourceBundles.getDouble(DINOSAUR_SCENE_BUNDLE, "player.velocityX"));
    physicsHandler.setAccelerationY(ResourceBundles.getDouble(DINOSAUR_SCENE_BUNDLE, "player.accelY"));

    BehaviorController controller = player.addComponent(BehaviorController.class);

    Behavior jumpBehavior = controller.addBehavior();

    jumpBehavior.addAction(JumpAction.class).setParameter(ResourceBundles.getDouble(DINOSAUR_SCENE_BUNDLE, "player.jumpPower"));
    jumpBehavior.addConstraint(KeyPressConstraint.class).setParameter(KeyCode.SPACE);
  }


  @Override
  public void onDeactivated() {
    // Remove Components
    //
  }
}
