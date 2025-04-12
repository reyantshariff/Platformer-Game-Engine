package oogasalad.view.gui.deserializedFieldInput;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.util.converter.DoubleStringConverter;
import oogasalad.model.engine.base.serialization.SerializedField;

public class DoubleFieldInput extends DeserializedFieldUI<Double> {

  @Override
  protected Node showGUI(SerializedField<Double> field) {
    // Make a label for the field
    String name = field.getFieldName().replaceAll("([a-z])([A-Z])", "$1 $2");
    name = name.substring(0, 1).toUpperCase() + name.substring(1);
    Label label = new Label(name);

    // Make a text field for the field that only accepts double values
    TextField textField = new TextField();
    textField.setPromptText("Enter a number");
    TextFormatter<Double> formatter = new TextFormatter<>(
        new DoubleStringConverter(),
        field.getValue(),
        change -> {
          String newText = change.getControlNewText();
          if (newText.isEmpty()) return change;
          try {
            Double.parseDouble(newText);
            return change;
          } catch (NumberFormatException e) {
            return null;
          }
        }
    );
    textField.setTextFormatter(formatter);

    // Listeners
    formatter.valueProperty().addListener((obs, oldVal, newVal) -> {
      field.setValue(newVal);
    });
    formatter.valueProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal != null) {
        field.setValue(newVal);
      } else {
        field.setValue(null);
      }
    });

    // Container for the label and text field
    HBox hBox = new HBox(10, label, textField);
    hBox.setAlignment(Pos.CENTER_LEFT);
    hBox.setPadding(new Insets(5));

    return hBox;
  }

}
