package oogasalad.engine.prefabs.dinosaur.objects;

import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.component.GravityComponent;
import oogasalad.engine.component.InputHandler;
import oogasalad.engine.component.Transform;
import oogasalad.engine.component.VelocityComponent;



public class Dinosaur extends GameObject {
  public Dinosaur()
  {
    this(true);
  }

  public Dinosaur(boolean loadDefaults)
  {
    super("Dinosaur");
    if (loadDefaults)
    {
      addDefaultComponents();
    }
  }

  private void addDefaultComponents() {
    addComponent(GravityComponent.class);
    addComponent(VelocityComponent.class);
    addComponent(InputHandler.class);
    addComponent(Transform.class);
  }
}
