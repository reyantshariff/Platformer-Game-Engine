package oogasalad.model.engine.prefab;

import oogasalad.model.engine.base.architecture.GameObject;

/**
 * This class is a prefab for the camera object. It is used to create a camera object in the game.
 * The camera is responsible for rendering the game world.
 */

public class CameraObj extends GameObject {

  /**
   * Constructor for Camera
   *
   * @param name the name of the Camera
   */
  public CameraObj(String name) {
    super(name, "camera");
  }

}
