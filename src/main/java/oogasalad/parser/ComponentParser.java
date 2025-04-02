package oogasalad.parser;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import oogasalad.engine.base.architecture.GameComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ComponentParser implements Parser<GameComponent> {
  public static final Logger LOGGER = LogManager.getLogger();

  @Override
  public GameComponent parse(JsonNode node) throws ParsingException {
    try {
      String componentName = node.get("componentName").asText();
      JsonNode configuations = node.get("Configurations");

      Class<? extends GameComponent> componentClass = ComponentFactory.getComponentClass(name);
      GameComponent gameComponent = ComponentFactory.create(componentClass);

      gameComponent.initializeFromJson(configuations);
      return gameComponent;
    } catch (ParsingException e) {
      LOGGER.error(e.getMessage());
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
