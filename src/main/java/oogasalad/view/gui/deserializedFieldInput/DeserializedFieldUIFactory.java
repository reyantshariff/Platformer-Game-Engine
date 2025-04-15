package oogasalad.view.gui.deserializedFieldInput;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Function;
import javafx.scene.layout.HBox;
import oogasalad.model.engine.base.serialization.SerializedField;
import java.util.Map;

/**
 * A factory class for creating JavaFX UI components (HBox) for deserialized fields
 * based on their types. This class supports both simple field types (e.g., Double, String)
 * and complex field types such as Lists with specific generic types.
 *
 * <p>The factory uses reflection to determine the type of the field and dynamically
 * creates the appropriate UI component. For simple types, it directly maps the type
 * to a corresponding input box. For List types, it uses a map of factories to handle
 * specific generic types of the List.</p>
 *
 * <p>Unsupported field types will result in an {@code IllegalStateException} being thrown.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * SerializedField<?> field = ...; // Obtain a SerializedField instance
 * HBox uiComponent = DeserializedFieldUIFactory.createDeserializedFieldUI(field);
 * </pre>
 *
 * <p>This class is designed to be extensible by adding new field input factories
 * for additional types as needed.</p>
 */

public class DeserializedFieldUIFactory {

  /**
   * Creates a UI component (HBox) for a given SerializedField based on its type.
   *
   * @param field the SerializedField for which the UI component is to be created
   * @return an HBox representing the UI for the given SerializedField
   * @throws IllegalStateException if the field type is unsupported
   */
  public static HBox createDeserializedFieldUI(SerializedField<?> field) {
    String fieldType = field.getFieldType().getSimpleName();

    if (fieldType.equals("List")) {
      return createListInputBox(field);
    }

    return createSimpleInputBox(fieldType, field);
  }

  /**
   * Creates a UI component (HBox) for a simple field type (e.g., Double, String).
   *
   * @param fieldType the type of the field as a String
   * @param field the SerializedField for which the UI component is to be created
   * @return an HBox representing the UI for the given field
   * @throws IllegalStateException if the field type is unsupported
   */
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

  /**
   * Creates a UI component (HBox) for a field of type List with a specific generic type.
   *
   * @param field the SerializedField of type List for which the UI component is to be created
   * @return an HBox representing the UI for the given List field
   * @throws IllegalStateException if the generic type of the List is unsupported
   */
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