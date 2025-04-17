package oogasalad.model.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.component.BehaviorController;
import oogasalad.model.engine.component.Transform;
import oogasalad.model.engine.prefab.dinosaur.Bird;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameObjectParserTest {

  GameObjectParser myGameObjectParser;
  ObjectMapper myMapper;

  @BeforeEach
  void setUp() {
    myGameObjectParser = new GameObjectParser();
    myMapper = new ObjectMapper();
  }

  @Test
  void parse_validJson_readsGameObject() throws JsonProcessingException, ParsingException {
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
                        "parameter": "SPACE",
                        "parameterType": "KeyCode"
                      }
                    ],
                    "actions": [
                      {
                        "name": "ChangeSceneAction",
                        "parameter": "Example Main Scene",
                        "parameterType": "String"
                      }
                    ]
                  }
                ]
              }
            }
            """;

    JsonNode node = myMapper.readTree(goodJsonString);
    GameObject gameObject = myGameObjectParser.parse(node);
    assertNotNull(gameObject);
    assertTrue(gameObject.hasComponent(Transform.class));
    assertTrue(gameObject.hasComponent(BehaviorController.class));
    assertEquals("ExampleMenuBackground", gameObject.getName());
  }

  @Test
  void parse_invalidJson_throwsError() throws JsonProcessingException {
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
    JsonNode node = myMapper.readTree(badJsonString);
    assertThrows(ParsingException.class, () -> myGameObjectParser.parse(node));
  }

  @Test
  void parse_invalidJsonNoTransform_throwsError() throws JsonProcessingException {
    String noTransformJsonString =
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
    JsonNode node = myMapper.readTree(noTransformJsonString);
    assertThrows(ParsingException.class, () -> myGameObjectParser.parse(node));
  }

  @Test
  void write_validJson_writesGameObject() throws IOException {
    Bird bird = new Bird("Bird1");
    JsonNode node;
    try {
      node = myGameObjectParser.write(bird);
    } catch (IOException e) {
      throw new IOException(e);
    }
    assertNotNull(node);

    assertTrue(node.toString().contains("Name"));
    assertTrue(node.toString().contains("Bird"));
    assertTrue(node.toString().contains("Tag"));
    assertTrue(node.toString().contains("Components"));
    assertTrue(node.toString().contains("BehaviorController"));
  }
}