package oogasalad.model.engine.base.architecture;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class GameSceneTest {

  @Test
  public void getName_getSceneName_returnsSceneName() {
    GameScene scene = new GameScene("Test Scene");
    assertEquals("Test Scene", scene.getName());
  }

  @Test
  public void getId_getSceneId_returnsSceneId() {
    GameScene scene = new GameScene("Test Scene");
    assertNotNull(scene.getId());
  }

  @Test
  public void getGame_getGameFromSceneNotAttachedToAGame_returnsNull() {
    GameScene scene = new GameScene("Test Scene");
    assertNull(scene.getGame());
  }

  @Test
  public void getGame_getGameFromSceneAttachedToAGame_returnsGame() {
    Game game = new Game();
    GameScene scene = new GameScene("Test Scene");
    scene.setGame(game);
    assertEquals(game, scene.getGame());
  }

  @Test
  public void getObject_getNonExistingObjectByIdFromScene_returnsNull() {
    GameScene scene = new GameScene("Test Scene");
    assertNull(scene.getObject(UUID.randomUUID()));
  }

  @Test
  public void getObject_getNonExistingObjectByNameFromScene_throwsException() {
    GameScene scene = new GameScene("Test Scene");
    assertThrows(IllegalArgumentException.class, () -> scene.getObject("Non-existing Object"));
  }

  @Test
  public void registerObject_registerObjectToScene_returnsObject() {
    GameScene scene = new GameScene("Test Scene");
    GameObject object = new GameObject("Test Object");
    scene.registerObject(object);
    assertEquals(object, scene.getObject(object.getId()));
  }

  @Test
  public void unregisterObject_unregisterObjectToScene_checkRemoved() {
    GameScene scene = new GameScene("Test Scene");
    GameObject object = new GameObject("Test Object");
    scene.registerObject(object);
    assertEquals(object, scene.getObject(object.getId()));
    scene.unregisterObject(object);
    assertNull(scene.getObject(object.getId()));
  }

  @Test
  public void getAllObjects_getAllObjectsFromScene_returnsAllObjects() {
    GameScene scene = new GameScene("Test Scene");
    assertEquals(0, scene.getAllObjects().size());
  }

  @Test
  public void subscribeEvent_subscribeToEvent_executedCheck() {
    GameScene scene = new GameScene("Test Scene");
    final boolean[] executed = {false};
    Runnable event = () -> executed[0] = true;
    scene.subscribeEvent(event);
    scene.step(0);
    assertTrue(executed[0]);
  }

}
