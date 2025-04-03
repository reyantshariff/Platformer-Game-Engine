package oogasalad.editor;
import oogasalad.engine.base.architecture.GameObject;


/**
 * Factory class that creates class Types of GameObject
 * @author Reyan Shariff
 */

public class GameObjectFactory {

  private static final String GAME_OBJECT_PACKAGE = "oogasalad.engine.prefabs.dinosaur"; //TODO: Refactor this line to work with any subfolder.

  @SuppressWarnings("unchecked")
  public static Class<? extends GameObject> create(String type) {
    try {
      String className = GAME_OBJECT_PACKAGE + "." + type;

      Class<?> clazz = Class.forName(className);

      if (!GameObject.class.isAssignableFrom(clazz)) {
        throw new IllegalArgumentException(type + " is not a valid GameObject type");
      }

      return (Class<? extends GameObject>) clazz;


    } catch (Exception e) {
      throw new RuntimeException("Failed to create GameObject of type: " + type, e);
    }
  }
}
