package oogasalad.view.gui.deserializedFieldInput;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import oogasalad.model.engine.base.serialization.SerializedField;

public class BooleanFieldInput extends DeserializedFieldUI<Boolean> {

  @Override
  protected Node showGUI(SerializedField field) {
    String name = field.getFieldName().replaceAll("([a-z])([A-Z])", "$1 $2");
    name = name.substring(0, 1).toUpperCase() + name.substring(1);
    Label label = new Label(name);

    CheckBox checkBox = new CheckBox();
    checkBox.setSelected(Boolean.TRUE.equals(field.getValue()));
    checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> field.setValue(newVal));

    HBox hBox = new HBox(10, label, checkBox);
    hBox.setAlignment(Pos.CENTER_LEFT);
    HBox.setHgrow(hBox, Priority.ALWAYS);
    HBox.setHgrow(checkBox, Priority.ALWAYS);

    return hBox;
  }

}
