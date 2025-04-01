package oogasalad.parser;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.io.OutputStream;

public class SceneParser implements Parser<Void> {
  private final GameObjectParser gameObjectParser = new GameObjectParser();
  @Override
  public Void parse(JsonNode node) throws ParsingException {
    for (JsonNode child : node) {
      String sceneName = child.get("Name").asText();
      JsonNode gameObjects = child.get("GameObjects");
      gameObjectParser.parse(gameObjects);
    }
    return null;
  }

  @Override
  public void write(Void data, OutputStream output) throws IOException {

  }
}
