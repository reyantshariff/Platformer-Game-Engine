package oogasalad.games.dinosaur.objects;
import oogasalad.engine.base.architecture.GameObject;

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

  private void addDefaultComponents() {
  }

}
