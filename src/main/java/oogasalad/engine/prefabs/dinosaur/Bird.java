package oogasalad.engine.prefabs.dinosaur;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.base.behavior.Behavior;
import oogasalad.engine.component.Transform;

public class Bird extends GameObject {
  /**
   * Outlines Bird Game Object for Dinosaur Game
   * @author Reyan Shariff
   */

  public Bird()
  {
    this(true);
  }

  public Bird(boolean loadDefaults)
  {
    super("Bird");
    if (loadDefaults) 
    { 
      addDefaultComponents();
    }
  }

  private void addDefaultComponents()
  {
    addComponent(Transform.class);
    addComponent(Behavior.class);
  }

}
