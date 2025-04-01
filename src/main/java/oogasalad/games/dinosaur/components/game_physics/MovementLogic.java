package oogasalad.games.dinosaur.components.game_physics;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.enumerate.ComponentTag;

public class MovementLogic extends GameComponent {

  @Override
  public void update(double deltaTime) {
    PositionComponent pos = getComponent(PositionComponent.class);
    VelocityComponent vel = getComponent(VelocityComponent.class);

    pos.setX(pos.getX() + vel.getVx() * deltaTime);
    pos.setY(pos.getY() + vel.getVy() * deltaTime);
  }

  @Override
  public ComponentTag componentTag() {
    return ComponentTag.PHYSICS;
  }
}
