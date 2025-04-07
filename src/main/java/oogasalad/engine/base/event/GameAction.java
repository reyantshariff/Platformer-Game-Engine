package oogasalad.engine.base.event;

import java.util.ArrayList;
import java.util.List;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.component.InputHandler;

/**
 * The GameAction class is the base class for all game actions. Actions are event that are called
 * only under certain conditions. For example, a player will only jump when the user presses the
 * jump button.
 *
 * @author Hsuan-Kai Liao, Christian Bepler
 */
public abstract class GameAction {

  private final List<GameActionConstraint> constraints;

  private InputHandler parent; // NOTE: This is set using reflection

  public GameAction() {
    this.constraints = new ArrayList<>();
  }

  /**
   * This is triggered only when the registered key pressed and the constraints are met
   */
  public abstract void dispatch();

  /**
   * Get the parent object of the action
   *
   * @return the parent object of the action
   */
  public final GameObject getParent() {
    return parent.getParent();
  }

  public final <T extends GameActionConstraint> void registerConstraint(Class<T> constraintClass) {
    try {
      T constraint = constraintClass.getDeclaredConstructor().newInstance();
      constraint.setParent(this);
      constraints.add(constraint);
    } catch (Exception e) {
      throw new RuntimeException("Failed to register constraint", e);
    }
  }

  public final <T extends GameActionConstraint> void unregisterConstraint(
      Class<T> constraintClass) {
    for (GameActionConstraint constraint : constraints) {
      if (constraint.getClass().equals(constraintClass)) {
        constraints.remove(constraint);
        return;
      }
    }
    throw new IllegalArgumentException("Constraint does not exist");
  }

  /**
   * Check all constraints of the action
   *
   * @return true if all constraints are met, false otherwise
   */
  public final boolean checkConstraints() {
    for (GameActionConstraint constraint : constraints) {
      if (!constraint.check()) {
        return false;
      }
    }
    return true;
  }
}
