package oogasalad.model.engine.component;

import static oogasalad.model.config.GameConfig.LOGGER;
import java.util.ArrayList;
import oogasalad.model.engine.base.architecture.MissingParentSceneException;
import java.util.List;
import java.awt.Dimension;
import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.base.architecture.GameScene;

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
      transform = getParent().getComponent(Transform.class);
      if (transform == null) {
        throw new IllegalArgumentException("Camera must have a Transform component");
      }
      Dimension screenDimensions = getParent().getScene().getGame().getGameInfo().resolution();
      transform.setScaleX(screenDimensions.getWidth());
      transform.setScaleY(screenDimensions.getHeight());
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
    List<GameObject> objects;
    try {
      GameScene scene = getParent().getScene();
      objects = new ArrayList<>(scene.getAllObjects());
    } catch (MissingParentSceneException e) {
      LOGGER.error("Camera does not have a parent scene", e);
      return new ArrayList<>();
    }
    List<GameObject> objectsInView = new ArrayList<>();
    for (GameObject object : objects) {
      Transform transform;
      try {
      transform = object.getComponent(Transform.class);
      } catch (IllegalArgumentException e) {
        LOGGER.warn("GameObject {} does not have a Transform component", object.getName());
        continue;
      }
      if (isInView(transform)) {
        objectsInView.add(object);
      }
    }
    return objectsInView;
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
