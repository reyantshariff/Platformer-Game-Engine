package oogasalad.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import oogasalad.engine.base.architecture.GameObject;

/**
 * Parses and serializes scenes to and from JSON
 *
 */
public class SceneParser implements Parser<List<Scene>> {
  private final GameObjectParser gameObjectParser = new GameObjectParser();

  /**
   * Parses a JSON array of scenes into a list of Scene objects
   *
   * @param node - the JSON node given to parse
   * @return - a list of the parsed scene objects
   * @throws ParsingException - parsing error specific thrown if issues with parsing
   */
  @Override
  public List<Scene> parse(JsonNode node) throws ParsingException {
    if (node == null || !node.isArray()) {
      throw new ParsingException("SceneParser requires a JSON array");
    }

    List<Scene> scenes = new ArrayList<>();

    for (JsonNode scene : node) {
      String sceneName = scene.get("Name").asText();

      JsonNode gameObjects = scene.get("GameObjects");
      List<GameObject> gameObjectList = new ArrayList<>();
      for (JsonNode gameObject : gameObjects) {
        gameObjectList.add(gameObjectParser.parse(gameObject));
      }

      Scene newScene = new Scene(sceneName, gameObjectList);
      scenes.add(newScene);

    }
    return scenes;
  }

  /**
   * Serializes a list of scene objects into JSON array
   *
   * @param data - the data object to serialize
   * @throws IOException - the error thrown when input output fails or is interrupted
   */
  @Override
  public JsonNode write(List<Scene> data) throws IOException {
    return NullNode.getInstance();
  }

}
