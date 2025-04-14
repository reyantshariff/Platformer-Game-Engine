package oogasalad.model.parser;

import static oogasalad.model.config.GameConfig.LOGGER;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.serialization.Serializable;
import oogasalad.model.engine.base.serialization.SerializedField;

/**
 * Parses and serializes a GameComponent object to and from a JSON node
 *
 * @author Justin Aronwald, Daniel Rodriguez-Florido
 */
public class ComponentParser implements Parser<GameComponent>, Serializable {

  private static final String NAME = "Name";
  private static final String CLASS_PATH = "oogasalad.model.engine.component.";

  private static final Map<Class<?>, Function<JsonNode, Object>> EXTRACTORS = new HashMap<>();

  static {
    EXTRACTORS.put(int.class, JsonNode::asInt);
    EXTRACTORS.put(Integer.class, JsonNode::asInt);
    EXTRACTORS.put(double.class, JsonNode::asDouble);
    EXTRACTORS.put(Double.class, JsonNode::asDouble);
    EXTRACTORS.put(boolean.class, JsonNode::asBoolean);
    EXTRACTORS.put(Boolean.class, JsonNode::asBoolean);
    EXTRACTORS.put(String.class, JsonNode::asText);

    // Handle List<String>
    EXTRACTORS.put(List.class, node -> {
      if (!node.isArray()) {
        throw new IllegalArgumentException("Expected JSON array for List<String> but got: " + node);
      }
      List<String> list = new ArrayList<>();
      for (JsonNode element : node) {
        if (!element.isTextual()) {
          throw new IllegalArgumentException("Expected string in List<String>, but found: " + element);
        }
        list.add(element.asText());
      }
      return list;
    });
  }

  /**
   * Parses a JSON node into a GameComponent instance
   *
   * @param componentNode - the JSON node given to parse
   * @return - a GameComponent that gets configured in the parent class
   * @throws ParsingException - error thrown if reflection or parsing fails
   */
  @Override
  public GameComponent parse(JsonNode componentNode) throws ParsingException {
    validateComponentName(componentNode);

    String name = componentNode.get(NAME).asText();
    try {
      String fullClassName = CLASS_PATH + name;

      Class<?> rawClass = getRawClass(fullClassName);

      Class<? extends GameComponent> componentClass = (Class<? extends GameComponent>) rawClass;

      GameComponent myComponent = componentClass.getDeclaredConstructor().newInstance();
      JsonNode configNode = componentNode.get("Configurations");

      myComponent
          .getSerializedFields()
          .forEach(field -> setFieldFromConfig(configNode, field));

      return myComponent;

    } catch (ClassNotFoundException e) {
      throw new ParsingException("Component class not found: " + componentNode, e);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
             NoSuchMethodException e) {
      LOGGER.error("Error instantiating component: {}", name);
      throw new ParsingException("Error instantiating component", e);
    }
  }

  private Class<?> getRawClass(String fullClassName)
      throws ClassNotFoundException, ParsingException {
    //had chatGpt help with the following three lines
    Class<?> rawClass = Class.forName(fullClassName);
    if (!GameComponent.class.isAssignableFrom(rawClass)) {
      throw new ParsingException("Class does not extend GameComponent: " + fullClassName);
    }
    return rawClass;
  }

  private void validateComponentName(JsonNode componentNode) throws ParsingException {
    if (!componentNode.has(NAME)) {
      LOGGER.error("Did not find component name. Throwing exception.");
      throw new ParsingException("Component did not have name.");
    }
  }

  private void setFieldFromConfig(JsonNode config, SerializedField<?> serializedField) {
    String fieldName = serializedField.getFieldName();

    if (!config.has(fieldName)) {
      return;
    }
    JsonNode valueNode = config.get(fieldName);
    Class<?> fieldType = serializedField.getFieldType();

    try {
      Object value = extractFieldValue(fieldType, valueNode);

      SerializedField<Object> typedField = (SerializedField<Object>) serializedField;
      typedField.setValue(value);

    } catch (IllegalArgumentException | ClassCastException e) {
      throw new IllegalStateException(
          "Failed to set field '" + fieldName + "' with value: " + valueNode, e);
    }
  }

  private Object extractFieldValue(Class<?> fieldType, JsonNode valueNode) {
    // Check if the field is a List (for example, a List<String>)
    if (List.class.isAssignableFrom(fieldType)) {
      // Create a list and populate it by iterating over the array node.
      List<String> list = new ArrayList<>();
      if (valueNode.isArray()) {
        for (JsonNode element : valueNode) {
          list.add(element.asText());
        }
      }
      return list;
    }

    Function<JsonNode, Object> extractor = EXTRACTORS.get(fieldType);
    if (extractor == null) {
      throw new IllegalArgumentException("Unsupported field type: " + fieldType);
    }
    return extractor.apply(valueNode);
  }


  /**
   * Serializes a gameComponent into a JSON node
   *
   * @param data - the data object to serialize
   * @return - a JSON node with the component's data
   */
  @Override
  public JsonNode write(GameComponent data) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    String componentName = data.getClass().getSimpleName();
    ObjectNode root = mapper.createObjectNode();
    root.put(NAME, componentName);

    ObjectNode configurations = root.putObject("Configurations");

    List<SerializedField<?>> serializableFields = data.getSerializedFields();
    for (SerializedField<?> serializedField : serializableFields) {
      serializeField(serializedField, configurations, mapper);
    }

    return root;
  }

  private static final Map<Class<?>, BiConsumer<SerializedField<?>, ObjectNode>> SERIALIZERS = new HashMap<>();

  static {
    SERIALIZERS.put(String.class, (field, config) -> config.put(field.getFieldName(), (String) field.getValue()));
    SERIALIZERS.put(Integer.class, (field, config) -> config.put(field.getFieldName(), (Integer) field.getValue()));
    SERIALIZERS.put(int.class, (field, config) -> config.put(field.getFieldName(), (Integer) field.getValue()));
    SERIALIZERS.put(Double.class, (field, config) -> config.put(field.getFieldName(), (Double) field.getValue()));
    SERIALIZERS.put(double.class, (field, config) -> config.put(field.getFieldName(), (Double) field.getValue()));
    SERIALIZERS.put(List.class, (field, config) -> serializeListField(field, config));  // Use a separate list serializer
  }

  private void serializeField(SerializedField<?> serializedField, ObjectNode configurations, ObjectMapper mapper) {
    Class<?> fieldType = serializedField.getFieldType();
    BiConsumer<SerializedField<?>, ObjectNode> serializer = SERIALIZERS.get(fieldType);

    if (serializer != null) {
      serializer.accept(serializedField, configurations);
    } else {
      throw new IllegalArgumentException("Unsupported field type: " + fieldType);
    }
  }

  private static void serializeListField(SerializedField<?> serializedField, ObjectNode configurations) {
    ParameterizedType pt = (ParameterizedType) serializedField.getFieldGenericType();
    Type argType = pt.getActualTypeArguments()[0];

    if (argType == String.class) {
      ArrayNode arrayNode = new ObjectMapper().valueToTree(serializedField.getValue());
      configurations.set(serializedField.getFieldName(), arrayNode);
    } else {
      throw new IllegalArgumentException("Unsupported List type: " + argType);
    }
  }
}
