package oogasalad.view.gui.deserializedFieldInput;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import oogasalad.model.engine.base.serialization.SerializedField;
import oogasalad.view.gui.textField.DoubleTextField;

/**
 * A class that provides a JavaFX UI component for editing a double field in a serialized object. It
 * extends the DeserializedFieldUI class and implements the showGUI method to create the UI. The UI
 * consists of a label and a text field that only accepts double values.
 */
public class DoubleFieldInput extends DeserializedFieldUI<Double> {

  @Override
  protected Node showGUI(SerializedField field) {
    // Container for the label and text field
    HBox hBox = new HBox();
    hBox.setAlignment(Pos.CENTER_LEFT);
    hBox.setSpacing(10);

    // Make a text field for the field that only accepts double values
    DoubleTextField textField = new DoubleTextField((Double) field.getValue(), "Enter a double value");
    textField.setChangeListener(newVal -> {
      field.setValue(newVal);
      return true;
    });

    hBox.getChildren().add(createLabel(field));
    hBox.getChildren().add(textField);
    HBox.setHgrow(hBox, Priority.ALWAYS);
    HBox.setHgrow(textField, Priority.ALWAYS);

    return hBox;
  }

}
