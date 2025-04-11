package oogasalad.model.engine.prefab.dinosaur;

import oogasalad.model.engine.base.architecture.GameObject;

/**
 * This class is a prefab for the base object. It is used to create a base object in the game.
 */
@Deprecated
public class Base extends GameObject {

  /**
   * Constructor for Base
   *
   * @param name the name of the base
   */
  public Base(String name) {
    super(name, "base");
  }
}
