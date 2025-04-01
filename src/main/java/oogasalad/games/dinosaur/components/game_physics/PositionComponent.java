package oogasalad.games.dinosaur.components.game_physics;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.enumerate.ComponentTag;

public class PositionComponent extends GameComponent {
  private double x = 0;
  private  double y = 0;

  public void setX(double x)
  {
    this.x = x;
  }

  public void setY(double y)
  {
    this.y = y;
  }

  public double getY()
  {
    return y;
  }

  public double getX()
  {
    return x;
  }

  @Override
  public ComponentTag componentTag() {
    return ComponentTag.PHYSICS;
  }
}
