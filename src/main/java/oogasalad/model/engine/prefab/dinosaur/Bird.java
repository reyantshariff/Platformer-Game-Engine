package oogasalad.model.engine.prefab.dinosaur;

import oogasalad.model.engine.base.architecture.GameObject;

/**
 * This class is a prefab for the bird object. It is used to create a bird object in the game.
 */
@Deprecated
public class Bird extends GameObject {

  /**
   * Constructor for Bird
   *
   * @param name the name of the bird
   */
  public Bird(String name) {
    super(name, "bird");
  }

}
