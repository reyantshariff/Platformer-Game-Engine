package oogasalad.engine.component;

import static oogasalad.config.GameConfig.LOGGER;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.serialization.SerializableField;

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
  private GameObject followObject;
  @SerializableField
  private double offsetX;
  @SerializableField
  private double offsetY;
  private Transform myTransform;

  public Follower() {
    super();
    this.followObject = null;
    this.offsetX = 0;
    this.offsetY = 0;
  }

  @Override
  public void awake() {
    myTransform = getParent().getComponent(Transform.class);
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
}
