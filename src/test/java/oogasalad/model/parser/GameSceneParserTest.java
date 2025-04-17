package oogasalad.model.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import oogasalad.model.parser.GameSceneParser;
import oogasalad.model.parser.ParsingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameSceneParserTest {

  GameSceneParser myGameSceneParser;
  ObjectMapper myMapper;
  String goodJsonString = """
      {
        "Name": "Test Scene",
        "GameObjects": [
          {
            "Name": "Test Object",
            "Components": [
              {
                "Name": "Transform",
                "Configurations": {
                  "x": 100,
                  "y": 420,
                  "rotation": 0,
                  "scaleX": 40,
                  "scaleY": 40
                }
              }
            ],
            "Behaviors": [
              {
                "Name": "ResetOnHit",
                "constraints": [
                  {
                    "name": "CollidesWithConstraint",
                    "parameter": "player",
                    "parameterType": "String"
                  }
                ],
                "actions": [
                  {
                    "name": "ChangeSceneAction",
                    "parameter": "MainMenuScene",
                    "parameterType": "String"
                  }
                ]
              }
            ]
          }
        ]
      }
      """;

  // The bad json string has no name, should return error
  String badJsonString = """
      {
        "GameObjects": [
          {
            "Name": "Test Object",
            "Components": [
              {
                "Name": "Transform",
                "Configurations": {
                  "x": 100,
                  "y": 420,
                  "rotation": 0,
                  "scaleX": 40,
                  "scaleY": 40
                }
              }
            ],
            "Behaviors": [
              {
                "Name": "ResetOnHit",
                "constraints": [
                  {
                    "name": "CollidesWithConstraint",
                    "parameter": "player",
                    "parameterType": "String"
                  }
                ],
                "actions": [
                  {
                    "name": "ChangeSceneAction",
                    "parameter": "MainMenuScene",
                    "parameterType": "String"
                  }
                ]
              }
            ]
          }
        ]
      }
      """;

  @BeforeEach
  void setUp() {
    myGameSceneParser = new GameSceneParser();
    myMapper = new ObjectMapper();
  }

  @Test
  void parse_validJson_properlyParses() throws JsonProcessingException, ParsingException {
    JsonNode node = myMapper.readTree(goodJsonString);
    myGameSceneParser.parse(node);
    assertEquals("Test Scene", node.get("Name").textValue());
    assertTrue(node.has("GameObjects"));
  }

  @Test
  void parse_invalidJson_throwsError() throws JsonProcessingException, ParsingException {
    JsonNode node = myMapper.readTree(badJsonString);
    // Will throw error for not having Name field
    assertThrows(ParsingException.class, () -> myGameSceneParser.parse(node));
  }

  @Test
  void write() {
    // TODO: Write once we have a concrete game scenes
  }
}