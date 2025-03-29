package oogasalad.engine;

public abstract class GameComponent {
  private GameObject parent;

  public abstract void start();

  public abstract void update(double deltaTime);

  protected GameObject getParent() {
    return parent;
  }
}
