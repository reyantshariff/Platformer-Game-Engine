package oogasalad.model.engine.component;

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
  private static final double OVERLAP_TOLERANCE =2;

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
    for (GameComponent collider : getParent().getScene().getAllComponents().get(ComponentTag.COLLISION)) {
      if(collidableTags.contains(collider.getParent().getTag())){
        processCollision(collider.getParent());
      }
    }
    if(getParent().hasComponent(PhysicsHandler.class)){
      resolveCollisions();
    }
  }

  private void processCollision(GameObject obj) {
    Collider collider = obj.getComponent(Collider.class);

    if (collider == this) {
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

      double overlapX = calculateOverlapX(transform, other);
      double overlapY = calculateOverlapY(transform, other);

      if (overlapX < overlapY) {
        resolveCollisionX(transform, other, overlapX);
      } else {
        resolveCollisionY(transform, other);
      }
    }
  }

  private double calculateOverlapX(Transform thisTransform, Transform otherTransform) {
    double thisLeft = thisTransform.getX();
    double thisRight = thisLeft + thisTransform.getScaleX();
    double otherLeft = otherTransform.getX();
    double otherRight = otherLeft + otherTransform.getScaleX();

    return Math.min(thisRight, otherRight) - Math.max(thisLeft, otherLeft);
  }

  private double calculateOverlapY(Transform thisTransform, Transform otherTransform) {
    double thisTop = thisTransform.getY();
    double thisBottom = thisTop + thisTransform.getScaleY();
    double otherTop = otherTransform.getY();
    double otherBottom = otherTop + otherTransform.getScaleY();

    return Math.min(thisBottom, otherBottom) - Math.max(thisTop, otherTop);
  }

  private void resolveCollisionX(Transform thisTransform, Transform otherTransform, double overlapX) {
    double thisRight = thisTransform.getX() + thisTransform.getScaleX();
    double thisLeft = thisTransform.getX();
    double otherLeft = otherTransform.getX();

    if (thisRight > otherLeft && thisLeft < otherLeft) {
      thisTransform.setX(thisTransform.getX() - overlapX - COLLISION_OFFSET);
    } else {
      thisTransform.setX(thisTransform.getX() + overlapX + COLLISION_OFFSET);
    }
  }


  private void resolveCollisionY(Transform thisTransform, Transform otherTransform) {
    double thisBottom = thisTransform.getY() + thisTransform.getScaleY();
    double otherTop = otherTransform.getY();

    if (thisBottom > otherTop && thisTransform.getY() < otherTop) {
      thisTransform.setY(otherTop - thisTransform.getScaleY());

      if (getParent().hasComponent(PhysicsHandler.class)) {
        PhysicsHandler physics = getParent().getComponent(PhysicsHandler.class);
        physics.setVelocityY(0);
      }
    }
  }

  private boolean isOverlapping(Transform other) {
    return transform.getX() < other.getX() + other.getScaleX() + OVERLAP_TOLERANCE &&
        transform.getX() + transform.getScaleX() > other.getX() - OVERLAP_TOLERANCE &&
        transform.getY() < other.getY() + other.getScaleY() + OVERLAP_TOLERANCE &&
        transform.getY() + transform.getScaleY() > other.getY() - OVERLAP_TOLERANCE;
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
      if (isTouchingFromAbove(obj, tag, tolerance)) {
        return true;
      }
    }
    return false;
  }

  private boolean isTouchingFromAbove(GameObject obj, String tag, double tolerance) {
    if (!obj.hasComponent(Collider.class)) {
      return false;
    }

    Collider collider = obj.getComponent(Collider.class);

    if (!collider.getParent().getTag().equals(tag)) {
      return false;
    }

    Transform other = obj.getComponent(Transform.class);

    double thisLeft = transform.getX();
    double thisRight = thisLeft + transform.getScaleX();
    double otherLeft = other.getX();
    double otherRight = otherLeft + other.getScaleX();

    boolean horizontalOverlap = thisRight > otherLeft && thisLeft < otherRight;

    double thisBottom = transform.getY() + transform.getScaleY();
    double otherTop = other.getY();

    boolean verticallyAligned = Math.abs(thisBottom - otherTop) <= tolerance;

    return horizontalOverlap && verticallyAligned;
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
