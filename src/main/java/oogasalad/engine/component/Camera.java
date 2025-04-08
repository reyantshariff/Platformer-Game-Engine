package oogasalad.engine.component;

import java.util.ArrayList;
import java.util.List;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.base.architecture.GameScene;
import oogasalad.engine.base.enumerate.ComponentTag;

/**
 * The CameraComponent class is used to represent a camera in the game. It is responsible for
 * determining which objects are in view.
 */

public class Camera extends GameComponent {

  private Transform transform;

  public Camera() {
    super();
  }

  @Override
  public void awake() {
    transform = getParent().getComponent(Transform.class);
  }

  @Override
  public ComponentTag componentTag() {
    return ComponentTag.NONE;
  }

  /**
   * Get all objects in the view of the camera.
   *
   * @return a list of GameObjects that are in the view of the camera.
   */
  public List<GameObject> getObjectsInView() {
    try {
      GameScene scene = getParent().getScene();
      List<GameObject> objects = new ArrayList<>(scene.getAllObjects());
      List<GameObject> objectsInView = new ArrayList<>();
      for (GameObject object : objects) {
        Transform transform = object.getComponent(Transform.class);
        if (isInView(transform)) {
          objectsInView.add(object);
        }
      }
      return objectsInView;
    } catch (NullPointerException e) {
      System.err.println("Missing GameScene or Transform Component");
      return new ArrayList<>();
    }
  }

  private boolean isInView(Transform transform) {
    double x = transform.getX();
    double y = transform.getY();
    double width = transform.getScaleX();
    double height = transform.getScaleY();

    double cameraX = this.transform.getX();
    double cameraY = this.transform.getY();
    double cameraWidth = this.transform.getScaleX();
    double cameraHeight = this.transform.getScaleY();
    return (x < cameraX + cameraWidth && x + width > cameraX && y < cameraY + cameraHeight
        && y + height > cameraY);
  }
}
