package oogasalad.engine.base.behavior;

import static oogasalad.config.GameConfig.LOGGER;
import java.util.ArrayList;
import java.util.List;
import oogasalad.engine.base.serialization.Serializable;
import oogasalad.engine.base.serialization.SerializableField;
import oogasalad.engine.component.BehaviorController;

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
   * Get the controller of the behavior. This is used to get the controller that the behavior
   */
  BehaviorController getController() {
    return controller;
  }

  public void setBehaviorController(BehaviorController controller) {
    this.controller = controller;
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
   * Awake the behavior. This method is used to awake the behavior and all the constraints and actions.
   */
  public void awake() {
    for (BehaviorConstraint<?> constraint : constraints) {
      constraint.awake();
    }
    for (BehaviorAction<?> action : actions) {
      action.awake();
    }
  }

  /**
   * Add a constraint to the behavior. This method is used to add a constraint to the behavior.
   * @param constraintClass the constraint class specified
   * @param <T> the type of the constraint
   */
  public <T extends BehaviorConstraint<?>> T addConstraint(Class<T> constraintClass) {
    try {
      T constraint = constraintClass.getDeclaredConstructor().newInstance();
      constraint.setBehavior(this);
      constraints.add(constraint);
      return constraint;
    } catch (Exception e) {
      LOGGER.error("Failed to create constraint: {}", constraintClass.getName(), e);
      throw new RuntimeException("Failed to create constraint: " + constraintClass.getName(), e);
    }
  }

  /**
   * Add a constraint to the behavior. This method is used to add a constraint to the behavior.
   * @param constraintInstance the instance of the constraint we wish to add
   */
  public void addConstraint(BehaviorConstraint<?> constraintInstance) {
    try {
      constraintInstance.setBehavior(this);
      constraints.add(constraintInstance);
    } catch (Exception e) {
      LOGGER.error("Failed to create constraint: {}", constraintInstance.getClass().getName());
      throw new RuntimeException("Failed to create constraint: " + constraintInstance.getClass().getName());
    }
  }

  /**
   * Remove a constraint from the behavior. This method is used to remove a constraint from the
   * @param constraintClass the constraint class specified
   * @param <T> the type of the constraint
   */
  public <T extends BehaviorConstraint<?>> void removeConstraint(Class<T> constraintClass) {
    constraints.removeIf(constraint -> constraint.getClass().equals(constraintClass));
  }

  /**
   * Add an action to the behavior. This method is used to add an action to the behavior.
   * @param actionClass the action class specified
   * @param <T> the type of the action
   */
  public <T extends BehaviorAction<?>> T addAction(Class<T> actionClass) {
    try {
      T action = actionClass.getDeclaredConstructor().newInstance();
      action.setBehavior(this);
      actions.add(action);
      return action;
    } catch (Exception e) {
      LOGGER.error("Failed to create action: {}", actionClass.getName(), e);
      throw new RuntimeException("Failed to create action: " + actionClass.getName(), e);
    }
  }

  /**
   * Add an already instantiated action to the actions list
   * @param actionInstance the action instance we wish to add
   */
  public void addAction(BehaviorAction<?> actionInstance) {
    try {
      actionInstance.setBehavior(this);
      actions.add(actionInstance);
    } catch (Exception e) {
      LOGGER.error("Failed to add action: {}", actionInstance.getClass().getName());
      throw new RuntimeException("Failed to create action: " + actionInstance.getClass().getName());
    }
  }

  /**
   * Remove an action from the behavior. This method is used to remove an action from the
   * @param actionClass the action class specified
   * @param <T> the type of the action
   */
  public <T extends BehaviorAction<?>> void removeAction(Class<T> actionClass) {
    actions.removeIf(action -> action.getClass().equals(actionClass));
  }

}

