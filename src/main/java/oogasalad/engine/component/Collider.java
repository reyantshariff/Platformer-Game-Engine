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
  public void awake() {
    transform = getComponent(Transform.class);
  }

  @Override
  public void update(double deltaTime) {
    collidedColliders.clear();
    for (GameComponent collider : getParent().getScene().getAllComponents()
        .get(ComponentTag.COLLISION)) {
      if (collider == this || collidableTags.contains(collider.getParent().getTag())) {
        continue;
      }

      Transform collidedTransform = getComponent(Transform.class);
      if (transform.getX() < collidedTransform.getX() + collidedTransform.getScaleX()
          && transform.getX() + transform.getScaleX() > collidedTransform.getX()
          && transform.getY() < collidedTransform.getY() + collidedTransform.getScaleY()
          && transform.getY() + transform.getScaleY() > collidedTransform.getY()) {
        collidedColliders.add((Collider) collider);
      }
    }
  }

  /**
   * Check if the collider has collided with another collider.
   * 
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
}
