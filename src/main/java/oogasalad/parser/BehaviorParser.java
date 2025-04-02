package oogasalad.parser;

import static oogasalad.config.GameConfig.LOGGER;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import oogasalad.engine.base.architecture.GameComponent;

// Assumes I am already within a behavior subsection at one of the array objects.
public class BehaviorParser implements Parser<GameComponent> {

  /**
   * @param node - the JSON node given to parse
   * @return A Behavior Game Component
   * @throws ParsingException indicating that there was an issue with parsing
   */
  @Override
  public GameComponent parse(JsonNode node) throws ParsingException {
    try {
      String behaviorName = node.get("Name").asText();
      JsonNode configurations = node.get("Configurations");

      Class<? extends GameComponent> behaviorClass = ComponentFactory.getComponentClass(behaviorName);
      GameComponent gameComponent = ComponentFactory.create(behaviorClass);

      gameComponent.initializeFromJson(configurations);

      return gameComponent;
    } catch (ParsingException e) {
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * @param data - the data object to serialize
   * @return
   * @throws IOException
   */
  @Override
  public JsonNode write(GameComponent data) throws IOException {
    return null;
  }
}
