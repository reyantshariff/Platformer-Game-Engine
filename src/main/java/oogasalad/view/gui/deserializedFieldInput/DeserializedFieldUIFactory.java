package oogasalad.view.gui.deserializedFieldInput;

import java.util.Map;
import java.util.function.Function;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import oogasalad.model.engine.base.behavior.BehaviorAction;
import oogasalad.model.engine.base.behavior.BehaviorComponent;
import oogasalad.model.engine.base.behavior.BehaviorConstraint;
import oogasalad.model.engine.base.serialization.SerializableFieldType;
import oogasalad.model.engine.base.serialization.SerializedField;

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
 * SerializedField field = ...; // Obtain a SerializedField instance
 * HBox uiComponent = DeserializedFieldUIFactory.createDeserializedFieldUI(field);
 * </pre>
 *
 * <p>This class is designed to be extensible by adding new field input factories
 * for additional types as needed.</p>
 */

public class DeserializedFieldUIFactory {

  private static final String BEHAVIOR_ACTION_PACKAGE = "oogasalad.model.engine.action";
  private static final String BEHAVIOR_CONSTRAINT_PACKAGE = "oogasalad.model.engine.constraint";
  private static final Map<SerializableFieldType, Function<SerializedField, HBox>> FIELD_UI_CREATORS = Map.of(
      SerializableFieldType.STRING, f -> new StringFieldInput(),
      SerializableFieldType.DOUBLE, f -> new DoubleFieldInput(),
      SerializableFieldType.BOOLEAN, f -> new BooleanFieldInput(),
      SerializableFieldType.LIST_STRING, f -> new StringListFieldInput(),
      SerializableFieldType.LIST_BEHAVIOR, f -> new BehaviorListFieldInput(),
      SerializableFieldType.LIST_BEHAVIOR_ACTION, f -> new BehaviorComponentListFieldInput<>(BEHAVIOR_ACTION_PACKAGE, BehaviorAction.class),
      SerializableFieldType.LIST_BEHAVIOR_CONSTRAINT, f -> new BehaviorComponentListFieldInput<>(BEHAVIOR_CONSTRAINT_PACKAGE, BehaviorConstraint.class)
  );
  
  /**
   * Creates a UI component (HBox) for a given SerializedField based on its type.
   *
   * @param field the SerializedField for which the UI component is to be created
   * @return an HBox representing the UI for the given SerializedField
   * @throws IllegalStateException if the field type is unsupported
   */
  public static HBox createDeserializedFieldUI(SerializedField field) {
    Function<SerializedField, HBox> creator = FIELD_UI_CREATORS.get(field.getFieldType());
    HBox box = (creator != null) ? creator.apply(field) : new HBox();

    if (box instanceof DeserializedFieldUI<?> dBox) {
      dBox.initGUI(field);
    }

    return box;
  }

}