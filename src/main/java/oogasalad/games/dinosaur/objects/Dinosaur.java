package oogasalad.games.dinosaur.objects;

import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.prefabs.Player;
import oogasalad.games.dinosaur.components.VelocityComponent;

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
    addComponent(VelocityComponent.class);
  }
}
