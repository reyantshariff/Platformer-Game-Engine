package oogasalad.engine.component;

import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.enumerate.ComponentTag;

public class GravityComponent extends GameComponent {
  private static final double GRAVITY = 100.0;

  /**
   * This is the actual updating order of the component.
   *
   * @return the specified component tag
   */
  @Override
  public ComponentTag componentTag() {
    return ComponentTag.PHYSICS;
  }

  @Override
  public void update(double deltaTime) {
    VelocityComponent velocity = getParent().getComponent(VelocityComponent.class);
    velocity.setVelocityY(velocity.getVelocityY() + GRAVITY * deltaTime);
  }
}
