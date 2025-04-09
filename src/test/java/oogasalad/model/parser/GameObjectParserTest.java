package oogasalad.model.parser;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.prefab.dinosaur.Bird;
import oogasalad.model.parser.GameObjectParser;
import oogasalad.model.parser.ParsingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameObjectParserTest {

  GameObjectParser myGameObjectParser;
  ObjectMapper myMapper;

  String goodJsonString =
      """
          {
            "Name": "ExampleMenuBackground",
            "Tag": "Background",
            "Components": [
              {
                "Name": "Transform",
                "Configurations": {
                  "x": 0,
                  "y": 0,
                  "rotation": 0,
                  "scaleX": 1,
                  "scaleY": 1
                }
              }
            ],
            "BehaviorController": {
              "Behaviors": [
                {
                  "Name": "SceneChanger",
                  "constraints": [
                    {
                      "name": "KeyPressConstraint",
                      "parameter": "SPACE"
                    }
                  ],
                  "actions": [
                    {
                      "name": "ChangeSceneAction",
                      "parameter": "Example Main Scene"
                    }
                  ]
                }
              ]
            }
          }
          """;

  // The bad json string has no name, should return error
  String badJsonString =
      """
          {
            "Tag": "Background",
            "Components": [
              {
                "Name": "Transform",
                "Configurations": {
                  "x": 0,
                  "y": 0,
                  "rotation": 0,
                  "scaleX": 1,
                  "scaleY": 1
                }
              }
            ],
            "BehaviorController": {
              "Behaviors": [
                {
                  "Name": "SceneChanger",
                  "constraints": [
                    {
                      "name": "KeyPressConstraint",
                      "parameter": "SPACE"
                    }
                  ],
                  "actions": [
                    {
                      "name": "ChangeSceneAction",
                      "parameter": "Example Main Scene"
                    }
                  ]
                }
              ]
            }
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
//    assertTrue(gameObject.getName().contains("GameObject_"));
  }

  @Test
  void parse_invalidJson_throwsError() throws JsonProcessingException, ParsingException {
    JsonNode node = myMapper.readTree(badJsonString);
    assertThrows(ParsingException.class, () -> myGameObjectParser.parse(node));
  }

  @Test
  void write_validJson_writesGameObject() throws IOException {
    Bird bird = new Bird("Bird1");
    JsonNode node = null;
    try {
      node = myGameObjectParser.write(bird);
    } catch (IOException e) {
      throw new IOException(e);
    }
    assertNotNull(node);
    System.out.println(node);
    assertTrue(node.toString().contains("Name"));
    assertTrue(node.toString().contains("Bird"));
    assertTrue(node.toString().contains("Tag"));
    assertTrue(node.toString().contains("Components"));
    assertTrue(node.toString().contains("Behaviors"));
  }
}