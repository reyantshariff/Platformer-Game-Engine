package oogasalad.view.player.dinosaur;

import java.util.List;
import javafx.scene.Scene;
import oogasalad.model.ResourceBundles;
import oogasalad.model.engine.action.VelocityYSetAction;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.base.behavior.Behavior;
import oogasalad.model.engine.component.Camera;
import oogasalad.model.engine.component.PhysicsHandler;
import oogasalad.model.engine.action.JumpAction;
import oogasalad.model.engine.base.enumerate.KeyCode;
import oogasalad.model.engine.component.BehaviorController;
import oogasalad.model.engine.component.Collider;
import oogasalad.model.engine.component.Follower;
import oogasalad.model.engine.component.InputHandler;
import oogasalad.model.engine.component.SpriteRenderer;
import oogasalad.model.engine.component.Transform;
import oogasalad.model.engine.constraint.CollidesWithConstraint;
import oogasalad.model.engine.constraint.KeyPressConstraint;
import oogasalad.model.engine.constraint.TouchingFromAboveConstraint;
import oogasalad.model.engine.prefab.Player;
import oogasalad.model.engine.prefab.dinosaur.Base;
import oogasalad.model.engine.prefab.dinosaur.Bird;
import oogasalad.model.engine.prefab.CameraObj;
import oogasalad.model.engine.base.architecture.GameObject;

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
  }

  @Override
  public void onActivated() {
    makePlayerTest();
    makeGroundTest();
    makeBirdTest();
    makeCameraTest();
  }


  private void makeBirdTest() {
    Bird bird = new Bird("Bird");
    bird.addComponent(Transform.class);
    Transform tBird = bird.getComponent(Transform.class);
    tBird.setX(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "bird.startX"));
    tBird.setY(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "bird.startY"));
    tBird.setScaleX(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "bird.width"));
    tBird.setScaleY(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "bird.height"));


    bird.addComponent(Collider.class);

    SpriteRenderer spriteRenderer = bird.addComponent(SpriteRenderer.class);
    spriteRenderer.setImagePaths(List.of("oogasalad/dinosaur/Bird1.png"));

    registerObject(bird);
  }


  private void makeGroundTest() {
    Base ground = new Base("Ground");
    ground.addComponent(Transform.class);
    Transform tGround = ground.getComponent(Transform.class);
    tGround.setX(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "ground.startX"));
    tGround.setY(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "ground.startY"));
    tGround.setScaleX(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "ground.width"));
    tGround.setScaleY(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "ground.height"));

    ground.addComponent(Collider.class);

    SpriteRenderer spriteRenderer = ground.addComponent(SpriteRenderer.class);
    spriteRenderer.setImagePaths(List.of("oogasalad/dinosaur/Track.png"));

    registerObject(ground);
  }


  private void makePlayerTest() {
    Player player = new Player(ResourceBundles.getString(DINOSAUR_SCENE_BUNDLE, "player.name"));
    registerObject(player);
    player.addComponent(Transform.class);
    Transform t = player.getComponent(Transform.class);

    t.setX(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "player.startX"));
    t.setY(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "player.startY"));
    t.setScaleX(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "player.width"));
    t.setScaleY(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "player.height"));

    player.addComponent(Collider.class);

    player.addComponent(InputHandler.class);

    PhysicsHandler physicsHandler = player.addComponent(PhysicsHandler.class);

    physicsHandler
        .setVelocityX(ResourceBundles.getDouble(DINOSAUR_SCENE_BUNDLE, "player.velocityX"));
    physicsHandler
        .setAccelerationY(ResourceBundles.getDouble(DINOSAUR_SCENE_BUNDLE, "player.accelY"));

    BehaviorController controller = player.addComponent(BehaviorController.class);

    Behavior jumpBehavior = controller.addBehavior();

    jumpBehavior.addAction(JumpAction.class)
        .setParameter(ResourceBundles.getDouble(DINOSAUR_SCENE_BUNDLE, "player.jumpPower"));
    jumpBehavior.addConstraint(KeyPressConstraint.class).setParameter(KeyCode.SPACE);
    jumpBehavior.addConstraint(TouchingFromAboveConstraint.class).setParameter("base");

    Behavior die = controller.addBehavior();

    die.addAction(VelocityYSetAction.class)
        .setParameter(ResourceBundles.getDouble(DINOSAUR_SCENE_BUNDLE, "player.die"));
    die.addConstraint(CollidesWithConstraint.class).setParameter("bird");

    SpriteRenderer spriteRenderer = player.addComponent(SpriteRenderer.class);
    spriteRenderer.setImagePaths(List.of("oogasalad/dinosaur/DinoStart.png"));
  }

  private void makeCameraTest() {
    CameraObj camera = new CameraObj("Camera");
    registerObject(camera);
    camera.addComponent(Transform.class);
    Transform tCamera = camera.getComponent(Transform.class);
    tCamera.setScaleX(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "camera.width"));
    tCamera.setScaleY(ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "camera.height"));
    camera.addComponent(Camera.class);
    Follower follower = new Follower();
    GameObject player = getObject(ResourceBundles.getString(DINOSAUR_SCENE_BUNDLE, "player.name"));
    follower.setFollowObject(player);
    double offsetX = ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "camera.offsetX");
    double offsetY = ResourceBundles.getInt(DINOSAUR_SCENE_BUNDLE, "camera.offsetY");
    follower.setOffset(offsetX, offsetY);
    camera.addComponent(follower);
  }


  @Override
  public void onDeactivated() {
    // Remove Components
    //
  }
}
