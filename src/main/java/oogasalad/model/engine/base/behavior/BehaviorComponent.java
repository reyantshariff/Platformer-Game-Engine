package oogasalad.model.engine.base.behavior;

import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.serialization.Serializable;
import oogasalad.model.engine.base.serialization.SerializableField;

/**
 * Abstract base class for behavior components that use a parameter and reference a parent
 * behavior.
 *
 * @param <T> the type of parameter this component handles
 * @author Hsuan-Kai Liao
 */
public abstract class BehaviorComponent<T> implements Serializable {

  @SerializableField
  private T parameter;

  private Behavior behavior;

  /**
   * Set the behavior that this component belongs to.
   */
  public final void setBehavior(Behavior behavior) {
    this.behavior = behavior;
  }

  /**
   * Get the behavior this component belongs to.
   */
  public final Behavior getBehavior() {
    return behavior;
  }

  /**
   * Get the parameter of the component.
   */
  public final T getParameter() {
    return parameter;
  }

  /**
   * Set the parameter of the component.
   */
  public final void setParameter(T parameter) {
    this.parameter = parameter;
  }

  /**
   * Retrieve a game component from the controller.
   */
  public final <U extends GameComponent> U getComponent(Class<U> componentClass) {
    return behavior.getController().getComponent(componentClass);
  }

  /**
   * This method is called to get the default parameter.
   *
   * @return the default parameter
   */
  protected abstract T defaultParameter();

  /**
   * Method to initialize component references.
   */
  protected void awake() {
    // NOTE: This should be override if needed.
  }

  /**
   * Check for equality based on class type.
   */
  public final boolean equals(BehaviorComponent<?> other) {
    return this.getClass().equals(other.getClass());
  }
}
