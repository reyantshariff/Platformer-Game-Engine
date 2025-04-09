package oogasalad.model.engine.component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import oogasalad.model.engine.base.serialization.SerializableField;

/**
 * A physics component responsible for detecting collisions and executing behaviors based on object
 * type.
 */
public class Collider extends GameComponent {
  private static final double COLLISION_OFFSET =0.1;

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
    collidableTags = new ArrayList<>();
  }

  @Override
  protected void update(double deltaTime) {
    collidedColliders.clear();
    for (GameObject obj : getParent().getScene().getAllObjects()) {
      if(obj.hasComponent(Collider.class)){
        processCollision(obj);
      }
    }
    if(getParent().hasComponent(PhysicsHandler.class)){
      resolveCollisions();
    }
  }

  private void processCollision(GameObject obj) {
    Collider collider = obj.getComponent(Collider.class);

    if (collider == this || collidableTags.contains(collider.getParent().getTag())) {
      return;
    }

    Transform collidedTransform = obj.getComponent(Transform.class);
    if (isOverlapping(collidedTransform)) {
      collidedColliders.add(collider);
    }
  }

  private void resolveCollisions() {
    for (Collider collider : collidedColliders) {
      Transform other = collider.getComponent(Transform.class);

      double thisLeft = transform.getX();
      double thisRight = thisLeft + transform.getScaleX();
      double thisTop = transform.getY();
      double thisBottom = thisTop + transform.getScaleY();

      double otherLeft = other.getX();
      double otherRight = otherLeft + other.getScaleX();
      double otherTop = other.getY();
      double otherBottom = otherTop + other.getScaleY();

      double overlapX = Math.min(thisRight, otherRight) - Math.max(thisLeft, otherLeft);
      double overlapY = Math.min(thisBottom, otherBottom) - Math.max(thisTop, otherTop);

      if (overlapX < overlapY) {
        if (thisRight > otherLeft && thisLeft < otherLeft) {
          transform.setX(transform.getX() - overlapX - COLLISION_OFFSET);
        } else {
          transform.setX(transform.getX() + overlapX + COLLISION_OFFSET);
        }
      } else {
        if (thisBottom > otherTop && thisTop < otherTop) {
          transform.setY(transform.getY() - overlapY - COLLISION_OFFSET);
        } else {
          transform.setY(transform.getY() + overlapY + COLLISION_OFFSET);
        }
      }
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
    for (GameObject obj : getParent().getScene().getAllObjects()) {
      if(!obj.hasComponent(Collider.class)) continue;

      Collider collider = obj.getComponent(Collider.class);

      if (!collider.getParent().getTag().equals(tag)) continue;

      Transform other = obj.getComponent(Transform.class);

      double thisLeft = transform.getX();
      double thisRight = thisLeft + transform.getScaleX();
      double otherLeft = other.getX();
      double otherRight = otherLeft + other.getScaleX();

      boolean horizontalOverlap = thisRight > otherLeft || thisLeft < otherRight;

      double thisBottom = transform.getY() + transform.getScaleY();
      double otherTop = other.getY();

      boolean verticallyAligned = Math.abs(thisBottom - otherTop) <= tolerance;

      if (horizontalOverlap && verticallyAligned) {
        return true;
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
