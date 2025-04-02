package oogasalad.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
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

  /**
   * Parses a JSON node into a gameObject (entity)
   *
   * @param node - the JSON node given to parse
   * @return - a new gameObject instance with components and behaviors added
   * @throws ParsingException - if there's an error or parsing fails
   */
  @Override
  public GameObject parse(JsonNode node) throws ParsingException {
    if (node == null || !node.has("Name")) {
      throw new ParsingException("No name found");
    }
    
    String name = node.get("Name").asText();
    GameObject gameObject = new GameObject(name);

    handleAddingTag(node, gameObject);
    handleAddingComponents(node, gameObject);
    handleAddingBehaviors(node, gameObject);

    return gameObject;
  }

  private static void handleAddingTag(JsonNode node, GameObject gameObject) {
    if (node.has("Tag")) {
      String tag = node.get("Tag").asText();
      gameObject.setTag(tag);
    }
  }

  private void handleAddingBehaviors(JsonNode node, GameObject gameObject) throws ParsingException {
    if (node.has("Behaviors")) {
      JsonNode behaviors = node.get("Behaviors");
      parseBehaviors(gameObject, behaviors);
    }
  }

  private void handleAddingComponents(JsonNode node, GameObject gameObject) throws ParsingException {
    if (node.has("Components")) {
      JsonNode components = node.get("Components");
      parseComponents(gameObject, components);
    }
  }

  private void parseComponents(GameObject gameObject, JsonNode componentsNode) throws ParsingException {
    if (componentsNode.isArray()) {
      for (JsonNode component : componentsNode) {
        handleComponentParsing(gameObject, component);
      }
    } else if (componentsNode.isObject()) {
      handleComponentParsing(gameObject, componentsNode);
    }
  }

  private void handleComponentParsing(GameObject gameObject, JsonNode componentNode) throws ParsingException {
    GameComponent component = componentParser.parse(componentNode);
    JsonNode config = componentNode.get("Configurations");
    component.initializeFromJson(config);
    gameObject.addComponent(component.getClass());
  }

  private void parseBehaviors(GameObject gameObject, JsonNode behaviorsNode) throws ParsingException {
    if (behaviorsNode.isArray()) {
      for (JsonNode behaviorNode : behaviorsNode) {
        handleBehaviorParsing(gameObject, behaviorNode);
      }
    }
  }

  private void handleBehaviorParsing(GameObject gameObject, JsonNode behaviorNode) throws ParsingException {
    Behavior behavior = behaviorParser.parse(behaviorNode);
    JsonNode config = behaviorNode.get("Configurations");
    behavior.initializeFromJson(config);
    gameObject.addComponent(behavior.getClass());
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
    root.put("Name", data.getName());

    if (data.getTag() != null) {
      root.put("Tag", data.getTag());
    }

    handleWritingComponents(data, root);
    handleWritingBehavior(data, root);

    return root;
  }

  private void handleWritingComponents(GameObject data, ObjectNode root) {
    ArrayNode components = mapper.createArrayNode();
    for (GameComponent component : data.getAllComponents().values()) {
      JsonNode componentNode = componentParser.write(component);
      components.add(componentNode);
    }
    root.set("Components", components);
  }

  private void handleWritingBehavior(GameObject data, ObjectNode root) {
    ArrayNode behaviors = mapper.createArrayNode();
    for (Behavior behavior : data.getAllBehaviors()) {
      JsonNode behaviorNode = behaviorParser.write(behavior);
      behaviors.add(behaviorNode);
    }

    root.set("Behaviors", behaviors);
  }
}
