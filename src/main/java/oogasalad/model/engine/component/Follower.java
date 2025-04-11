package oogasalad.model.engine.component;

import static oogasalad.model.config.GameConfig.LOGGER;
import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import oogasalad.model.engine.base.serialization.SerializableField;

/**
 * The FollowBehavior class is used to make a game object follow another game object with a
 * specified offset.
 */

public class Follower extends GameComponent {
  @Override
  public ComponentTag componentTag() {
    return ComponentTag.TRANSFORM;
  }

  @SerializableField
  private String followObjectName;
  private double offsetX;
  private double offsetY;
  private Transform myTransform;
  private GameObject followObject;

  /**
   * Constructor for Follower. Sets default values for the offset and the follow object.
   */
  public Follower() {
    super();
    this.followObject = null;
    this.offsetX = 0;
    this.offsetY = 0;
  }

  @Override
  public void awake() {
    myTransform = getParent().getComponent(Transform.class);
    followObject = getParent().getScene().getObject(followObjectName);
    Transform attachTransform = followObject.getComponent(Transform.class);
    offsetX = myTransform.getX() - attachTransform.getX();
    offsetY = myTransform.getY() - attachTransform.getY();
  }

  @Override
  public void update(double deltaTime) {
    try {
      Transform targetTransform = followObject.getComponent(Transform.class);
      myTransform.setX(targetTransform.getX() + offsetX);
      myTransform.setY(targetTransform.getY() + offsetY);
    } catch (NullPointerException e) {
      LOGGER.error("Missing Transform Component");
      throw new RuntimeException("Missing Transform Component", e);
    }
  }

  /**
   * Sets the object to follow.
   *
   * @param followObject the object to follow
   * 
   * @apiNote Use this method if the awake method for this component has already been called.
   */
  public void setFollowObject(GameObject followObject) {
    this.followObject = followObject;
    Transform attachTransform = followObject.getComponent(Transform.class);
    offsetX = myTransform.getX() - attachTransform.getX();
    offsetY = myTransform.getY() - attachTransform.getY();
  }

  /**
   * Sets the offset for the follower.
   *
   * @param offsetX the x offset
   * @param offsetY the y offset
   */
  public void setOffset(double offsetX, double offsetY) {
    this.offsetX = offsetX;
    this.offsetY = offsetY;
  }

  /**
   * Gets the object to follow.
   *
   * @return the object to follow
   */
  public GameObject getFollowObject() {
    return followObject;
  }

  /**
   * Gets the x offset.
   *
   * @return the x offset
   */
  public double getOffsetX() {
    return offsetX;
  }

  /**
   * Gets the y offset.
   *
   * @return the y offset
   */
  public double getOffsetY() {
    return offsetY;
  }

  /**
   * Sets the name of the object to follow.
   *
   * @param followObjectName the name of the object to follow
   * 
   * @apiNote Use this method if the awake method for this component has not been called yet.
   */
  public void setFollowObjectName(String followObjectName) {
    this.followObjectName = followObjectName;
  }

  /**
   * Gets the name of the object to follow.
   *
   * @return the name of the object to follow
   */
  public String getFollowObjectName() {
    return followObjectName;
  }
}
