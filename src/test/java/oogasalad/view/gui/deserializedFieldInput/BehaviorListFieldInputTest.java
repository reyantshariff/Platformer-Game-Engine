package oogasalad.view.gui.deserializedFieldInput;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oogasalad.model.engine.base.behavior.Behavior;
import oogasalad.model.engine.base.serialization.SerializedField;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

class BehaviorListFieldInputTest extends ApplicationTest {

  private SerializedField testField;
  private BehaviorListFieldInput fieldInput;

  @Override
  public void start(Stage stage) throws Exception {
    DummyTarget target = new DummyTarget();
    Field behaviorField = DummyTarget.class.getDeclaredField("behaviors");
    Method getter = DummyTarget.class.getDeclaredMethod("getBehaviors");
    Method setter = DummyTarget.class.getDeclaredMethod("setBehaviors", List.class);

    testField = new SerializedField(target, behaviorField, null, getter, setter);

    fieldInput = new BehaviorListFieldInput();
    VBox root = new VBox(fieldInput.showGUI(testField));
    stage.setScene(new Scene(root, 500, 400));
    stage.show();
  }

  @Test
  void clickAddButton_InitialState_ShouldAddBehaviorRow() {
    clickOn("+");
    waitForFxEvents();

    List<Behavior> updated = (List<Behavior>) testField.getValue();
    assertEquals(1, updated.size());
  }

  @Test
  void clickAddThenRemove_ShouldLeaveListEmpty() {
    clickOn("+");
    waitForFxEvents();

    clickOn("−"); // Removes the only behavior
    waitForFxEvents();

    List<Behavior> updated = (List<Behavior>) testField.getValue();
    assertTrue(updated.isEmpty());
  }

  // Dummy target class with List<Behavior>
  public static class DummyTarget {
    private List<Behavior> behaviors = new ArrayList<>();

    public List<Behavior> getBehaviors() {
      return behaviors;
    }

    public void setBehaviors(List<Behavior> behaviors) {
      this.behaviors = behaviors;
    }
  }
}