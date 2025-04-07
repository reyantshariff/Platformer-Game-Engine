package oogasalad.engine.component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.base.enumerate.ComponentTag;

public class CollisionHandlerComponent extends GameComponent {

  private final Map<Class<?>, Consumer<GameObject>> collisionMap = new HashMap<>();

  /**
   * This is the actual updating order of the component.
   *
   * @return the specified component tag
   */
  @Override
  public ComponentTag componentTag() {
    return ComponentTag.COLLISION;
  }

  public void registerCollisionBehavior(Class<?> clazz, Consumer<GameObject> collisionBehavior) {
    collisionMap.put(clazz, collisionBehavior);
  }

  @Override
  public void update(double deltaTime) {
    ColliderComponent collider = getParent().getComponent(ColliderComponent.class);
    if (collider == null) {
      return;
    }

    for (GameObject obj : getParent().getScene().getAllObjects()) {
      if (obj == getParent()) {
        continue;
      }
      if (!objHasCollider(obj)) {
        continue;
      }

      if (collider.collidesWith(obj)) {
        collisionMap.forEach((clazz, handler) -> {
          if (clazz.isAssignableFrom(obj.getClass())) {
            handler.accept(obj);
          }
        });
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
