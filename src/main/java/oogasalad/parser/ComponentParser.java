package oogasalad.parser;

import static oogasalad.config.GameConfig.LOGGER;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import oogasalad.engine.base.architecture.GameComponent;


public class ComponentParser implements Parser<GameComponent> {

  @Override
  public GameComponent parse(JsonNode node) throws ParsingException {
    try {
      String componentName = node.get("componentName").asText();
      JsonNode configuations = node.get("Configurations");

      Class<? extends GameComponent> componentClass = ComponentFactory.getComponentClass(componentName);
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
