package oogasalad.model.engine.component;

import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColliderTest {

  private GameObject player;
  private GameObject platform;
  private Collider playerCollider;
  private Transform playerTransform;

  @BeforeEach
  void setup() {
    Game game = new Game();
    GameScene scene = new GameScene("testScene");
    game.addScene(scene);

    player = new GameObject("Player", "player");
    platform = new GameObject("Platform", "ground");

    playerTransform = player.addComponent(Transform.class);
    Transform platformTransform = platform.addComponent(Transform.class);

    playerCollider = player.addComponent(Collider.class);
    Collider platformCollider = platform.addComponent(Collider.class);

    playerTransform.setX(50);
    playerTransform.setY(50);
    playerTransform.setScaleX(10);
    playerTransform.setScaleY(10);

    platformTransform.setX(50);
    platformTransform.setY(60);
    platformTransform.setScaleX(10);
    platformTransform.setScaleY(10);

    scene.registerObject(player);
    scene.registerObject(platform);

    playerCollider.awake();
    platformCollider.awake();
  }

  @Test
  void componentTag_Always_ReturnsCollisionTag() {
    assertEquals(ComponentTag.COLLISION, playerCollider.componentTag());
  }

  @Test
  void update_WithPhysics_ResolvesCollision() {
    player.addComponent(PhysicsHandler.class);
    player.getComponent(PhysicsHandler.class).awake();
    playerTransform.setX(55);
    playerTransform.setY(60);
    double XPosition = playerTransform.getX();

    playerCollider.update(1.0);

    assertNotEquals(playerTransform.getY(), XPosition);
  }

  @Test
  void update_NoPhysics_DoesNotResolveCollision() {
    double originalY = playerTransform.getY();
    playerCollider.update(1.0);
    assertEquals(originalY, playerTransform.getY());
  }

  @Test
  void collidesWith_CollidedTag_ReturnsTrue() {
    player.addComponent(PhysicsHandler.class).awake();
    playerTransform.setX(55);
    playerTransform.setY(60);
    playerCollider.update(1.0);
    assertTrue(playerCollider.collidesWith("ground"));
  }

  @Test
  void collidesWith_NonCollidedTag_ReturnsFalse() {
    assertFalse(playerCollider.collidesWith("enemy"));
  }

  @Test
  void touchingFromAbove_CorrectVerticalAlignment_ReturnsTrue() {
    playerCollider.update(1.0);
    assertTrue(playerCollider.touchingFromAbove("ground", 0.5));
  }

  @Test
  void touchingFromAbove_NoOverlapHorizontally_ReturnsFalse() {
    platform.getComponent(Transform.class).setX(200);
    playerCollider.update(1.0);
    assertFalse(playerCollider.touchingFromAbove("ground", 1.0));
  }

  @Test
  void touchingFromAbove_TooFarAway_ReturnsFalse() {
    platform.getComponent(Transform.class).setY(200);
    playerCollider.update(1.0);
    assertFalse(playerCollider.touchingFromAbove("ground", 1.0));
  }

  @Test
  void horizontallyAligned_OverlappingColliders_ReturnsTrue() {
    player.addComponent(PhysicsHandler.class).awake();
    playerTransform.setY(60);
    playerCollider.update(1.0);
    assertTrue(playerCollider.horizontallyAligned("ground"));
  }

  @Test
  void horizontallyAligned_NotOverlappingHorizontally_ReturnsFalse() {
    platform.getComponent(Transform.class).setX(200);
    player.addComponent(PhysicsHandler.class).awake();
    playerCollider.update(1.0);
    assertFalse(playerCollider.horizontallyAligned("ground"));
  }

  @Test
  void resolveCollisionY_FromAbove_ResolvesUpward() {
    player.addComponent(PhysicsHandler.class).awake();
    playerCollider.update(1.0);
    assertTrue(playerTransform.getY() < 60);
  }

  @Test
  void resolveCollisionY_FromBelow_ResolvesDownward() {
    playerTransform.setY(65);
    player.addComponent(PhysicsHandler.class).awake();
    playerCollider.update(1.0);
    assertTrue(playerTransform.getY() > 65);
  }

  @Test
  void resolveCollisionX_FromLeft_ResolvesLeftward() {
    platform.getComponent(Transform.class).setX(60);
    platform.getComponent(Transform.class).setY(50);

    playerTransform.setX(61);
    playerTransform.setY(50);

    player.addComponent(PhysicsHandler.class).awake();
    playerCollider.update(1.0);

    assertTrue(playerTransform.getX() > 61);
  }


  @Test
  void resolveCollisionX_FromRight_ResolvesRightward() {
    platform.getComponent(Transform.class).setX(40);
    platform.getComponent(Transform.class).setY(50);

    playerTransform.setX(39);
    playerTransform.setY(50);

    player.addComponent(PhysicsHandler.class).awake();
    playerCollider.update(1.0);

    assertTrue(playerTransform.getX() < 39);
  }


  @Test
  void processCollision_SelfCollision_Ignored() {
    GameObject solo = new GameObject("Solo", "tag");
    Collider soloCollider = solo.addComponent(Collider.class);
    solo.addComponent(Transform.class).setScaleX(10);
    solo.getComponent(Transform.class).setScaleY(10);
    soloCollider.awake();

    GameScene tempScene = new GameScene("temp");
    tempScene.registerObject(solo);
    new Game().addScene(tempScene);

    soloCollider.update(1.0);

    assertFalse(soloCollider.collidesWith("tag"));
  }
}
