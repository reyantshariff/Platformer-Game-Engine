package oogasalad.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GameScene {
  private final Map<Class<GameComponent>, List<GameComponent>> allComponents = new HashMap<>();
  private final Map<String, GameObject> allObjects = new HashMap<>();

  /**
   * This will be called every frame.
   * @param deltaTime the elapsed time between two frames
   */
  public void step(double deltaTime) {

  }

  /**
   * Register the component from the gameObject onto the scene
   * @param gameComponent the component to be registered
   */
  public void registerComponent(GameComponent gameComponent) {}

  /**
   * Unregister the component from the scene.
   * @param gameObject the gameObject to be unregistered
   */
  public void unregisterComponent(GameComponent gameObject) {

  }

  /**
   * Instantiate the gameObject based on the specified gameObject subclass.
   * @param gameObjectClass the gameObject class
   * @return the instantiated gameObject
   */
  public <T extends GameObject> T instantiateObject(Class<T> gameObjectClass) {
    return null;
  }

  /**
   * Register the existing gameObject to the scene.
   * @param gameObject the gameObject to be registered.
   */
  public void registerObject(GameObject gameObject) {

  }

  /**
   * Destroy the gameObject specified.
   * @param gameObject the gameObject to be destroyed
   */
  public void destroyObject(GameObject gameObject) {

  }

}
