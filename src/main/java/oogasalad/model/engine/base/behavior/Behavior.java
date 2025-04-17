package oogasalad.model.engine.base.behavior;

import static oogasalad.model.config.GameConfig.LOGGER;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import oogasalad.model.engine.base.serialization.Serializable;
import oogasalad.model.engine.base.serialization.SerializableField;
import oogasalad.model.engine.component.BehaviorController;

/**
 * Game Behavior is a special component that aims to be inherited and implement specific object's
 * behavior. Normally, like playerController should be one of the game behavior.
 *
 * @author Hsuan-Kai Liao
 */
public class Behavior implements Serializable {

  @SerializableField
  private String behaviorName;
  @SerializableField
  private List<BehaviorConstraint<?>> constraints = new ArrayList<>();
  @SerializableField
  private List<BehaviorAction<?>> actions = new ArrayList<>();

  private BehaviorController controller;

  /**
   * Constructor of the Behavior class. This is used to create a new behavior object
   */
  public Behavior() {
    // Empty to utilize default constructor when necessary
  }

  /**
   * Constructor of the Behavior class. This is used to create a new behavior object
   */
  public Behavior(String behaviorName) {
    this.behaviorName = behaviorName;
  }


  /**
   * Gets the name for the behavior
   *
   * @return String being behavior name
   */
  public String getName() {
    return behaviorName;
  }

  /**
   * Get the controller of the behavior. This is used to get the controller that the behavior
   */
  public BehaviorController getController() {
    return controller;
  }

  /**
   * Sets the controller of the behavior
   *
   * @param controller the controller to set
   */
  public void setBehaviorController(BehaviorController controller) {
    this.controller = controller;
  }

  /**
   * Getter for the constraints
   *
   * @return - a list of the behavior constraints
   */
  public List<BehaviorConstraint<?>> getConstraints() {
    return constraints;
  }

  /**
   * Getter for the actions
   *
   * @return - a list of behavior actions
   */
  public List<BehaviorAction<?>> getActions() {
    return actions;
  }

  /**
   * Execute the behavior. This method checks all the constraints and performs the actions if all
   * the constraints are met.
   */
  public void execute() {
    for (BehaviorConstraint<?> constraint : constraints) {
      if (!constraint.onCheck(constraint.getParameter())) {
        return;
      }
    }

    for (BehaviorAction<?> action : actions) {
      action.onPerform(action.getParameter());
    }
  }

  /**
   * Awake the behavior. This method is used to awake the behavior and all the constraints and
   * actions.
   */
  public void awake() {
    for (BehaviorConstraint<?> constraint : constraints) {
      constraint.setBehavior(this);
      constraint.awake();
    }
    for (BehaviorAction<?> action : actions) {
      action.setBehavior(this);
      action.awake();
    }
  }

  /**
   * Add a constraint to the behavior. This method is used to add a constraint to the behavior.
   *
   * @param constraintClass the constraint class specified
   * @param <T>             the type of the constraint
   */
  public <T extends BehaviorConstraint<?>> T addConstraint(Class<T> constraintClass) {
    try {
      T constraint = constraintClass.getDeclaredConstructor().newInstance();
      constraints.add(constraint);
      return constraint;
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException
             | InvocationTargetException e) {
      LOGGER.error("Failed to create constraint: {}", constraintClass.getName(), e);
      throw new ConstraintConstructionException(
          "Failed to create constraint: " + constraintClass.getName(), e);
    }
  }

  /**
   * Add a constraint to the behavior. This method is used to add a constraint to the behavior.
   *
   * @param constraintInstance the instance of the constraint we wish to add
   */
  public void addConstraint(BehaviorConstraint<?> constraintInstance) {
    try {
      constraints.add(constraintInstance);
    } catch (UnsupportedOperationException | IllegalArgumentException | ClassCastException e) {
      LOGGER.error("Failed to create constraint: {}", constraintInstance.getClass().getName());
      throw new ConstraintConstructionException(
          "Failed to create constraint: " + constraintInstance.getClass().getName(), e);
    }
  }

  /**
   * Remove a constraint from the behavior.
   *
   * @param constraint the constraint to be removed
   */
  public void removeConstraint(BehaviorConstraint<?> constraint) {
    constraints.remove(constraint);
  }

  /**
   * Add an action to the behavior. This method is used to add an action to the behavior.
   *
   * @param actionClass the action class specified
   * @param <T>         the type of the action
   */
  public <T extends BehaviorAction<?>> T addAction(Class<T> actionClass) {
    try {
      T action = actionClass.getDeclaredConstructor().newInstance();
      actions.add(action);
      return action;
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException
             | InvocationTargetException e) {
      LOGGER.error("Failed to create action: {}", actionClass.getName(), e);
      throw new ActionConstructionException("Failed to create action: " + actionClass.getName(), e);
    }
  }

  /**
   * Add an already instantiated action to the actions list
   *
   * @param actionInstance the action instance we wish to add
   */
  public void addAction(BehaviorAction<?> actionInstance) {
    try {
      actions.add(actionInstance);
    } catch (UnsupportedOperationException | IllegalArgumentException | ClassCastException e) {
      LOGGER.error("Failed to add action: {}", actionInstance.getClass().getName());
      throw new ActionConstructionException(
          "Failed to create action: " + actionInstance.getClass().getName(), e);
    }
  }

  /**
   * Remove an action from the behavior.
   *
   * @param action the action to be removed
   */
  public void removeAction(BehaviorAction<?> action) {
    actions.remove(action);
  }

}

