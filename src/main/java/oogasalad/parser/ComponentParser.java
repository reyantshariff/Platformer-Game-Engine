package oogasalad.parser;

import static oogasalad.config.GameConfig.LOGGER;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.print.attribute.standard.MediaSize.NA;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.serialization.Serializable;
import oogasalad.engine.base.serialization.SerializedField;

/**
 * Parses and serializes a GameComponent object to and from a JSON node
 *
 * @author Justin Aronwald, Daniel Rodriguez-Florido
 */
public class ComponentParser implements Parser<GameComponent>, Serializable {
  private static final String NAME = "Name";

  private static final Map<Class<?>, Function<JsonNode, Object>> EXTRACTORS = new HashMap<>();

  static {
    EXTRACTORS.put(int.class, JsonNode::asInt);
    EXTRACTORS.put(Integer.class, JsonNode::asInt);
    EXTRACTORS.put(double.class, JsonNode::asDouble);
    EXTRACTORS.put(Double.class, JsonNode::asDouble);
    EXTRACTORS.put(boolean.class, JsonNode::asBoolean);
    EXTRACTORS.put(Boolean.class, JsonNode::asBoolean);
    EXTRACTORS.put(String.class, JsonNode::asText);
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
      String fullClassName = "oogasalad.engine.component." + name;

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
      throw new RuntimeException(e);
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

    if (!config.has(fieldName))
      return;
    JsonNode valueNode = config.get(fieldName);
    Class<?> fieldType = serializedField.getFieldType();

    try {
      Object value = extractFieldValue(fieldType, valueNode);

      SerializedField<Object> typedField = (SerializedField<Object>) serializedField;
      typedField.setValue(value);

    } catch (IllegalArgumentException | ClassCastException e) {
      throw new IllegalStateException("Failed to set field '" + fieldName + "' with value: " + valueNode, e);
    }
  }

  private Object extractFieldValue(Class<?> fieldType, JsonNode valueNode) {
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

    //Note: I tried my hardest to use reflection and something dynamic but Jackson library does not allow it.
    List<SerializedField<?>> serializableFields = data.getSerializedFields();
    for (SerializedField<?> serializedField : serializableFields) {
      if (serializedField.getFieldType() == String.class) {
        configurations.put(serializedField.getFieldName(), (String) (serializedField.getValue()));
      } else if (serializedField.getFieldType() == int.class) {
        configurations.put(serializedField.getFieldName(), (Integer) serializedField.getValue());
      } else if (serializedField.getFieldType() == double.class) {
        configurations.put(serializedField.getFieldName(), (Double) serializedField.getValue());
      }
    }

    return root;
  }
}
