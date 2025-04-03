package oogasalad.engine.base.event;

import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.component.ColliderComponent;

import java.util.Collection;
import oogasalad.engine.prefabs.dinosaur.Base;

public class IsGroundedConstraint extends GameActionConstraint {

  public IsGroundedConstraint() {
    super();
  }

  @Override
  public boolean check() {
    GameObject player = getParentObject();
    ColliderComponent playerCollider = player.getComponent(ColliderComponent.class);
    if (playerCollider == null) return false;

    Collection<GameObject> others = player.getScene().getAllObjects();
    for (GameObject obj : others) {
      if (obj == player) continue;
      if (obj.getComponent(ColliderComponent.class) != null && obj.getClass() == Base.class &&
          playerCollider.collidesWith(obj)) {
        return true;
      }
    }
    return false;
  }
}
