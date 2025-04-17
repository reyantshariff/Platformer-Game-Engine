package oogasalad.view.gui.deserializedFieldInput;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import oogasalad.model.engine.base.serialization.SerializedField;

/**
 * A class that provides a JavaFX UI component for editing a string field in a serialized object. It
 * extends the DeserializedFieldUI class and implements the showGUI method to create the UI. The UI
 * consists of a label and a text field that only accepts string values.
 */
public class BooleanFieldInput extends DeserializedFieldUI<Boolean> {

  @Override
  protected Node showGUI(SerializedField field) {
    CheckBox checkBox = new CheckBox();
    checkBox.setSelected(Boolean.TRUE.equals(field.getValue()));
    checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> field.setValue(newVal));

    HBox hBox = new HBox(10, createLabel(field), checkBox);
    hBox.setAlignment(Pos.CENTER_LEFT);
    HBox.setHgrow(hBox, Priority.ALWAYS);
    HBox.setHgrow(checkBox, Priority.ALWAYS);

    return hBox;
  }

}
