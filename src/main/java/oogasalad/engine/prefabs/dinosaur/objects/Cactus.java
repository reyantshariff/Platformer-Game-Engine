package oogasalad.games.dinosaur.objects;
import oogasalad.engine.base.architecture.GameObject;

public class Cactus extends GameObject {
  public Cactus() 
  {
    this(true);
  }

  public Cactus(boolean loadDefaults) {
    super("Cactus");
    addDefaultComponents();
  }

  private void addDefaultComponents() {
  }

}
