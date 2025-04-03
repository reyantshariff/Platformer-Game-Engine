package oogasalad.parser;

import static oogasalad.config.GameConfig.LOGGER;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.Serial;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.serialization.Serializable;
import oogasalad.engine.base.serialization.SerializableField;
import oogasalad.engine.base.serialization.SerializedField;

/**
 * Parses and serializes a GameComponent object to and from a JSON node
 *
 * @author Justin Aronwald
 */
public class ComponentParser implements Parser<GameComponent>, Serializable {

  /**
   * Parses a JSON node into a GameComponent instance
   *
   * @param componentNode - the JSON node given to parse
   * @return - a GameComponent that gets configured in the parent class
   * @throws ParsingException - error thrown if reflection or parsing fails
   */
  @Override
  public GameComponent parse(JsonNode componentNode) throws ParsingException {
    try {
      String name = componentNode.get("Name").asText();
      String fullClassName = "oogasalad.engine.component." + name;

      //had chatGpt help with the following three lines
      Class<?> rawClass = Class.forName(fullClassName);
      if (!GameComponent.class.isAssignableFrom(rawClass)) {
        throw new ParsingException("Class does not extend GameComponent: " + fullClassName);
      }

      Class<? extends GameComponent> componentClass = (Class<? extends GameComponent>) rawClass;

      return componentClass.getDeclaredConstructor().newInstance();
    } catch (ClassNotFoundException e) {
      throw new ParsingException("Component class not found: " + componentNode, e);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
             NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
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
    root.put("Name", componentName);

    ObjectNode configurations = root.putObject("Configurations");

    List<SerializedField<?>> serializableFields = data.getSerializedFields();
    for (SerializedField<?> serializedField : serializableFields) {
      if (serializedField.getFieldType() == String.class) {
        configurations.put(serializedField.getFieldName(), (String) (serializedField.getValue()));
      } else if (serializedField.getFieldType() == Integer.class) {
        configurations.put(serializedField.getFieldName(), (Integer) serializedField.getValue());
      }
    }

    return root;
  }
}
