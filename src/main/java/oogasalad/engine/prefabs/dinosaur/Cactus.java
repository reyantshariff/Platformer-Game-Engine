package oogasalad.engine.prefabs.dinosaur;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.component.Behavior;
import oogasalad.engine.component.Transform;

public class Cactus extends GameObject {
  /**
   * Outlines Cactus Game Object for Dinosaur Game
   * @author Reyan Shariff
   */
  public Cactus()
  {
    this(true);
  }

  public Cactus(boolean loadDefaults) {
    super("Cactus");
  }
}
