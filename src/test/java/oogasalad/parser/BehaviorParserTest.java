package oogasalad.parser;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BehaviorParserTest {

  BehaviorParser myBehaviorParser;
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
    myBehaviorParser = new BehaviorParser();
    myMapper = new ObjectMapper();
  }

  @Test
  void parse_validJson_success() throws JsonProcessingException, ParsingException {
    JsonNode node = myMapper.readTree(goodJsonString);
    //Behavior behavior = myBehaviorParser.parse(node);
    //assertNotNull(behavior); // Once we have actual behavior classes this should work
  }

  @Test
  void parse_invalidJson_failure() throws JsonProcessingException, ParsingException {
    JsonNode node = myMapper.readTree(badJsonString);
    assertThrows(ParsingException.class, () -> myBehaviorParser.parse(node));
  }

  // TODO: This test has similar structure to Component but we do not have concrete behaviors yet
  @Test
  void write_validComponent_success() throws IOException {
//    Transform transformComponent = new Transform();
//    transformComponent.setX(1);
//    transformComponent.setY(1);
//    transformComponent.setRotation(1);
//    transformComponent.setScaleX(1);
//    transformComponent.setScaleY(1);
//
//    JsonNode gameComponent = myBehaviorParser.write(transformComponent);
//    assertNotNull(gameComponent);
//    assertTrue(gameComponent.isObject());
//    assertTrue(gameComponent.has("Name"));
//    assertTrue(gameComponent.has("Configurations"));
//
//    JsonNode configurations = gameComponent.get("Configurations");
//    assertTrue(configurations.has("x"));
//    assertTrue(configurations.has("y"));
//    assertTrue(configurations.has("rotation"));
//    assertTrue(configurations.has("scaleX"));
//    assertTrue(configurations.has("scaleY"));
  }
}