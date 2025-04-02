package oogasalad.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.awt.Dimension;
import java.io.IOException;
import oogasalad.engine.base.architecture.GameInfo;
import static oogasalad.config.GameConfig.LOGGER;

/**
 * Parses and serializes the information node of a Game object to and from a JSON node
 *
 * @author Justin Aronwald
 */
public class InformationParser implements Parser<GameInfo> {
  private final ObjectMapper mapper = new ObjectMapper();

  /**
   * Parses a JSON node into a GameInfo class
   *
   * @param node - the JSON node given to parse
   * @return - a fully configured GameInfo object
   * @throws ParsingException - error thrown if reflection or parsing fails
   */
  @Override
  public GameInfo parse(JsonNode node) throws ParsingException {
    if (node == null || !node.has("Name")) {
      LOGGER.warn("Missing name");
      throw new ParsingException("Missing name");
    }
    handleGameInfoParsing result = getHandleGameInfoParsing(node);
    return new GameInfo(result.name(), result.description(), result.author(), result.resolution());
  }

  private static handleGameInfoParsing getHandleGameInfoParsing(JsonNode node)
      throws ParsingException {
    String name = node.get("Name").asText();
    String description = node.has("Description") ? node.get("Description").asText() : "";
    String author = node.has("Author") ? node.get("Author").asText() : "";

    JsonNode resolutionNode = node.get("Resolution");
    if (resolutionNode == null || !resolutionNode.has("Width") || !resolutionNode.has("Height")) {
      LOGGER.warn("Missing or invalid resolution block");
      throw new ParsingException("Missing or invalid resolution block");
    }

    int width = resolutionNode.get("Width").asInt();
    int height = resolutionNode.get("Height").asInt();
    Dimension resolution = new Dimension(width, height);
    return new handleGameInfoParsing(name, description, author, resolution);
  }

  private record handleGameInfoParsing(String name, String description, String author, Dimension resolution) {

  }

  /**
   * Serializes a GameInfo object into a JSON node
   *
   * @param info - the GameInfo object to serialize
   * @return - a JsonNode that holds all the configured GameInfo information (name, author ...)
   * @throws IOException - an exception thrown if errors occur with input/output
   */
  @Override
  public JsonNode write(GameInfo info) throws IOException {
    ObjectNode root = mapper.createObjectNode();
    root.put("Name", info.name());
    root.put("Description", info.description());
    root.put("Author", info.author());

    ObjectNode resolutionNode = mapper.createObjectNode();
    resolutionNode.put("Width", info.resolution().width);
    resolutionNode.put("Height", info.resolution().height);
    root.set("Resolution", resolutionNode);

    return root;
  }
}