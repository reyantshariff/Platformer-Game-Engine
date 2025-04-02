package oogasalad.engine.prefabs.dinosaur;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.component.Behavior;
import oogasalad.engine.component.Transform;

public class Cactus extends GameObject {
  public Cactus() 
  {
    this(true);
  }

  public Cactus(boolean loadDefaults) {
    super("Cactus");
    addDefaultComponents();
  }

  private void addDefaultComponents()
  {
    addComponent(Transform.class);
    addComponent(Behavior.class);
  }

}
