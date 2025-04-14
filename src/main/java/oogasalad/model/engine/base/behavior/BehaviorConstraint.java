package oogasalad.model.engine.base.behavior;

import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import oogasalad.model.engine.base.serialization.Serializable;
import oogasalad.model.engine.base.serialization.SerializableField;

/**
 * The BehaviorConstraint class is the base class for all behavior action constraints. Constraints are
 * conditions that must be met in order for an BehaviorAction to be triggered.
 *
 * @param <T> the type of the parameter that the constraint will check against
 * @author Hsuan-Kai Liao 
 */
public abstract class BehaviorConstraint<T> implements Serializable {
  @SerializableField
  private T parameter;

  private Behavior behavior;

  /**
   * Set the behavior of the constraint. This is used to set the behavior that the constraint belongs to
   * @param behavior the behavior to set
   */
  public final void setBehavior(Behavior behavior) {
    this.behavior = behavior;
  }

  /**
   * Get the parameter of the constraint. This is used to get the parameter that the constraint will
   */
  public final T getParameter() {
    return parameter;
  }

  /**
   * Set the parameter of the constraint. This is used to set the parameter that the constraint will
   * check against.
   *
   * @param parameter the parameter to set
   */
  public final void setParameter(T parameter) {
    this.parameter = parameter;
  }

  /**
   * Get the component based on
   *
   * @param componentClass the component class specified
   * @return the component instance
   */
  public final <U extends GameComponent> U getComponent(Class<U> componentClass) {
    return behavior.getController().getComponent(componentClass);
  }

  /**
   * Check if the constraint is met.
   * @param parameter the parameter to check against
   * @return true if the constraint is met, false otherwise
   */
  @SuppressWarnings("unchecked")
  public final boolean onCheck(Object parameter) {
    if (parameter == null) {
      return check(defaultParameter());
    } else {
      return check((T) parameter);
    }
  }

  /**
   * This method is called to get the default parameter.
   * @return the default parameter
   */
  protected abstract T defaultParameter();

  /**
   * Check if the constraint is met.
   * @param parameter the parameter to check against
   * @return true if the constraint is met, false otherwise
   */
  protected abstract boolean check(T parameter);

  /**
   * This method is called to get the component references.
   */
  protected void awake() {
    // NOTE: This method should be override if needed
  }

  /**
   * Check if two BehaviorConstraint objects are equal
   * 
   * @param other the other BehaviorConstraint object to compare against
   * 
   * @return true if the two BehaviorConstraint objects are equal, false otherwise
   */
  public boolean equals(BehaviorConstraint<?> other) {
    return this.getClass().equals(other.getClass());
  }

}
