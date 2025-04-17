package oogasalad.view.gui.deserializedFieldInput;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oogasalad.model.engine.base.serialization.SerializedField;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

class DoubleFieldInputTest extends ApplicationTest {

  private SerializedField testField;
  private DoubleFieldInput fieldInput;

  @Override
  public void start(Stage stage) throws Exception {
    DummyTarget target = new DummyTarget();
    Field doubleField = DummyTarget.class.getDeclaredField("price");
    Method getter = DummyTarget.class.getDeclaredMethod("getPrice");
    Method setter = DummyTarget.class.getDeclaredMethod("setPrice", Double.class);

    testField = new SerializedField(target, doubleField, null, getter, setter);

    fieldInput = new DoubleFieldInput();
    VBox root = new VBox(fieldInput.showGUI(testField));
    stage.setScene(new Scene(root, 400, 200));
    stage.show();
  }

  @Test
  void showGUI_WithValidField_ShouldShowLabelAndTextField() {
    Label label = lookup(".label").query();
    TextField tf = lookup(".text-field").query();

    assertEquals("Price", label.getText());
    assertEquals("9.99", tf.getText());
  }

  @Test
  void typeInTextField_NewValidDouble_ShouldUpdateSerializedField() {
    TextField tf = lookup(".text-field").query();
    clickOn(tf).eraseText(3).write("125");
    clickOn(".label");
    waitForFxEvents();

    assertEquals(9125, (Double) testField.getValue(), 0.001);
  }

  // Dummy target object
  public static class DummyTarget {
    private Double price = 9.99;

    public Double getPrice() {
      return price;
    }

    public void setPrice(Double price) {
      this.price = price;
    }
  }
}