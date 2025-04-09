package oogasalad.player.dinosaur;

import javafx.scene.Scene;
import oogasalad.ResourceBundles;
import oogasalad.engine.base.architecture.GameScene;
import oogasalad.engine.base.behavior.Behavior;
import oogasalad.engine.constraint.IsGroundedConstraint;
import oogasalad.engine.action.JumpAction;
import oogasalad.engine.base.enumerate.KeyCode;
import oogasalad.engine.component.BehaviorController;
import oogasalad.engine.component.Collider;
import oogasalad.engine.component.InputHandler;
import oogasalad.engine.component.Transform;
import oogasalad.engine.constraint.KeyHoldConstraint;
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
    Bird bird = new Bird("Bird");

    Transform tBird = bird.addComponent(Transform.class);
    tBird.setX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "bird.startX"));
    tBird.setY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "bird.startY"));
    tBird.setScaleX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "bird.width"));
    tBird.setScaleY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "bird.height"));


    bird.addComponent(Collider.class);

    registerObject(bird);
  }


  private void makeGroundTest() {
    Base ground = new Base("Ground");

    Transform tGround = ground.addComponent(Transform.class);
    tGround.setX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "ground.startX"));
    tGround.setY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "ground.startY"));
    tGround.setScaleX(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "ground.width"));
    tGround.setScaleY(ResourceBundles.getInt("oogasalad.dinosaur.dinosaur", "ground.height"));

    ground.addComponent(Collider.class);

    registerObject(ground);
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
    jumpBehavior.addConstraint(KeyHoldConstraint.class).setParameter(KeyCode.SPACE);


    InputHandler input = player.addComponent(InputHandler.class);

    return player;
  }


  @Override
  public void onDeactivated() {
    // Remove Components
    //
  }
}
