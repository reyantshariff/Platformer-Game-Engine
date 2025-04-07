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
  private List<BehaviorConstraint<?>> constraints = new ArrayList<>();
  @SerializableField
  private List<BehaviorAction<?>> actions = new ArrayList<>();

  private final BehaviorController controller;

  /**
   * Constructor of the Behavior class. This is used to create a new behavior object
   * @param controller the controller that the behavior belongs to
   */
  public Behavior(BehaviorController controller) {
    this.controller = controller;
  }

  /**
   * Get the controller of the behavior. This is used to get the controller that the behavior
   */
  BehaviorController getController() {
    return controller;
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
   * Add a constraint to the behavior. This method is used to add a constraint to the behavior.
   * @param constraintClass the constraint class specified
   * @param <T> the type of the constraint
   */
  public <T extends BehaviorConstraint<?>> void addConstraint(Class<T> constraintClass) {
    try {
      T constraint = constraintClass.getDeclaredConstructor().newInstance();
      constraint.setBehavior(this);
      constraints.add(constraint);
    } catch (Exception e) {
      LOGGER.error("Failed to create constraint: {}", constraintClass.getName(), e);
      throw new RuntimeException("Failed to create constraint: " + constraintClass.getName(), e);
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
  public <T extends BehaviorAction<?>> void addAction(Class<T> actionClass) {
    try {
      T action = actionClass.getDeclaredConstructor().newInstance();
      action.setBehavior(this);
      actions.add(action);
    } catch (Exception e) {
      LOGGER.error("Failed to create action: {}", actionClass.getName(), e);
      throw new RuntimeException("Failed to create action: " + actionClass.getName(), e);
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

