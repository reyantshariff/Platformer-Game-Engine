package oogasalad.view.gui.deserializedFieldInput;

import javafx.scene.layout.HBox;
import oogasalad.model.engine.base.serialization.SerializedField;

public class DeserializedFieldUIFactory {

  public static HBox createDeserializedFieldUI(SerializedField<?> field) {
    String fieldType = field.getFieldType().getSimpleName();

    switch (fieldType) {
      case "Double", "double" -> {
        DoubleFieldInput box = new DoubleFieldInput();
        box.initGUI(field);
        return box;
      }
      case "String", "string" -> {
        StringFieldInput box = new StringFieldInput();
        box.initGUI(field);
        return box;
      }
      case "List" -> {
        ListFieldInput box = new ListFieldInput();
        box.initGUI(field);
        return box;
      }
      default -> {
        throw new IllegalStateException("Unexpected value: " + fieldType);
      }
    }
  }

}
