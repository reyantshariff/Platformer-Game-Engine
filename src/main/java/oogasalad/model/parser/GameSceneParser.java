package oogasalad.model.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.base.architecture.GameScene;

/**
 * Parses and serializes a GameScene to and from a JSON node
 *
 * @author Justin Aronwald
 */
public class GameSceneParser implements Parser<GameScene> {

  private final GameObjectParser gameObjectParser = new GameObjectParser();
  private final ObjectMapper mapper = new ObjectMapper();

  private static final String GAMEOBJECTS = "GameObjects";
  private static final String NAME = "Name";


  @Override
  public GameScene parse(JsonNode node) throws ParsingException {
    validateGameSceneName(node);
    String name = node.get(NAME).asText();

    GameScene scene = new GameScene(name);
    handleGameObjectParsing(node, scene);
    return scene;
  }

  private static void validateGameSceneName(JsonNode node) throws ParsingException {
    if (node == null || !node.has(NAME)) {
      throw new ParsingException("No name found");
    }
  }

  private void handleGameObjectParsing(JsonNode node, GameScene scene) throws ParsingException {
    if (node.has(GAMEOBJECTS) && node.get(GAMEOBJECTS).isArray()) {
      for (JsonNode gameObjectNode : node.get(GAMEOBJECTS)) {
        GameObject gameObject = gameObjectParser.parse(gameObjectNode);
        scene.registerObject(gameObject);
      }
    }
  }

  /**
   * Serializes a GameScene into a JSON node
   *
   * @param data - the data object to serialize
   * @return - a JsonNode that holds all the configured GameScene information
   * @throws IOException - an exception thrown if errors occurs with input/output
   */
  @Override
  public JsonNode write(GameScene data) throws IOException {
    ObjectNode root = mapper.createObjectNode();
    root.put(NAME, data.getName());

    ArrayNode gameObjects = mapper.createArrayNode();
    for (GameObject gameObject : data.getAllObjects()) {
      JsonNode gameObjectNode = gameObjectParser.write(gameObject);
      gameObjects.add(gameObjectNode);
    }

    root.set(GAMEOBJECTS, gameObjects);
    return root;
  }
}
