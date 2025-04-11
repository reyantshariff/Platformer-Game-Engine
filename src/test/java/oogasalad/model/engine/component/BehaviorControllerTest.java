package oogasalad.model.engine.component;

import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import oogasalad.model.engine.base.behavior.Behavior;
import oogasalad.model.engine.base.behavior.BehaviorAction;
import oogasalad.model.engine.base.behavior.BehaviorConstraint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BehaviorControllerTest {

  private BehaviorController controller;

  static class TestConstraint extends BehaviorConstraint<Boolean> {
    private final boolean value;

    public TestConstraint(boolean value) {
      this.value = value;
    }

    /**
     * The type of the constraint. This is used to classify the constraints being checked. Note:
     * This method MUST be override.
     *
     * @return the type of the constraint
     */
    @Override
    public ComponentTag ConstraintType() {
      return null;
    }

    /**
     * Check if the constraint is met.
     *
     * @param parameter the parameter to check against
     * @return true if the constraint is met, false otherwise
     */
    @Override
    protected boolean check(Boolean parameter) {
      return value;
    }
  }

  static class FlagAction extends BehaviorAction<Boolean> {
    private boolean executed = false;

    public boolean wasExecuted() {
      return executed;
    }

    /**
     * The type of the constraint. This is used to classify the constraints being checked. Note:
     * This method MUST be override.
     *
     * @return the type of the constraint
     */
    @Override
    public ComponentTag ActionType() {
      return null;
    }

    /**
     * Performs the action
     *
     * @param parameter the parameter for the action
     */
    @Override
    protected void perform(Boolean parameter) {
      executed = true;
    }
  }

  @BeforeEach
  void setup() {
    Game game = new Game();
    GameScene scene = new GameScene("BehaviorTestScene");
    game.addScene(scene);

    GameObject obj = new GameObject("TestObj", "tag");
    controller = obj.addComponent(BehaviorController.class);
    scene.registerObject(obj);
  }

  @Test
  void componentTag_Always_ReturnsBehaviorTag() {
    assertEquals(ComponentTag.BEHAVIOR, controller.componentTag());
  }

  @Test
  void addBehavior_WithDefaultConstructor_RegistersBehavior() {
    Behavior behavior = controller.addBehavior();
    assertNotNull(behavior);
  }

  @Test
  void addBehavior_WithExistingBehavior_AddsAndTriggersCorrectly() {
    FlagAction action = new FlagAction();

    Behavior behavior = new Behavior("custom");
    behavior.setBehaviorController(controller);
    behavior.addAction(action);
    behavior.addConstraint(new TestConstraint(true));

    controller.addBehavior(behavior);
    controller.update(1.0);

    assertTrue(action.wasExecuted());
  }

  @Test
  void update_BehaviorFailsConstraint_DoesNotTriggerAction() {
    FlagAction action = new FlagAction();

    Behavior behavior = controller.addBehavior();
    behavior.addAction(action);
    behavior.addConstraint(new TestConstraint(false)); // fail the constraint

    controller.update(1.0);

    assertFalse(action.wasExecuted());
  }

  @Test
  void update_NoConstraints_AlwaysExecutesAction() {
    FlagAction action = new FlagAction();

    Behavior behavior = controller.addBehavior();
    behavior.addAction(action); // no constraint

    controller.update(1.0);

    assertTrue(action.wasExecuted());
  }

  @Test
  void addSameBehaviorMultipleTimes_OnlyAddedOnce() {
    Behavior behavior = new Behavior("shared");
    behavior.setBehaviorController(controller);
    controller.addBehavior(behavior);
    controller.addBehavior(behavior); // duplicate add

    FlagAction action = new FlagAction();
    behavior.addAction(action);

    controller.update(1.0);

    assertTrue(action.wasExecuted()); // but only once
  }
}
