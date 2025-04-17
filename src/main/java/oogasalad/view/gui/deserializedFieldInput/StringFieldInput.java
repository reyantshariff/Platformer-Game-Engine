package oogasalad.view.gui.deserializedFieldInput;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import oogasalad.model.engine.base.serialization.SerializedField;
import oogasalad.view.gui.textField.StringTextField;

/**
 * A class that provides a JavaFX UI component for editing a string field in a serialized object.
 * It extends the DeserializedFieldUI class and implements the showGUI method to create the UI.
 * The UI consists of a label and a text field that only accepts string values.
 */
public class StringFieldInput extends DeserializedFieldUI<String> {

  @Override
  protected Node showGUI(SerializedField field) {
    // Make a label for the field
    String name = field.getFieldName().replaceAll("([a-z])([A-Z])", "$1 $2");
    name = name.substring(0, 1).toUpperCase() + name.substring(1);
    Label label = new Label(name);

    // Make a text field for the field that only accepts double values
    StringTextField textField = new StringTextField((String) field.getValue(), "Enter a string value");

    // Listeners
    textField.setChangeListener(newVal -> {
      field.setValue(newVal);
      return true;
    });

    // Container for the label and text field
    HBox hBox = new HBox(10, label, textField);
    HBox.setHgrow(hBox, Priority.ALWAYS);
    hBox.setAlignment(Pos.CENTER_LEFT);
    HBox.setHgrow(textField, Priority.ALWAYS);

    return hBox;
  }
}
