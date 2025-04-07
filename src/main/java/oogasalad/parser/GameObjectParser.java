package oogasalad.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.component.Behavior;

/**
 * This class parses and serializes gameObjects to and from a JSON
 *
 * @author Justin Aronwald
 */
public class GameObjectParser implements Parser<GameObject> {

  private final ComponentParser componentParser = new ComponentParser();
  private final BehaviorParser behaviorParser = new BehaviorParser();
  private final ObjectMapper mapper = new ObjectMapper();

  private static final String TAG = "Tag";
  private static final String BEHAVIORS = "Behaviors";
  private static final String NAME = "Name";
  private static final String COMPONENTS = "Components";
  private static final String CONFIGURATIONS = "Configurations";


  /**
   * Parses a JSON node into a gameObject (entity)
   *
   * @param node - the JSON node given to parse
   * @return - a new gameObject instance with components and behaviors added
   * @throws ParsingException - if there's an error or parsing fails
   */
  @Override
  public GameObject parse(JsonNode node) throws ParsingException {
    if (node == null || !node.has(NAME)) {
      throw new ParsingException("No name found");
    }

    String name = node.get(NAME).asText();
    String tag = node.has(TAG) ? node.get(TAG).asText() : null;

    GameObject gameObject = new GameObject(name, tag);

    handleAddingTag(node, gameObject);
    handleAddingComponents(node, gameObject);
    handleAddingBehaviors(node, gameObject);

    return gameObject;
  }

  private static void handleAddingTag(JsonNode node, GameObject gameObject) {
    if (node.has(TAG)) {
      String tag = node.get(TAG).asText();
      gameObject.setTag(tag);
    }
  }

  private void handleAddingBehaviors(JsonNode node, GameObject gameObject) throws ParsingException {
    if (node.has(BEHAVIORS)) {
      JsonNode behaviors = node.get(BEHAVIORS);
      parseBehaviors(gameObject, behaviors);
    }
  }

  private void handleAddingComponents(JsonNode node, GameObject gameObject)
      throws ParsingException {
    if (node.has(COMPONENTS)) {
      JsonNode components = node.get(COMPONENTS);
      parseComponents(gameObject, components);
    }
  }

  private void parseComponents(GameObject gameObject, JsonNode componentsNode)
      throws ParsingException {
    if (componentsNode.isArray()) {
      for (JsonNode component : componentsNode) {
        handleComponentParsing(gameObject, component);
      }
    } else if (componentsNode.isObject()) {
      handleComponentParsing(gameObject, componentsNode);
    }
  }

  private void handleComponentParsing(GameObject gameObject, JsonNode componentNode)
      throws ParsingException {
    GameComponent component = componentParser.parse(componentNode);
    gameObject.addComponent(
        component.getClass()); // TODO: Change to addComponent(Component) instead of component class
  }

  private void parseBehaviors(GameObject gameObject, JsonNode behaviorsNode)
      throws ParsingException {
    if (behaviorsNode.isArray()) {
      for (JsonNode behaviorNode : behaviorsNode) {
        handleBehaviorParsing(gameObject, behaviorNode);
      }
    }
  }

  private void handleBehaviorParsing(GameObject gameObject, JsonNode behaviorNode)
      throws ParsingException {
    Behavior behavior = behaviorParser.parse(behaviorNode);
    gameObject.addComponent(
        behavior.getClass()); // TODO: Change to addComponent(Component) instead of component class
  }

  /**
   * Serializes a GameObject into a JSON node
   *
   * @param data - the data object to serialize
   * @return - a JSON node with the game object's data
   */
  @Override
  public JsonNode write(GameObject data) throws IOException {
    ObjectNode root = mapper.createObjectNode();
    root.put(NAME, data.getName());

    if (data.getTag() != null) {
      root.put(TAG, data.getTag());
    }

    List<GameComponent> components = new ArrayList<>();
    List<Behavior> behaviors = new ArrayList<>();

    divideComponentsAndBehaviors(data, behaviors, components);

    handleWritingComponents(root, components);
    handleWritingBehavior(root, behaviors);

    return root;
  }

  private static void divideComponentsAndBehaviors(GameObject data, List<Behavior> behaviors,
      List<GameComponent> components) {
    for (GameComponent component : data.getAllComponents().values()) {
      if (Behavior.class.isAssignableFrom(component.getClass())) {
        behaviors.add((Behavior) component);
      } else {
        components.add(component);
      }
    }
  }

  private void handleWritingComponents(ObjectNode root, List<GameComponent> componentList)
      throws IOException {
    ArrayNode components = mapper.createArrayNode();
    for (GameComponent component : componentList) {
      JsonNode componentNode = componentParser.write(component);
      components.add(componentNode);
    }
    root.set(COMPONENTS, components);
  }

  private void handleWritingBehavior(ObjectNode root, List<Behavior> behaviorList)
      throws IOException {
    ArrayNode behaviors = mapper.createArrayNode();
    for (Behavior behavior : behaviorList) {
      JsonNode behaviorNode = behaviorParser.write(behavior);
      behaviors.add(behaviorNode);
    }
    root.set(BEHAVIORS, behaviors);
  }
}
