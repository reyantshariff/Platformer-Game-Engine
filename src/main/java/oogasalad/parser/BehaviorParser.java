package oogasalad.parser;

import static oogasalad.config.GameConfig.LOGGER;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import oogasalad.engine.base.behavior.Behavior;
import oogasalad.engine.base.behavior.BehaviorAction;
import oogasalad.engine.base.behavior.BehaviorConstraint;
import oogasalad.engine.base.serialization.SerializedField;

// Assumes I am already within a behavior subsection at one of the array objects.

/**
 * Parses and serializes a Behavior object to and from a JSON node
 *
 * @author Justin Aronwald, Daniel Radriguez-Florido
 */
public class BehaviorParser implements Parser<Behavior> {
  private final ObjectMapper mapper = new ObjectMapper();
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

      Class<?> rawClass = Class.forName(fullClassName);
      if (!Behavior.class.isAssignableFrom(rawClass)) {
        throw new ParsingException("Class does not extend Behavior: " + fullClassName);
      }

      Class<? extends Behavior> behaviorClass = (Class<? extends Behavior>) rawClass;
      Behavior behaviorInstance = behaviorClass.getDeclaredConstructor().newInstance();

      parseConstraints(behaviorNode, behaviorInstance);
      parseActions(behaviorNode, behaviorInstance);

      return behaviorInstance;

    } catch (ClassNotFoundException e) {
      LOGGER.error("Behavior class not found: {}", behaviorNode.get("Name").asText(), e);
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
             NoSuchMethodException e) {
      LOGGER.error("Could not instantiate Behavior class: {}", behaviorNode.get("Name").asText(),
          e);
    }
    LOGGER.error("Could not instantiate Behavior class: {}", behaviorNode.get("Name").asText());
    return null;
  }

  private void parseActions(JsonNode behaviorNode, Behavior behaviorInstance)
      throws ClassNotFoundException, ParsingException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    if (behaviorNode.has("actions")) {
      for (JsonNode actionNode : behaviorNode.get("actions")) {
        String aName = actionNode.get("name").asText();
        String aFullClassName = "oogasalad.engine.behavior.action." + aName;
        Class<?> clazz = Class.forName(aFullClassName);

        if (!BehaviorAction.class.isAssignableFrom(clazz)) {
          throw new ParsingException("Invalid action class: " + aFullClassName);
        }

        createAndAddAction(behaviorInstance, actionNode, clazz);
      }
    }
  }

  private void createAndAddAction(Behavior behaviorInstance, JsonNode actionNode, Class<?> clazz)
      throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    BehaviorAction<?> action = (BehaviorAction<?>) clazz.getDeclaredConstructor().newInstance();
    action.setBehavior(behaviorInstance);
    JsonNode config = actionNode.get("parameter");

    action
        .getSerializedFields()
        .forEach(field -> setFieldFromConfig(config, field));

    behaviorInstance.addAction(action.getClass());
  }

  private void parseConstraints(JsonNode behaviorNode, Behavior behaviorInstance)
      throws ClassNotFoundException, ParsingException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    if (behaviorNode.has("constraints")) {
      for (JsonNode constraintNode : behaviorNode.get("constraints")) {
        String cName = constraintNode.get("name").asText();
        String cFullClassName = "oogasalad.engine.behavior.constraint." + cName;
        Class<?> clazz = Class.forName(cFullClassName);

        if (!BehaviorConstraint.class.isAssignableFrom(clazz)) {
          throw new ParsingException("Invalid constraint class: " + cFullClassName);
        }

        createAndAddConstraint(behaviorInstance, constraintNode, clazz);
      }
    }
  }

  private void createAndAddConstraint(Behavior behaviorInstance, JsonNode constraintNode, Class<?> clazz)
      throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    BehaviorConstraint<?> constraint = (BehaviorConstraint<?>) clazz.getDeclaredConstructor().newInstance();
    constraint.setBehavior(behaviorInstance);
    JsonNode config = constraintNode.get("parameter");

    constraint
        .getSerializedFields()
        .forEach(field -> setFieldFromConfig(config, field));

    behaviorInstance.addConstraint(constraint.getClass());
  }

  /**
   * Generates a serialized object to place into JSON
   *
   * @param data - the data object to serialize
   * @return JsonNode to append to the parent node
   */
  @Override
  public JsonNode write(Behavior data) throws IOException {
    // Todo write this method
    return new ObjectMapper().valueToTree(data);
  }

  /**
   * Needed help from ChatGPT to generate this
   */
  @SuppressWarnings("unchecked")
  private <T> void setGenericParameter(BehaviorConstraint<T> constraint, Object value) {
    constraint.setParameter((T) value);
  }

  private void setFieldFromConfig(JsonNode config, SerializedField<?> serializedField) {
    String fieldName = serializedField.getFieldName();
    if (!config.has(fieldName)) return;

    JsonNode valueNode = config.get(fieldName);
    Class<?> fieldType = serializedField.getFieldType();

    try {
      Object value = mapper.convertValue(valueNode, fieldType);
      ((SerializedField<Object>) serializedField).setValue(value);
    } catch (IllegalArgumentException | ClassCastException e) {
      throw new IllegalStateException(
          "Failed to set field '" + fieldName + "' with value: " + valueNode, e);
    }
  }

}

