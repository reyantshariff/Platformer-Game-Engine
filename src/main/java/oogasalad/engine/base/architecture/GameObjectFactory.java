package oogasalad.engine.base.architecture;

import java.lang.reflect.Constructor;

public class GameObjectFactory {

  private static final String GAME_OBJECT_PACKAGE = "oogasalad.engine.prefabs.dinosaur";

  @SuppressWarnings("unchecked")
  public static GameObject create(String type) {
    try {
      String className = GAME_OBJECT_PACKAGE + "." + type;

      Class<?> clazz = Class.forName(className);

      if (!GameObject.class.isAssignableFrom(clazz)) {
        throw new IllegalArgumentException(type + " is not a valid GameObject type");
      }

      Constructor<? extends GameObject> constructor =
          (Constructor<? extends GameObject>) clazz.getConstructor(String.class);

      return constructor.newInstance(type);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create GameObject of type: " + type, e);
    }
  }
}
