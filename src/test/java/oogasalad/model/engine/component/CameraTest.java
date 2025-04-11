package oogasalad.model.engine.component;

import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CameraTest {

  private GameScene scene;
  private Camera camera;

  @BeforeEach
  void setup() {
    Game game = new Game();
    scene = new GameScene("testScene");
    game.addScene(scene);

    GameObject cameraObj = new GameObject("CameraObj", "camera");
    Transform cameraTransform = cameraObj.addComponent(Transform.class);
    cameraTransform.setX(0);
    cameraTransform.setY(0);
    cameraTransform.setScaleX(100);
    cameraTransform.setScaleY(100);

    camera = cameraObj.addComponent(Camera.class);
    scene.registerObject(cameraObj);
    camera.awake();
  }

  @Test
  void componentTag_Always_ReturnsCameraTag() {
    assertEquals(ComponentTag.CAMERA, camera.componentTag());
  }

  @Test
  void getObjectsInView_ObjectWithinBounds_IsIncluded() {
    GameObject obj = new GameObject("Visible", "tag");
    Transform t = obj.addComponent(Transform.class);
    t.setX(25);
    t.setY(25);
    t.setScaleX(10);
    t.setScaleY(10);

    scene.registerObject(obj);
    List<GameObject> visible = camera.getObjectsInView();

    assertTrue(visible.contains(obj));
  }

  @Test
  void getObjectsInView_ObjectOutsideBounds_IsExcluded() {
    GameObject obj = new GameObject("Invisible", "tag");
    Transform t = obj.addComponent(Transform.class);
    t.setX(200); // far outside
    t.setY(200);
    t.setScaleX(10);
    t.setScaleY(10);

    scene.registerObject(obj);
    List<GameObject> visible = camera.getObjectsInView();

    assertFalse(visible.contains(obj));
  }

  @Test
  void getObjectsInView_MultipleObjects_OnlyInViewReturned() {
    GameObject inView = new GameObject("Visible", "tag");
    Transform inT = inView.addComponent(Transform.class);
    inT.setX(50);
    inT.setY(50);
    inT.setScaleX(10);
    inT.setScaleY(10);
    scene.registerObject(inView);

    GameObject outOfView = new GameObject("Invisible", "tag");
    Transform outT = outOfView.addComponent(Transform.class);
    outT.setX(150);
    outT.setY(150);
    outT.setScaleX(10);
    outT.setScaleY(10);
    scene.registerObject(outOfView);

    List<GameObject> visible = camera.getObjectsInView();
    assertTrue(visible.contains(inView));
    assertFalse(visible.contains(outOfView));
  }

  @Test
  void getObjectsInView_ObjectMissingTransform_IsSkipped() {
    GameObject obj = new GameObject("NoTransform", "tag");
    scene.registerObject(obj);

    List<GameObject> visible = camera.getObjectsInView();
    assertFalse(visible.contains(obj));
  }

  @Test
  void getObjectsInView_MissingScene_ReturnsEmptyList() {
    GameObject rogueCameraObj = new GameObject("Camera2", "camera");
    rogueCameraObj.addComponent(Transform.class).setScaleX(100);
    rogueCameraObj.getComponent(Transform.class).setScaleY(100);

    Camera cam = rogueCameraObj.addComponent(Camera.class);
    cam.awake();

    assertTrue(cam.getObjectsInView().isEmpty());
  }
}
