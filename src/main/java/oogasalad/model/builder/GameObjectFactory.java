package oogasalad.model.builder;

import oogasalad.model.engine.base.architecture.GameObject;
import java.lang.reflect.InvocationTargetException;


/**
 * Factory class that creates class Types of GameObject
 *
 * @author Reyan Shariff
 */

public class GameObjectFactory {

  private static final String GAME_OBJECT_PACKAGE = "oogasalad.engine.prefabs.dinosaur"; //TODO: Refactor this line to work with any subfolder.

  /**
   * Method that uses reflection to create a gameObject
   *
   * @param type - Type of game object being created
   * @return - Game object created using reflection
   */
  @SuppressWarnings("unchecked")
  public static GameObject create(String type) {
    try {
      String className = GAME_OBJECT_PACKAGE + "." + type;

      Class<?> clazz = Class.forName(className);

      if (!GameObject.class.isAssignableFrom(clazz)) {
        throw new IllegalArgumentException(type + " is not a valid GameObject type");
      }

      return (GameObject) clazz.getDeclaredConstructor().newInstance();


    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException("GameObject type " + type + " not found", e);
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      throw new RuntimeException("Failed to create GameObject of type " + type, e);
    }
  }
}
