package oogasalad.engine.base.event;

import java.util.Collection;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.component.Collider;
import oogasalad.engine.prefab.dinosaur.Base;

public class IsGroundedConstraint extends GameActionConstraint {

  public IsGroundedConstraint() {
    super();
  }

  @Override
  public boolean check() {
    GameObject player = getParentObject();
    Collider playerCollider = player.getComponent(Collider.class);
    if (playerCollider == null) {
      return false;
    }

    Collection<GameObject> others = player.getScene().getAllObjects();
    for (GameObject obj : others) {
      if (obj == player) {
        continue;
      }
      if (obj.getComponent(Collider.class) != null && obj.getClass() == Base.class &&
          playerCollider.collidesWith(obj)) {
        return true;
      }
    }

    return false;
  }
}
