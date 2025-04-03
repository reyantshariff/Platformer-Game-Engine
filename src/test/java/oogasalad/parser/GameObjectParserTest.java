package oogasalad.parser;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import oogasalad.engine.base.architecture.GameObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameObjectParserTest {

  GameObjectParser myGameObjectParser;
  ObjectMapper myMapper;
  String goodJsonString =
        """
        {
          "Name": "Test Object",
          "Components": [
            {
              "Name": "Transform",
              "Configurations": {
                "position": { "X": 0, "Y": 0 },
                "rotation": { "X": 0, "Y": 0 },
                "scale": { "X": 1, "Y": 1 }
              }
            }
          ]
        }
        """;

  // The bad json string has no name, should return error
  String badJsonString =
      """
      {
        "Components": [
          {
            "Name": "Transform",
            "Configurations": {
              "position": { "X": 0, "Y": 0 },
              "rotation": { "X": 0, "Y": 0 },
              "scale": { "X": 1, "Y": 1 }
            }
          }
        ]
      }
      """;

  @BeforeEach
  void setUp() {
    myGameObjectParser = new GameObjectParser();
    myMapper = new ObjectMapper();
  }

  @Test
  void parse_validJson_readsGameObject() throws JsonProcessingException, ParsingException {
    JsonNode node = myMapper.readTree(goodJsonString);
    GameObject gameObject = myGameObjectParser.parse(node);
    assertNotNull(gameObject);
    assertTrue(gameObject.getName().contains("GameObject_"));
  }

  @Test
  void parse_invalidJson_throwsError() throws JsonProcessingException, ParsingException {
    JsonNode node = myMapper.readTree(badJsonString);
    assertThrows(ParsingException.class, () -> myGameObjectParser.parse(node));
  }

  @Test
  void write() {
  }
}