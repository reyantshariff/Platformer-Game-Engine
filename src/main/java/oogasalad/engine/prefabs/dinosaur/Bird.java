package oogasalad.engine.prefabs.dinosaur;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.component.Behavior;
import oogasalad.engine.component.Transform;

public class Bird extends GameObject {

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
