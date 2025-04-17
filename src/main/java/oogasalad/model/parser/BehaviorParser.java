package oogasalad.model.parser;

import static oogasalad.model.config.GameConfig.LOGGER;
import static oogasalad.model.config.GameConfig.getText;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import oogasalad.model.engine.base.behavior.Behavior;
import oogasalad.model.engine.base.behavior.BehaviorAction;
import oogasalad.model.engine.base.behavior.BehaviorComponent;
import oogasalad.model.engine.base.behavior.BehaviorConstraint;
import oogasalad.model.engine.base.behavior.BehaviorParamType;
import oogasalad.model.engine.base.serialization.SerializedField;
import oogasalad.model.engine.base.serialization.TypeRef;

// Assumes I am already within a behavior subsection at one of the array objects.

/**
 * Parses and serializes a Behavior object to and from a JSON node
 *
 * @author Justin Aronwald, Daniel Radriguez-Florido
 */
public class BehaviorParser implements Parser<Behavior> {

  private static final String NAME = "Name";
  private static final String PARAMETER_TYPE = "parameterType";
  private static final String ACTIONS = "actions";
  private static final String CONSTRAINTS = "constraints";
  private static final String ACTION_CLASS_PACKAGE = "oogasalad.model.engine.action.";
  private static final String CONSTRAINT_CLASS_PACKAGE = "oogasalad.model.engine.constraint.";
  private static final String PARAMETER = "parameter";
  private static final String LOWER_NAME = "name";
  private final ObjectMapper mapper = new ObjectMapper();

  /**
   * @param behaviorNode - the JSON node given to parse
   * @return A Behavior Game Component
   * @throws ParsingException indicating that there was an issue with parsing
   */
  @Override
  public Behavior parse(JsonNode behaviorNode) throws ParsingException {
    if (!behaviorNode.has(NAME)) {
      LOGGER.error(getText("noBehaviorNameError"));
      throw new ParsingException(getText("noBehaviorNameError"));
    }

    try {
      String name = behaviorNode.get(NAME).asText();

      Behavior behaviorInstance = new Behavior(name);

      parseBehaviorComponent(behaviorNode, behaviorInstance, CONSTRAINTS, CONSTRAINT_CLASS_PACKAGE);
      parseBehaviorComponent(behaviorNode, behaviorInstance, ACTIONS, ACTION_CLASS_PACKAGE);

      return behaviorInstance;
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
             NoSuchMethodException e) {
      LOGGER.error(getText("instantiateBehaviorError",
          behaviorNode.get(NAME).asText()));
      throw new ParsingException(getText("instantiateBehaviorError",
          behaviorNode.get(NAME).asText()), e);
    }
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

    writeBehaviorComponent(data, root, CONSTRAINTS);
    writeBehaviorComponent(data, root, ACTIONS);

    return root;
  }

  private void parseBehaviorComponent(JsonNode behaviorNode, Behavior behaviorInstance, String typeName, String packageName) throws ParsingException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
    if (behaviorNode.has(typeName)) {
      for (JsonNode node : behaviorNode.get(typeName)) {
        Class<?> clazz;
        String nameBlock = node.get(LOWER_NAME).asText();
        String fullClassName = packageName + nameBlock;

        try {
          clazz = Class.forName(fullClassName);
        } catch (ClassNotFoundException e) {
          LOGGER.error(getText("failToParseError", fullClassName));
          throw new ParsingException(getText("failToParseError", fullClassName), e);
        }

        createBehaviorComponent(behaviorInstance, node, clazz);
      }
    }
  }

  private void createBehaviorComponent(Behavior behaviorInstance, JsonNode node, Class<?> behaviorCompClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParsingException {
    BehaviorComponent<?> behaviorComp = (BehaviorComponent<?>) behaviorCompClass.getDeclaredConstructor().newInstance();
    behaviorComp.setBehavior(behaviorInstance);

    SerializedField paramField = behaviorComp.getSerializedFields().stream().filter(field -> field.getFieldName().equals(PARAMETER)).findAny().orElse(null);
    JsonNode valueNode = node.get(PARAMETER);

    if (paramField == null || valueNode == null) {
      throw new IllegalArgumentException(getText("missingParameter"));
    }

    Class<?> parameterType = getParameterType(node);
    setFieldFromValue(paramField, valueNode, parameterType);

    if (behaviorComp instanceof BehaviorAction) {
      behaviorInstance.addAction((BehaviorAction<?>) behaviorComp);
    } else if (behaviorComp instanceof BehaviorConstraint) {
      behaviorInstance.addConstraint((BehaviorConstraint<?>) behaviorComp);
    }
  }

  private void setFieldFromValue(SerializedField field, JsonNode valueNode, Class<?> type) throws ParsingException {
    try {
      Object value = getValueFromMapper(valueNode, type);
      if (value != null) {
        field.setValue(value);
      }
    } catch (IllegalArgumentException e) {
      throw new ParsingException(getText("invalidEnumValue", field.getFieldName(), valueNode.asText()), e);
    } catch (ClassCastException e) {
      throw new ParsingException(getText("typeMismatchError", field.getFieldName(), type.getSimpleName()), e);
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

  private void writeBehaviorComponent(Behavior data, ObjectNode root, String typeName) {
    ArrayNode constraintArray = mapper.createArrayNode();
    List<BehaviorComponent<?>> comps = (List<BehaviorComponent<?>>) (Objects.equals(typeName, ACTIONS) ? data.getActions() : data.getConstraints());

    comps.forEach(comp -> {
      ObjectNode oneConstraint = mapper.createObjectNode();

      // Set null values
      if (comp.getParameter() == null || !BehaviorParamType.isSupported(comp.getParameter().getClass())) {
        oneConstraint.set(PARAMETER, NullNode.instance);
        return;
      }

      // Ensure PARAMETER_TYPE uses wrapper types (e.g., Integer for int)
      Class<?> parameterClass = comp.getParameter().getClass();
      parameterClass = TypeRef.wrapperForPrimitive(parameterClass);

      oneConstraint.put(LOWER_NAME, comp.getClass().getSimpleName());
      oneConstraint.set(PARAMETER, JsonNodeFactory.instance.pojoNode(comp.getParameter()));
      oneConstraint.put(PARAMETER_TYPE, parameterClass.getSimpleName());
      constraintArray.add(oneConstraint);

      root.set(typeName, constraintArray);
    });
  }

  private Class<?> getParameterType(JsonNode node) throws ParsingException {
    String typeName = node.get(PARAMETER_TYPE).asText().toUpperCase();

    try {
      Class<?> typeClass = BehaviorParamType.valueOf(typeName).getClassType();
      assert typeClass != null;
      return typeClass;
    } catch (IllegalArgumentException | AssertionError e) {
      throw new ParsingException(getText("unknownParameterError", typeName));
    }
  }

}

