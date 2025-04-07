package oogasalad.parser;

import static oogasalad.config.GameConfig.LOGGER;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.behavior.Behavior;

// Assumes I am already within a behavior subsection at one of the array objects.
/**
 * Parses and serializes a Behavior object to and from a JSON node
 *
 * @author Justin Aronwald, Daniel Radriguez-Florido
 */
public class BehaviorParser implements Parser<GameComponent> {

  private final ComponentParser componentParser = new ComponentParser();

  private static final String NAME = "Name";

  /**
   * @param behaviorNode - the JSON node given to parse
   * @return A Behavior Game Component
   * @throws ParsingException indicating that there was an issue with parsing
   */
  @Override
  public Behavior parse(JsonNode behaviorNode) throws ParsingException {
    if (!behaviorNode.has(NAME)) {
      LOGGER.error("No name found in Behavior Node. Throwing Error.");
      throw new ParsingException("Behavior does not have required name.");
    }

    try {
      String name = behaviorNode.get(NAME).asText();
      String fullClassName = "oogasalad.engine.component." + name;

      //had chatGpt help with the following three lines
      Class<?> rawClass = Class.forName(fullClassName);
      if (!Behavior.class.isAssignableFrom(rawClass)) {
        throw new ParsingException("Class does not extend Behavior: " + fullClassName);
      }

      Class<? extends Behavior> behaviorClass = (Class<? extends Behavior>) rawClass;

      return behaviorClass.getDeclaredConstructor().newInstance();

    } catch (ClassNotFoundException e) {
      LOGGER.error("Behavior class not found: {}", behaviorNode.get("Name").asText(), e);
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
             NoSuchMethodException e) {
      LOGGER.error("Could not instantiate Behavior class: {}", behaviorNode.get("Name").asText(), e);
    }

    LOGGER.error("Could not instantiate Behavior class: {}", behaviorNode.get("Name").asText());
    return null;
  }

  /**
   * Generates a serialized object to place into JSON
   *
   * @param data - the data object to serialize
   * @return JsonNode to append to the parent node
   */
  @Override
  public JsonNode write(GameComponent data) throws IOException {
    return componentParser.write(data);
  }
}
