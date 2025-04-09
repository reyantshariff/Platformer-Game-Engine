package oogasalad.model.engine.prefab;

import oogasalad.model.engine.base.architecture.GameObject;

/**
 * This class is a prefab for the player object. It is used to create a player object in the game.
 */

public class Player extends GameObject {

  /**
   * Constructor for Player
   *
   * @param name the name of the player
   */
  public Player(String name) {
    super(name, "player");
  }
}
