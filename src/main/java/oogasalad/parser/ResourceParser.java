package oogasalad.parser;

import static oogasalad.config.GameConfig.LOGGER;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses and serializes the resources with a name-to-path mapping
 *
 */
public class ResourceParser implements Parser<Map.Entry<String, String>> {
  private final ObjectMapper mapper = new ObjectMapper();


  /**
   * Parses the resource array into a map of name to a path
   *
   * @param node - the JSON node given to parse
   * @return - a map where the key is the name and the value is the path of the file
   * @throws ParsingException - error thrown when parsing fails or is interrupted
   */
  @Override
  public Map.Entry<String, String> parse(JsonNode node) throws ParsingException {
    if (node == null || !node.isArray()) {
      LOGGER.warn("{} is not an array", node);
      throw new ParsingException("Not an array");
    }
    String name = node.get("Name").asText();
    String path = node.get("Path").asText();

    return Map.entry(name, path);
  }

  @Override
  public JsonNode write(Map.Entry<String, String> entry) throws IOException {
    ObjectNode resourceNode = mapper.createObjectNode();
    resourceNode.put("Name", entry.getKey());
    resourceNode.put("Path", entry.getValue());
    return resourceNode;
  }
}
