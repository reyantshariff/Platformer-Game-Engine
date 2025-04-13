package oogasalad.view.gui.deserializedFieldInput;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javafx.scene.layout.HBox;
import oogasalad.model.engine.base.serialization.SerializedField;

public class DeserializedFieldUIFactory {

  public static HBox createDeserializedFieldUI(SerializedField<?> field) {
    String fieldType = field.getFieldType().getSimpleName();

    if (fieldType.equals("List")) {
      return createListInputBox(field);
    }

    return createSimpleInputBox(fieldType, field);
  }

  private static HBox createSimpleInputBox(String fieldType, SerializedField<?> field) {
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
      default -> throw new IllegalStateException("Unsupported field type: " + fieldType);
    }
  }

  private static HBox createListInputBox(SerializedField<?> field) {
    Type genericType = field.getFieldGenericType();

    if (!(genericType instanceof ParameterizedType pt)) {
      throw new IllegalStateException("Expected ParameterizedType for List");
    }

    String simpleName = pt.getActualTypeArguments()[0].getTypeName().replaceAll(".*\\.", "");

    return switch (simpleName) {
      case "String" -> {
        StringListFieldInput box = new StringListFieldInput();
        box.initGUI(field);
        yield box;
      }
      case "Behavior" -> {
        BehaviorListFieldInput box = new BehaviorListFieldInput();
        box.initGUI(field);
        yield box;
      }
      case "BehaviorConstraint<?>" -> {
        ConstraintListFieldInput box = new ConstraintListFieldInput();
        box.initGUI(field);
        yield box;
      }
      case "BehaviorAction<?>" -> {
        ActionListFieldInput box = new ActionListFieldInput();
        box.initGUI(field);
        yield box;
      }
      default -> throw new IllegalStateException("Unsupported List generic type: " + simpleName);
    };
  }
}
