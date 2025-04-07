package oogasalad.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import oogasalad.engine.base.architecture.Game;
import oogasalad.engine.base.architecture.GameInfo;
import oogasalad.engine.base.architecture.GameScene;
import static oogasalad.config.GameConfig.LOGGER;
import java.io.File;


/**
 * Parses and serializes a Game object to and from a JSON node
 *
 * @author Justin Aronwald, Daniel Rodriguez-Florido
 */
public class GameParser implements Parser<Game> {
  private final ObjectMapper mapper = new ObjectMapper();
  private final GameSceneParser sceneParser = new GameSceneParser();
  private final ResourceParser resourceParser = new ResourceParser();
  private final InformationParser informationParser = new InformationParser();

  private static final String DATA = "Data";
  private static final String INFORMATION = "Information";
  private static final String NAME = "Name";
  private static final String SCENE = "Scene";
  private static final String RESOURCES = "Resources";

  /**
   * Parses a JSON node into a Game instance
   *
   * @param node - the JSON node given to parse
   * @return - a fully configured GameScene
   * @throws ParsingException - error thrown if reflection or parsing fails
   */
  @Override
  public Game parse(JsonNode node) throws ParsingException {
    if (node == null || !node.has(DATA)) {
      LOGGER.warn("No data found");
      throw new ParsingException("No data found");
    }

    Game newGame = new Game();
    JsonNode data = node.get(DATA);

    handleInformationParsing(data, newGame);
    handleResourceParsing(data);
    handleSceneParsing(data, newGame);

    return newGame;
  }

  private void handleInformationParsing(JsonNode data, Game newGame) throws ParsingException {
    if (data.has(INFORMATION)) {
      GameInfo gameInfo = informationParser.parse(data.get(INFORMATION));
      newGame.setGameInfo(gameInfo);
    }
  }

  private void handleResourceParsing(JsonNode data) throws ParsingException {
    Map<String, String> resourceMap = new HashMap<>();
    if (data.has(RESOURCES)) {
      for (JsonNode resourceNode : data.get(RESOURCES)) {
        Map.Entry<String, String> entry = resourceParser.parse(resourceNode);
        resourceMap.put(entry.getKey(), entry.getValue());
      }
    }
  }

  private void handleSceneParsing(JsonNode data, Game newGame) throws ParsingException {
    if (data.has(SCENE) && data.get(SCENE).isArray()) {
      for (JsonNode sceneNode : data.get(SCENE)) {
        createGameScene(newGame, sceneNode);
      }
    }
  }

  private void createGameScene(Game newGame, JsonNode sceneNode) throws ParsingException {
    GameScene gameScene = sceneParser.parse(sceneNode);
    if (gameScene != null) {
      newGame.addScene(gameScene.getName());
    } else {
      LOGGER.error("Scene with name {} not found and therefore will not be added "
          + "to Game.", sceneNode.get(NAME));
    }
  }

  /**
   * Serializes a Game object into a JSON root node
   *
   * @param data - the data object to serialize
   * @return - a JsonNode that holds all the configured Game information
   * @throws IOException - an exception thrown if errors occur with input/output
   */
  @Override
  public JsonNode write(Game data) throws IOException {
    ObjectNode root = mapper.createObjectNode();
    ObjectNode dataNode = mapper.createObjectNode();

    handleInformationWriting(data, root);
    handleResourceWriting(data, dataNode);
    handleSceneWriting(data, dataNode);

    root.set(DATA, dataNode);
    return root;
  }

  private void handleInformationWriting(Game data, ObjectNode root) throws IOException {
    ObjectNode infoNode = (ObjectNode) informationParser.write(data.getGameInfo());
    root.set(INFORMATION, infoNode);
  }

  private void handleSceneWriting(Game data, ObjectNode dataNode) throws IOException {
    ArrayNode sceneArray = mapper.createArrayNode();
    for (GameScene scene : data.getAllScenes()) {
      sceneArray.add(sceneParser.write(scene));
    }
    dataNode.set(SCENE, sceneArray);
  }

  private void handleResourceWriting(Game data, ObjectNode dataNode) throws IOException {
//    ArrayNode resourceArray = mapper.createArrayNode();
//    for (Map.Entry<String, String> entry : data.getAllResources.entrySet()) {
//      resourceArray.add(resourceParser.write(entry));
//    }
//    dataNode.set("Resources", resourceArray);
  }
}
