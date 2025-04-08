package oogasalad.engine.base.behavior;

import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.serialization.Serializable;
import oogasalad.engine.base.serialization.SerializableField;

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
   * The type of the constraint. This is used to classify the constraints being checked.
   * Note: This method MUST be override.
   * @return the type of the constraint
   */
  public abstract ComponentTag ConstraintType();

  /**
   * Check if the constraint is met.
   * @param parameter the parameter to check against
   * @return true if the constraint is met, false otherwise
   */
  @SuppressWarnings("unchecked")
  final boolean onCheck(Object parameter) {
    return check((T) parameter);
  }

  /**
   * Check if the constraint is met.
   * @param parameter the parameter to check against
   * @return true if the constraint is met, false otherwise
   */
  protected abstract boolean check(T parameter);

  /**
   * This method is called to get the component references.
   * NOTE: This method should be override if needed.
   */
  protected void awake() {}
}
