package oogasalad.model.engine.component;

import java.util.ArrayList;
import java.util.List;
import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.behavior.Behavior;
import oogasalad.model.engine.base.serialization.SerializableField;

/**
 * The BehaviorController class is a component that manages the behaviors of a game object. It is
 * responsible for executing the behaviors and managing the constraints and actions associated with
 * each behavior.
 */

public final class BehaviorController extends GameComponent {

  @Override
  public ComponentTag componentTag() {
    return ComponentTag.BEHAVIOR;
  }

  @SerializableField
  private List<Behavior> behaviors = new ArrayList<>();

  @Override
  protected void awake() {
    for (Behavior behavior : behaviors) {
      behavior.setBehaviorController(this);
      behavior.awake();
    }
  }

  @Override
  protected void update(double deltaTime) {
    for (Behavior behavior : behaviors) {
      behavior.execute();
    }
  }

  /**
   * Add a behavior to the controller. This method is used to add a behavior to the controller.
   */
  public Behavior addBehavior() {
    Behavior behavior = new Behavior();
    behaviors.add(behavior);
    return behavior;
  }

  /**
   * Overloaded behavior method to add
   *
   * @param behavior is the fully qualified behavior that is to be added to the behaviors list
   */
  public void addBehavior(Behavior behavior) {
    behaviors.add(behavior);
  }

  /**
   * Getter for the behavior objects
   *
   * @return - a list of behavior objects
   */
  public List<Behavior> getBehaviors() {
    return behaviors;
  }

}
