package oogasalad.engine.component;

import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.base.enumerate.ComponentTag;

public class ColliderComponent extends GameComponent {
  @Override
  public ComponentTag componentTag() {
    return ComponentTag.COLLISION;
  }

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
}
