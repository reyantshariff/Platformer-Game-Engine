package oogasalad.model.engine.base.behavior;

import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import oogasalad.model.engine.base.serialization.Serializable;
import oogasalad.model.engine.base.serialization.SerializableField;

/**
 * The BehaviorAction class is the base class for all behavior actions. the actions will be called
 * only under certain conditions.
 *
 * @param <T> the type of the parameter that the action will take as input parameter
 * @author Hsuan-Kai Liao
 */
public abstract class BehaviorAction<T> implements Serializable {
  @SerializableField
  private T parameter;

  private Behavior behavior;

  /**
   * Set the behavior of the constraint. This is used to set the behavior that the constraint
   * belongs to
   * 
   * @param behavior the behavior to set
   */
  public final void setBehavior(Behavior behavior) {
    this.behavior = behavior;
  }

  /**
   * @return - Parent behavior
   */
  public Behavior getBehavior() {
    return behavior;
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
   * The type of the constraint. This is used to classify the constraints being checked. Note: This
   * method MUST be override.
   * 
   * @return the type of the constraint
   */
  public abstract ComponentTag ActionType();

  /**
   * This method is called to perform the action
   * 
   * @param parameter the parameter for the action
   */
  @SuppressWarnings("unchecked")
  public final void onPerform(Object parameter) {
    perform((T) parameter);
  }

  /**
   * Performs the action
   * 
   * @param parameter the parameter for the action
   */
  protected abstract void perform(T parameter);

  /**
   * This method is called to get the component references.
   */
  protected void awake() {
    // NOTE: Override this method if needed
  }

  /**
   * Returns whether the action is equal to another action
   * 
   * @param other the other action to compare to
   * @return true if the actions are equal, false otherwise
   */
  public boolean equals(BehaviorAction<?> other) {
    return this.getClass().equals(other.getClass());
  }
}
