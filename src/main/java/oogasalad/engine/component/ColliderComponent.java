package oogasalad.engine.component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.base.enumerate.ComponentTag;

/**
 * A physics component responsible for detecting collisions and executing
 * behaviors based on object type.
 */
public class ColliderComponent extends GameComponent {

  /**
   * A map of GameObject classes to their respective collision handlers.
   */
  private final Map<Class<?>, Consumer<GameObject>> collisionHandlers = new HashMap<>();

  /**
   * Returns the component tag identifying this as a collision-related component.
   *
   * @return {@link ComponentTag#COLLISION}
   */
  @Override
  public ComponentTag componentTag() {
    return ComponentTag.COLLISION;
  }

  /**
   * Registers a collision behavior for a specific GameObject class.
   *
   * @param clazz the class to match against colliding objects
   * @param handler the action to perform upon collision
   */
  public void registerCollisionBehavior(Class<?> clazz, Consumer<GameObject> handler) {
    collisionHandlers.put(clazz, handler);
  }

  /**
   * Determines if this component's GameObject is colliding with another.
   *
   * @param other the other GameObject
   * @return true if the objects' bounding boxes overlap
   */
  public boolean collidesWith(GameObject other) {
    Transform a = getParent().getComponent(Transform.class);
    Transform b = other.getComponent(Transform.class);

    if (a == null || b == null) return false;

    return (
        a.getX() < b.getX() + b.getScaleX() &&
            a.getX() + a.getScaleX() > b.getX() &&
            a.getY() < b.getY() + b.getScaleY() &&
            a.getY() + a.getScaleY() > b.getY()
    );
  }

  /**
   * Updates this component by checking for collisions and applying any
   * registered handlers based on the class of the collided object.
   *
   * @param deltaTime time passed since the last update
   */
  @Override
  public void update(double deltaTime) {
    for (GameObject obj : getParent().getScene().getAllObjects()) {
      if (shouldCheckCollision(obj)) {
        handleCollisionWith(obj);
      }
    }
  }

  private boolean shouldCheckCollision(GameObject obj) {
    return obj != getParent() && objHasCollider(obj) && collidesWith(obj);
  }

  private void handleCollisionWith(GameObject obj) {
    for (Map.Entry<Class<?>, Consumer<GameObject>> entry : collisionHandlers.entrySet()) {
      if (entry.getKey().isAssignableFrom(obj.getClass())) {
        entry.getValue().accept(obj);
      }
    }
  }

  private boolean objHasCollider(GameObject obj) {
    try {
      obj.getComponent(ColliderComponent.class);
      obj.getComponent(Transform.class);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
