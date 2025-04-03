package oogasalad.parser;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import oogasalad.engine.component.Behavior;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BehaviorParserTest {

  BehaviorParser behaviorParser;
  ObjectMapper myMapper;
  String goodJsonString =
      """
      {
        "Name": "TestBehaviour",
        "Configurations": {
          "startScene": "Example Main Scene"
        }
      }
      """;

  // The bad json string has no name, should return error
  String badJsonString =
      """
      {
        "Configurations": {}
      }
      """;

  @BeforeEach
  void setUp() {
    behaviorParser = new BehaviorParser();
    myMapper = new ObjectMapper();
  }

  @Test
  void parse_validJson_success() throws JsonProcessingException, ParsingException {
    JsonNode node = myMapper.readTree(goodJsonString);
    Behavior behavior = behaviorParser.parse(node);
    assertNotNull(behavior); // Once we have actual behavior classes this should work
  }

  @Test
  void parse_invalidJson_failure() throws JsonProcessingException, ParsingException {
    JsonNode node = myMapper.readTree(badJsonString);
    assertThrows(ParsingException.class, () -> behaviorParser.parse(node));
  }

  @Test
  void write() {
  }
}