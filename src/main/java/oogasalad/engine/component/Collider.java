package oogasalad.engine.component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.serialization.SerializableField;

/**
 * A physics component responsible for detecting collisions and executing behaviors based on object
 * type.
 */
public class Collider extends GameComponent {
  @Override
  public ComponentTag componentTag() {
    return ComponentTag.COLLISION;
  }

  @SerializableField
  private List<String> collidableTags;

  private final Set<Collider> collidedColliders = new HashSet<>();
  private Transform transform;

  @Override
  protected void awake() {
    transform = getComponent(Transform.class);
  }

  @Override
  protected void update(double deltaTime) {
    collidedColliders.clear();
    for (GameObject obj : getParent().getScene().getAllObjects()) {
      processCollision(obj);
    }
  }

  private void processCollision(GameObject obj) {
    Collider collider;

    try {
      collider = obj.getComponent(Collider.class);
    } catch (IllegalArgumentException e) {
      return;
    }

    if (collider == this || collidableTags.contains(collider.getParent().getTag())) {
      return;
    }

    Transform collidedTransform = getComponent(Transform.class);
    if (isOverlapping(collidedTransform)) {
      collidedColliders.add(collider);
    }
  }

  private boolean isOverlapping(Transform collidedTransform) {
    return transform.getX() < collidedTransform.getX() + collidedTransform.getScaleX() &&
        transform.getX() + transform.getScaleX() > collidedTransform.getX() &&
        transform.getY() < collidedTransform.getY() + collidedTransform.getScaleY() &&
        transform.getY() + transform.getScaleY() > collidedTransform.getY();
  }

  /**
   * Check if the collider has collided with another collider.
   * @param tag the gameobject tag of the collider to check for collision
   * @return true if the collider has collided with another collider, false otherwise
   */
  public boolean collidesWith(String tag) {
    for (Collider collider : collidedColliders) {
      if (collider.getParent().getTag().equals(tag)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if the collider is touching another collider from above, within a tolerance.
   * @param tag the gameobject tag of the collider to check for collision
   * @param tolerance the allowable difference between the bottom of this collider and the top of the other collider.
   * @return true if the collider is touching another collider from above, false otherwise
   */
  public boolean touchingFromAbove(String tag, double tolerance) {
    Transform t = getComponent(Transform.class);
    double selfBottom = t.getY() + t.getScaleY();

    for (Collider collider : collidedColliders) {
      if (collider.getParent().getTag().equals(tag)) {
        Transform tOther = collider.getComponent(Transform.class);
        return Math.abs(selfBottom - tOther.getY()) < tolerance;
      }
    }
    return false;
  }

  /**
   * Check if the collider is horizontally aligned with another collider.
   * @param tag the gameobject tag of the collider to check for alignment
   * @return true if the collider is horizontally aligned with another collider, false otherwise
   */
  public boolean horizontallyAligned(String tag) {
    Transform t = getComponent(Transform.class);

    for (Collider collider : collidedColliders) {
      if (collider.getParent().getTag().equals(tag)) {
        Transform tOther = collider.getComponent(Transform.class);
        return t.getX() + t.getScaleX() > tOther.getX() &&
            t.getX() < tOther.getX() + tOther.getScaleX();
      }
    }
    return false;
  }
}
