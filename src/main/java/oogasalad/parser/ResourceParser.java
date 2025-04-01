package oogasalad.parser;

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
public class ResourceParser implements Parser<Map<String, String>> {
  private final ObjectMapper mapper = new ObjectMapper();


  /**
   * Parses the resource array into a map of name to a path
   *
   * @param node - the JSON node given to parse
   * @return - a map where the key is the name and the value is the path of the file
   * @throws ParsingException - error thrown when parsing fails or is interrupted
   */
  @Override
  public Map<String, String> parse(JsonNode node) throws ParsingException {
    Map<String, String> resourceMap = new HashMap<>();
    if (node == null || !node.isArray()) {
      return resourceMap;
    }

    for (JsonNode resourceNode : node) {
      String name = resourceNode.get("Name").asText();
      String path = resourceNode.get("Path").asText();

      resourceMap.put(name, path);
    }

    return resourceMap;
  }

  @Override
  public JsonNode write(Map<String, String> data) throws IOException {
    ArrayNode arrayNode = mapper.createArrayNode();
    for (Map.Entry<String, String> entry : data.entrySet()) {
      ObjectNode resourceNode = mapper.createObjectNode();
      resourceNode.put("Name", entry.getKey());
      resourceNode.put("Path", entry.getValue());
      arrayNode.add(resourceNode);
    }
    return arrayNode;
  }
}
