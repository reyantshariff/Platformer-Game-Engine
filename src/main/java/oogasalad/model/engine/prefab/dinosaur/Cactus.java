package oogasalad.model.engine.prefab.dinosaur;

import oogasalad.model.engine.base.architecture.GameObject;

/**
 * This class is a prefab for the cactus object. It is used to create a cactus object in the game.
 * The cactus is a type of obstacle that the player must avoid.
 *
 * @author Reyan Shariff
 */
@Deprecated
public class Cactus extends GameObject {

  /**
   * Constructor for Cactus
   * 
   * @param name the name of the cactus
   */
  public Cactus(String name) {
    super(name, "cactus");
  }

}
