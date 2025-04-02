package oogasalad.parser;

import static oogasalad.config.GameConfig.LOGGER;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import oogasalad.engine.base.architecture.GameComponent;


public class ComponentParser implements Parser<GameComponent> {

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
    return null;
  }
}
