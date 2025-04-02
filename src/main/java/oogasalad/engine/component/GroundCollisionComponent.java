package oogasalad.engine.component;

import java.util.Collection;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.base.architecture.GameScene;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.prefabs.Base;

public class GroundCollisionComponent extends GameComponent {

  @Override
  public ComponentTag componentTag() {
    return ComponentTag.COLLISION;
  }

  @Override
  public void update(double deltaTime) {
    ColliderComponent playerCollider = getParent().getComponent(ColliderComponent.class);
    VelocityComponent playerVelocity = getParent().getComponent(VelocityComponent.class);
    if (playerCollider == null) return;

    Collection<GameObject> others = getParent().getScene().getAllObjects();
    for (GameObject obj : others) {
      if (obj == getParent()) continue;
      if (obj.getComponent(ColliderComponent.class) != null && obj.getClass() == Base.class &&
          playerCollider.collidesWith(obj)) {
        playerVelocity.setVelocityY(0.1);
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
