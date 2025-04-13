package oogasalad.view.gui.deserializedFieldInput;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Function;
import javafx.scene.layout.HBox;
import oogasalad.model.engine.base.serialization.SerializedField;
import java.util.Map;

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

  private static final Map<String, Function<SerializedField<?>, HBox>> listFieldInputFactories = Map.of(
      "String", field -> {
        StringListFieldInput box = new StringListFieldInput();
        box.initGUI(field);
        return box;
      },
      "Behavior", field -> {
        BehaviorListFieldInput box = new BehaviorListFieldInput();
        box.initGUI(field);
        return box;
      },
      "BehaviorConstraint<?>", field -> {
        ConstraintListFieldInput box = new ConstraintListFieldInput();
        box.initGUI(field);
        return box;
      },
      "BehaviorAction<?>", field -> {
        ActionListFieldInput box = new ActionListFieldInput();
        box.initGUI(field);
        return box;
      }
  );

  private static HBox createListInputBox(SerializedField<?> field) {
    Type genericType = field.getFieldGenericType();

    if (!(genericType instanceof ParameterizedType pt)) {
      throw new IllegalStateException("Expected ParameterizedType for List");
    }

    String simpleName = pt.getActualTypeArguments()[0].getTypeName().replaceAll(".*\\.", "");

    Function<SerializedField<?>, HBox> factory = listFieldInputFactories.get(simpleName);
    if (factory == null) {
      throw new IllegalStateException("Unsupported List generic type: " + simpleName);
    }

    return factory.apply(field);
  }

}
