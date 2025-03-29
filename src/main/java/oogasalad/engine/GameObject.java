package oogasalad.engine;

import java.util.Map;

public abstract class GameObject {
  private GameScene parentScene;
  private String name;
  private Map<Class<? extends GameComponent>, GameComponent> allComponents;

  /**
   * Add the component to the gameObject based on its class.
   * @apiNote Every component class should only have one instance per object.
   * @param componentClass the component class specified
   * @return the added component instance
   */
  public <T extends GameComponent> T addComponent(Class<T> componentClass) {
    return null;
  }

  /**
   * Get the component based on
   * @param componentClass
   * @return
   * @param <T>
   */
  public <T extends GameComponent> T getComponent(Class<T> componentClass) {
    return null;
  }

  public <T extends GameComponent> void removeComponent(Class<T> componentClass) {

  }

  public String getName() {
    return name;
  }

  public GameScene getScene() {
    return parentScene;
  }

}
