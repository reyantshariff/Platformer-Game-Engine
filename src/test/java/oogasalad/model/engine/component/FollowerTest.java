package oogasalad.model.engine.component;

import java.awt.Dimension;

import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import oogasalad.model.engine.base.architecture.GameInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FollowerTest {

  private GameObject targetObj;
  private Follower follower;
  private Transform targetTransform;
  private Transform followerTransform;

  @BeforeEach
  void setup() {
    Game game = new Game();
    GameScene scene = new GameScene("TestScene");
    game.addScene(scene);

    targetObj = new GameObject("Target", "tag");
    targetTransform = targetObj.addComponent(Transform.class);
    scene.registerObject(targetObj);

    GameObject followerObj = new GameObject("Follower", "tag");
    followerTransform = followerObj.addComponent(Transform.class);
    follower = followerObj.addComponent(Follower.class);
    follower.setFollowObjectName("Target");
    scene.registerObject(followerObj);

  }

  @Test
  void setFollowObject_ValidObject_ObjectStored() {
    follower.setFollowObject(targetObj);
    assertEquals(targetObj, follower.getFollowObject());
  }

  @Test
  void getFollowObjectName_NameSetBeforeAwake_ReturnsCorrectName() {
    Follower follower = new Follower();
    follower.setFollowObjectName("Target");
    assertEquals("Target", follower.getFollowObjectName());
  }

  @Test
  void getFollowObjectName_NameNeverSet_ReturnsNull() {
    Follower follower = new Follower();
    assertNull(follower.getFollowObjectName());
  }

  @Test
  void setOffset_ValidValues_OffsetsStored() {
    follower.setOffset(5.0, -3.0);
    assertEquals(5.0, follower.getOffsetX());
    assertEquals(-3.0, follower.getOffsetY());
  }

  @Test
  void update_FollowingTarget_UpdatesPositionCorrectly() {
    follower.setFollowObject(targetObj);
    follower.setOffset(10.0, -5.0);

    targetTransform.setX(100);
    targetTransform.setY(50);

    follower.update(0.016);

    assertEquals(110.0, followerTransform.getX());
    assertEquals(45.0, followerTransform.getY());
  }

  @Test
  void update_NoFollowObject_ThrowsRuntimeException() {
    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> follower.setFollowObject(null));
    assertTrue(ex.getMessage().contains("Follow Object is null"));
  }

  @Test
  void awake_ValidFollowObjectName_SetsFollowObjectWithoutError() {
    Game game = new Game();
    GameScene scene = new GameScene("FollowerScene");
    game.addScene(scene);

    GameObject target = new GameObject("Target", "tag");
    target.addComponent(Transform.class);
    scene.registerObject(target);

    GameObject followerObj = new GameObject("Follower", "tag");
    followerObj.addComponent(Transform.class);
    Follower follower = followerObj.addComponent(Follower.class);
    follower.setFollowObjectName("Target");

    assertDoesNotThrow(() -> scene.registerObject(followerObj));
  }

  @Test
  void awake_InvalidFollowObjectName_ThrowsRuntimeException() {
    Game game = new Game();
    game.setGameInfo(new GameInfo("", "", "", new Dimension(1000, 1000)));
    GameScene scene = new GameScene("FollowerScene");
    game.addScene(scene);

    GameObject camera = new GameObject("Camera", "tag");
    camera.addComponent(Transform.class);
    camera.addComponent(Camera.class);
    scene.registerObject(camera);
    camera.getComponent(Camera.class).awake();

    GameObject followerObj = new GameObject("Follower", "tag");
    Transform followTransfom = followerObj.addComponent(Transform.class);
    followTransfom.setX(10);
    followTransfom.setY(10);
    Follower follower = followerObj.addComponent(Follower.class);
    follower.setFollowObjectName("NonExistent");
    scene.registerObject(followerObj);

    RuntimeException ex = assertThrows(RuntimeException.class, () -> scene.step(1));


    assertTrue(ex.getMessage().contains("No such Object with name NonExistent"));
  }


  @Test
  void componentTag_Always_ReturnsTransformTag() {
    assertEquals(ComponentTag.TRANSFORM, follower.componentTag());
  }
}
