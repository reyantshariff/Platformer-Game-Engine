package oogasalad.model.engine.component;

import static oogasalad.model.config.GameConfig.LOGGER;
import java.util.ArrayList;
import java.util.List;
import java.awt.Dimension;
import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.base.enumerate.ComponentTag;

/**
 * The CameraComponent class is used to represent a camera in the game. It is responsible for
 * determining which objects are in view.
 */

public class Camera extends GameComponent {

  private Transform transform;

  /**
   * Default constructor for the Camera class.
   */
  public Camera() {
    super();
  }

  @Override
  public void awake() {
    try {
      transform = getParent().getComponent(Transform.class);
      Dimension screenDimensions = getParent().getScene().getGame().getGameInfo().resolution();
      transform.setScaleX(screenDimensions.getWidth());
      transform.setScaleY(screenDimensions.getHeight());
    } catch (NullPointerException e) {
      LOGGER.error("Missing Transform Component or Parent");
      throw new RuntimeException("Missing Transform Component or Parent", e);
    }
  }

  @Override
  public ComponentTag componentTag() {
    return ComponentTag.CAMERA;
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
    } catch (IllegalArgumentException | NullPointerException e) {
      LOGGER.warn("Missing GameScene or Transform Component");
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
