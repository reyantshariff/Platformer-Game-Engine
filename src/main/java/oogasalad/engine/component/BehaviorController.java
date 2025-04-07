package oogasalad.engine.component;

import java.util.ArrayList;
import java.util.List;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.behavior.Behavior;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.serialization.SerializableField;

public final class BehaviorController extends GameComponent {
  @Override
  public ComponentTag componentTag() {
    return ComponentTag.BEHAVIOR;
  }

  @SerializableField
  private List<Behavior> behaviors = new ArrayList<>();

  @Override
  public void update(double deltaTime) {
    for (Behavior behavior : behaviors) {
      behavior.execute();
    }
  }

  /**
   * Add a behavior to the controller. This method is used to add a behavior to the controller.
   */
  public Behavior addBehavior() {
    Behavior behavior = new Behavior(this);
    behaviors.add(behavior);
    return behavior;
  }
}
