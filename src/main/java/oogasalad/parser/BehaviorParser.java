package oogasalad.parser;

import static oogasalad.config.GameConfig.LOGGER;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.component.Behavior;

// Assumes I am already within a behavior subsection at one of the array objects.
public class BehaviorParser implements Parser<GameComponent> {

  /**
   * @param node - the JSON node given to parse
   * @return A Behavior Game Component
   * @throws ParsingException indicating that there was an issue with parsing
   */
  @Override
  public Behavior parse(JsonNode behaviorNode) throws ParsingException {
    try {

      String name = behaviorNode.get("Name").asText();
      String fullClassName = "oogasalad.engine.component." + name;

      //had chatGpt help with the following three lines
      Class<?> rawClass = Class.forName(fullClassName);
      if (!Behavior.class.isAssignableFrom(rawClass)) {
        throw new ParsingException("Class does not extend Behavior: " + fullClassName);
      }

      Class<? extends Behavior> behaviorClass = (Class<? extends Behavior>) rawClass;

      return behaviorClass.getDeclaredConstructor().newInstance();

    } catch (ClassNotFoundException e) {
      LOGGER.warn("Behavior class not found: {}", behaviorNode.get("Name").asText(), e);
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
             NoSuchMethodException e) {
      LOGGER.warn("Could not instantiate Behavior class: {}", behaviorNode.get("Name").asText(), e);
    }
    return null;
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
