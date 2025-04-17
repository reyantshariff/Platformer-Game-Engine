package oogasalad.view.gui.deserializedFieldInput;

import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oogasalad.model.engine.base.behavior.BehaviorComponent;
import oogasalad.model.engine.base.serialization.SerializedField;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;

//ChatGPT assisted
class BehaviorComponentListFieldInputTest extends ApplicationTest {

  private SerializedField testField;
  private BehaviorComponentListFieldInput<TestComponent> fieldInput;

  @Override
  public void start(Stage stage) throws Exception {
    DummyTarget target = new DummyTarget();
    Field listField = DummyTarget.class.getDeclaredField("components");
    Method getter = DummyTarget.class.getDeclaredMethod("getComponents");
    Method setter = DummyTarget.class.getDeclaredMethod("setComponents", List.class);

    testField = new SerializedField(target, listField, null, getter, setter);

    fieldInput = new BehaviorComponentListFieldInput<>(
        this.getClass().getName(),
        TestComponent.class
    );

    VBox root = new VBox(fieldInput.showGUI(testField));
    stage.setScene(new Scene(root, 600, 400));
    stage.show();
  }

  @Test
  void clickOnAddButton_NoDropdownSelection_ListShouldRemainEmpty() {
    clickOn("+");
    waitForFxEvents();
    assertEquals(0, ((List<?>) testField.getValue()).size());
  }

  @Test
  void clickOnAddButton_ThenClickRemoveButton_ShouldClearList() {
    clickOn("+");
    waitForFxEvents();
    selectFirstComponentFromDropdown();
    waitForFxEvents();

    clickOn("−");
    waitForFxEvents();

    assertTrue(((List<?>) testField.getValue()).isEmpty());
  }

  // 👇 Simulates selecting the first dropdown item
  private void selectFirstComponentFromDropdown() {
    ComboBox<?> comboBox = lookup(".combo-box").query();
    clickOn(comboBox);
    type(DOWN);
    type(ENTER);
  }

  // ✅ Dummy target with List<TestComponent>
  public static class DummyTarget {
    private List<TestComponent> components = new ArrayList<>();
    public List<TestComponent> getComponents() { return components; }
    public void setComponents(List<TestComponent> components) { this.components = components; }
  }

  // ✅ Dummy BehaviorComponent with a single String param
  public static class TestComponent extends BehaviorComponent<String> {
    private String value = "";
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    @Override
    public List<SerializedField> getSerializedFields() {
      try {
        Field f = TestComponent.class.getDeclaredField("value");
        Method getter = TestComponent.class.getDeclaredMethod("getValue");
        Method setter = TestComponent.class.getDeclaredMethod("setValue", String.class);
        return List.of(new SerializedField(this, f, null, getter, setter));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    protected String defaultParameter() {
      return "";
    }
  }
}