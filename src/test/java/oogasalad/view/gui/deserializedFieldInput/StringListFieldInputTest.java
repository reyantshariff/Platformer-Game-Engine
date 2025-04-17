package oogasalad.view.gui.deserializedFieldInput;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oogasalad.model.engine.base.serialization.SerializedField;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

class StringListFieldInputTest extends ApplicationTest {

  private SerializedField testField;

  @Override
  public void start(Stage stage) throws Exception {
    StringListTarget target = new StringListTarget();
    Field listField = StringListTarget.class.getDeclaredField("values");
    Method getter = StringListTarget.class.getDeclaredMethod("getValues");
    Method setter = StringListTarget.class.getDeclaredMethod("setValues", List.class);

    testField = new SerializedField(target, listField, null, getter, setter);

    StringListFieldInput fieldInput = new StringListFieldInput();
    VBox root = new VBox(fieldInput.showGUI(testField));
    stage.setScene(new Scene(root, 400, 300));
    stage.show();
  }

  @Test
  void clickOnAddButton_InitialState_ShouldAddOneTextField() {
    clickOn("+");
    waitForFxEvents();
    assertEquals(1, lookup(".text-field").queryAll().size());
  }

  @Test
  void clickAddThenTypeAndRemoveTextField_ShouldUpdateSerializedFieldToEmpty() {
    clickOn("+");
    waitForFxEvents();

    TextField tf = lookup(".text-field").nth(0).query();
    clickOn(tf);
    write("hello");

    clickOn("−");
    waitForFxEvents();

    List<String> updated = (List<String>) testField.getValue();
    assertTrue(updated.isEmpty());
  }

  @Test
  void clickAddTwiceAndTypeValidItems_ShouldUpdateSerializedFieldWithTwoItems() {
    clickOn("+");
    waitForFxEvents();
    clickOn((TextField) lookup(".text-field").nth(0).query());
    write("apple");

    clickOn("+");
    waitForFxEvents();
    clickOn((TextField) lookup(".text-field").nth(1).query());
    write("banana");

    List<String> updated = (List<String>) testField.getValue();
    assertEquals(List.of("apple", "banana"), updated);
  }

  // Dummy target for serialized field
  public static class StringListTarget {
    private List<String> values = new ArrayList<>();
    public List<String> getValues() { return values; }
    public void setValues(List<String> values) { this.values = values; }
  }
}