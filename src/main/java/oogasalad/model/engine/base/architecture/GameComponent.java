package oogasalad.model.engine.base.architecture;

import static oogasalad.model.config.GameConfig.LOGGER;
import java.lang.reflect.InvocationTargetException;

import oogasalad.model.engine.base.enumerate.ComponentTag;
import oogasalad.model.engine.base.serialization.Serializable;
import oogasalad.model.engine.base.serialization.SerializedField;
/**
 * The GameComponent class is the base class for all game components. It is used to define the
 * behavior of game objects. Each game object can have multiple components, and each component can
 * have its own logic and behavior.
 *
 * @author Hsuan-Kai Liao, Christian Bepler
 */
public abstract class GameComponent implements Serializable {
  private GameObject parent;

  /**
   * This method is called after all objects have been created and initialized. It is used to set up
   * references to other objects and components. If an object is made mid-game, this method is called
   * right after the constructor.
   */
  protected void awake() {
    // NOTE: Override this method if references are needed
  }

  /**
   * This method is called before the object calls its update method for the first time
   */
  protected void start() {
    // NOTE: This method should be override if needed
  }

  /**
   * This method is called when the component is removed.
   */
  protected void onRemove() {
    // NOTE: This method should be override if needed.
  }

  /**
   * This method is called every frame. It is used to update the object and perform any necessary
   * game logic.
   * @param deltaTime The time since the last frame, in seconds.
   */
  protected void update(double deltaTime) {
    // NOTE: This method should be override if needed.
  }

  /**
   * Return a copy of this GameComponent and its parameters
   *
   * @return new GameComponent object with same characteristics
   */
  public GameComponent copy() {
    try {
      // Create a new instance of the same class using its no-arg constructor.
      GameComponent copy = this.getClass().getDeclaredConstructor().newInstance();

      // Iterate over each serialized field in the original.
      for (SerializedField<?> field : this.getSerializedFields()) {
        setCopyField(copy, field);
      }
      return copy;
    } catch (InstantiationException | IllegalAccessException |
             NoSuchMethodException | InvocationTargetException e) {
      throw new FailedCopyException("Failed to deep copy component: " + this.getClass().getSimpleName(), e);
    }
  }

  private void setCopyField(GameComponent copy, SerializedField<?> field) {
    String fieldName = field.getFieldName();
    Object value = field.getValue();
    for (SerializedField<?> copyField : copy.getSerializedFields()) {
      if (copyField.getFieldName().equals(fieldName) &&
          copyField.getFieldType().equals(field.getFieldType())) {
        // For immutable types (e.g. primitives, Strings) a simple assignment is enough.
        // If the field value is mutable and requires further deep copying,
        // you'll need to handle that separately.
        ((SerializedField<Object>) copyField).setValue(value);
        break;
      }
    }
  }


  /**
   * Get the component based on
   *
   * @param componentClass the component class specified
   * @return the component instance
   */
  public final <T extends GameComponent> T getComponent(Class<T> componentClass) {
    if (parent != null) {
      return parent.getComponent(componentClass);
    }

    LOGGER.error("{} has no parent", this.getClass().getSimpleName());
    throw new IllegalArgumentException("Parent gameObject not exist!");
  }

  /**
   * This is the actual updating order of the component.
   * NOTE: This method MUST be override.
   * @return the specified component tag
   */
  public abstract ComponentTag componentTag();

  /**
   * Get the parent gameObject of the component
   */
  public final GameObject getParent() {
    return parent;
  }

  /**
   * Set the parent gameObject of the component
   */
  final void setParent(GameObject parent) {
    this.parent = parent;
  }

  /**
   * Change the scene to the specified scene name.
   * @param sceneName the name of the scene to be changed to
   */
  public final void changeScene(String sceneName) {
    parent.changeScene(sceneName);
  }
}
