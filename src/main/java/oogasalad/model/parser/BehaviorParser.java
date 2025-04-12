package oogasalad.model.parser;

import static oogasalad.model.config.GameConfig.LOGGER;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import oogasalad.model.engine.base.behavior.Behavior;
import oogasalad.model.engine.base.behavior.BehaviorAction;
import oogasalad.model.engine.base.behavior.BehaviorConstraint;
import oogasalad.model.engine.base.enumerate.KeyCode;
import oogasalad.model.engine.base.serialization.SerializedField;

// Assumes I am already within a behavior subsection at one of the array objects.

/**
 * Parses and serializes a Behavior object to and from a JSON node
 *
 * @author Justin Aronwald, Daniel Radriguez-Florido
 */
public class BehaviorParser implements Parser<Behavior> {

  private static final String NAME = "Name";
  private static final String ACTIONS = "actions";
  private static final String PARAMETER_TYPE = "parameterType";
  private static final String CONSTRAINTS = "constraints";
  private static final String ACTION_CLASS_PATH = "oogasalad.model.engine.action.";
  private static final String CONSTRAINT_CLASS_PATH = "oogasalad.model.engine.constraint.";
  private static final String PARAMETER = "parameter";
  private static final String LOWER_NAME = "name";

  private static final Map<Class<?>, BiConsumer<ObjectNode, Object>> TYPE_WRITERS = new HashMap<>();

  static {
    TYPE_WRITERS.put(String.class, (node, value) -> node.put(PARAMETER, (String) value));
    TYPE_WRITERS.put(Integer.class, (node, value) -> node.put(PARAMETER, (Integer) value));
    TYPE_WRITERS.put(Double.class, (node, value) -> node.put(PARAMETER, (Double) value));
    TYPE_WRITERS.put(KeyCode.class, (node, value) -> node.put(PARAMETER, value.toString()));
  }

  private final ObjectMapper mapper = new ObjectMapper();

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

      Behavior behaviorInstance = new Behavior(name);

      parseConstraints(behaviorNode, behaviorInstance);
      parseActions(behaviorNode, behaviorInstance);

      return behaviorInstance;
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
             NoSuchMethodException e) {
      LOGGER.error("Could not instantiate Behavior class: {}",
          behaviorNode.get("Name").asText());
      throw new ParsingException("Could not instantiate Behavior class." + e, e);
    }
  }

  private void parseActions(JsonNode behaviorNode, Behavior behaviorInstance)
      throws ParsingException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
    if (behaviorNode.has(ACTIONS)) {
      for (JsonNode actionNode : behaviorNode.get(ACTIONS)) {
        Class<?> clazz;
        String aName = actionNode.get(LOWER_NAME).asText();
        String aFullClassName = ACTION_CLASS_PATH + aName;

        try {
          clazz = Class.forName(aFullClassName);
        } catch (ClassNotFoundException e) {
          LOGGER.error("Could not parse action. Class {} does not exist.", aFullClassName);
          throw new ParsingException("Failed to parse action: " + actionNode, e);
        }

        createAndAddAction(behaviorInstance, actionNode, clazz);
      }
    }
  }

  private void createAndAddAction(Behavior behaviorInstance, JsonNode actionNode, Class<?> clazz)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchMethodException, ParsingException {
    BehaviorAction<?> action = (BehaviorAction<?>) clazz.getDeclaredConstructor().newInstance();
    action.setBehavior(behaviorInstance);

    SerializedField<?> paramField = action.getParentSerializableField();
    JsonNode valueNode = actionNode.get(PARAMETER);

    if (valueNode == null) {
      throw new IllegalArgumentException("Missing parameter field");
    }

    Class<?> parameterType = getParameterType(actionNode, paramField.getFieldType());
    setFieldFromValue(paramField, valueNode, parameterType);

    behaviorInstance.addAction(action);
  }

  private void parseConstraints(JsonNode behaviorNode, Behavior behaviorInstance)
      throws ParsingException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
    if (behaviorNode.has(CONSTRAINTS)) {
      for (JsonNode constraintNode : behaviorNode.get(CONSTRAINTS)) {
        Class<?> clazz;
        String cName = constraintNode.get(LOWER_NAME).asText();
        String cFullClassName = CONSTRAINT_CLASS_PATH + cName;

        try {
          clazz = Class.forName(cFullClassName);
        } catch (ClassNotFoundException e) {
          LOGGER.error("Could not find constraint class {}", cFullClassName);
          throw new ParsingException("Invalid constraint class: " + cFullClassName + e, e);
        }

        createAndAddConstraint(behaviorInstance, constraintNode, clazz);
      }
    }
  }

  private void createAndAddConstraint(Behavior behaviorInstance, JsonNode constraintNode,
      Class<?> clazz)
      throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParsingException {

    BehaviorConstraint<?> constraint = (BehaviorConstraint<?>) clazz.getDeclaredConstructor()
        .newInstance();
    constraint.setBehavior(behaviorInstance);

    SerializedField<?> paramField = constraint.getParentSerializableField();
    JsonNode valueNode = constraintNode.get(PARAMETER);

    if (valueNode == null) {
      throw new IllegalArgumentException("Missing parameter field");
    }

    Class<?> parameterType = getParameterType(constraintNode, paramField.getFieldType());
    setFieldFromValue(paramField, valueNode, parameterType);

    behaviorInstance.addConstraint(constraint);
  }

  private void setFieldFromValue(SerializedField<?> field, JsonNode valueNode, Class<?> type)
      throws ParsingException {

    try {
      Object value;

      value = getValueFromMapper(valueNode, type);

      SerializedField<Object> typedField = (SerializedField<Object>) field;
      typedField.setValue(value);

    } catch (IllegalArgumentException e) {
      throw new ParsingException(
          "Invalid value for enum field '" + field.getFieldName() + "': " + valueNode.asText(), e);
    } catch (ClassCastException e) {
      throw new ParsingException(
          "Type mismatch for field '" + field.getFieldName() + "' — expected "
              + type.getSimpleName(), e);
    }
  }

  private Object getValueFromMapper(JsonNode valueNode, Class<?> type) {
    Object value;
    if (type.isEnum()) {
      Class<? extends Enum<?>> enumType = (Class<? extends Enum<?>>) type;
      value = Enum.valueOf((Class) enumType, valueNode.asText());
    } else {
      value = mapper.convertValue(valueNode, type);
    }
    return value;
  }

  /**
   * Generates a serialized object to place into JSON
   *
   * @param data - the data object to serialize
   * @return JsonNode to append to the parent node
   */
  @Override
  public JsonNode write(Behavior data) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode root = mapper.createObjectNode();

    String behaviorName = data.getName();
    root.put(NAME, behaviorName);

    //I understand that the two methods below are quite similar in structure, but the alternative
    // would be to create a method with about 6 or more parameters to account for both,
    // which I think is less readable and coder friendly than two similar methods.
    writeConstraints(data, root);
    writeActions(data, root);

    return root;
  }

  private void writeConstraints(Behavior data, ObjectNode root) {
    ArrayNode constraintArray = mapper.createArrayNode();

    data.getConstraints().forEach(constraint -> {
      ObjectNode oneConstraint = mapper.createObjectNode();
      BiConsumer<ObjectNode, Object> writer = TYPE_WRITERS.get(
          constraint.getParameter().getClass());
        
        if(writer == null || constraint.getParameter() == null || oneConstraint == null) {
          LOGGER.error("Could not write constraint {} for behavior {}. Invalid JSON parameter type. Skipping constraint.",
              constraint.getParameter().getClass().getSimpleName(), data.getName());
          return;
        }

        oneConstraint.put(LOWER_NAME, constraint.getClass().getSimpleName());
        writer.accept(oneConstraint, constraint.getParameter()); // Catching this error
        oneConstraint.put(PARAMETER_TYPE, constraint.getParameter().getClass().getSimpleName());
        constraintArray.add(oneConstraint);

      root.set(CONSTRAINTS, constraintArray);
    });
  }

  private void writeActions(Behavior data, ObjectNode root) {
    ArrayNode actionsArray = mapper.createArrayNode();

    data.getActions().forEach(action -> {
      ObjectNode oneAction = mapper.createObjectNode();
      BiConsumer<ObjectNode, Object> writer = TYPE_WRITERS.get(action.getParameter().getClass());

      if(writer == null || action.getParameter() == null || oneAction == null) {
        LOGGER.error(
            "Could not write action {} for behavior {}. Invalid JSON parameter type. Skipping action.",
            action.getParameter().getClass().getSimpleName(), data.getName());
        return;
      }

        oneAction.put(LOWER_NAME, action.getClass().getSimpleName());
        writer.accept(oneAction, action.getParameter()); // Catching this error
        oneAction.put(PARAMETER_TYPE, action.getParameter().getClass().getSimpleName());

        actionsArray.add(oneAction);
    });

    root.set(ACTIONS, actionsArray);
  }


  //unsure how to handle this without using switch
  private Class<?> getParameterType(JsonNode node, Class<?> defaultType) throws ParsingException {
    if (!node.has(PARAMETER_TYPE)) {
      return defaultType;
    }

    String typeName = node.get(PARAMETER_TYPE).asText();
    return switch (typeName) {
      case "KeyCode" -> oogasalad.model.engine.base.enumerate.KeyCode.class;
      case "String" -> String.class;
      case "Integer" -> Integer.class;
      case "Double" -> Double.class;
      default -> throw new ParsingException("Unknown parameterType: " + typeName);
    };
  }

}

