package oogasalad.model.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import oogasalad.model.engine.base.behavior.Behavior;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BehaviorParserTest {

  static final int CONSTRAINTS_INDEX = 1;
  static final int ACTIONS_INDEX = 2;
  static final String CONSTRAINTS = "constraints";
  static final String ACTIONS = "actions";

  BehaviorParser myBehaviorParser;
  ObjectMapper myMapper;

  String goodJsonString =
      """
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
                "name": "ChangeViewSceneAction",
                "parameter": "Example Main Scene",
                "parameterType": "String"
              }
            ]
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
    Behavior behavior = myBehaviorParser.parse(node);
    assertNotNull(behavior); // Once we have actual behavior classes this should work
    behavior
        .getSerializedFields()
        .forEach(Assertions::assertNotNull);
  }

  @Test
  void parse_validJsonRepeatConstraints_success() throws JsonProcessingException, ParsingException {
    String repeatConstraintsJsonString =
        """
            {
              "Name": "SceneChanger",
              "constraints": [
                {
                  "name": "KeyPressConstraint",
                  "parameter": "SPACE",
                  "parameterType": "KeyCode"
                },
                {
                  "name": "KeyPressConstraint",
                  "parameter": "SPACE",
                  "parameterType": "KeyCode"
                }
              ],
              "actions": [
                {
                  "name": "ChangeViewSceneAction",
                  "parameter": "Example Main Scene",
                  "parameterType": "String"
                }
              ]
            }
            """;
    JsonNode node = myMapper.readTree(repeatConstraintsJsonString);
    Behavior behavior = myBehaviorParser.parse(node);

    assertNotNull(behavior);

    behavior
        .getSerializedFields()
        .forEach(Assertions::assertNotNull);

    assertFalse(behavior
        .getSerializedFields()
        .get(CONSTRAINTS_INDEX).toString().contains(",")); // Checks that list only has one element
  }

  @Test
  void parse_validJsonNoConstraints_success() throws JsonProcessingException, ParsingException {
    String noConstraintsJson =
        """
            {
              "Name": "SceneChanger",
              "constraints": [
              ],
              "actions": [
                {
                  "name": "ChangeViewSceneAction",
                  "parameter": "Example Main Scene",
                  "parameterType": "String"
                }
              ]
            }
            """;
    JsonNode node = myMapper.readTree(noConstraintsJson);
    Behavior behavior = myBehaviorParser.parse(node);

    assertNotNull(behavior);

    behavior
        .getSerializedFields()
        .forEach(Assertions::assertNotNull);
  }

  @Test
  void parse_validJsonRepeatActions_success() throws JsonProcessingException, ParsingException {
    String repeatActionsJsonString =
        """
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
                  "name": "ChangeViewSceneAction",
                  "parameter": "Example Main Scene",
                  "parameterType": "String"
                },
                {
                  "name": "ChangeViewSceneAction",
                  "parameter": "Example Main Scene",
                  "parameterType": "String"
                }
              ]
            }
            """;
    JsonNode node = myMapper.readTree(repeatActionsJsonString);
    Behavior behavior = myBehaviorParser.parse(node);

    assertNotNull(behavior);

    behavior
        .getSerializedFields()
        .forEach(Assertions::assertNotNull);

    assertFalse(behavior
        .getSerializedFields()
        .get(ACTIONS_INDEX).toString().contains(",")); // Checks that list only has one element
  }

  @Test
  void parse_validJsonNoActions_success() throws JsonProcessingException, ParsingException {
    String noActionsJsonString =
        """
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
              ]
            }
            """;
    JsonNode node = myMapper.readTree(noActionsJsonString);
    Behavior behavior = myBehaviorParser.parse(node);

    assertNotNull(behavior);

    behavior
        .getSerializedFields()
        .forEach(Assertions::assertNotNull);
  }

  @Test
  void parse_invalidJson_failure() throws JsonProcessingException, ParsingException {
    // The bad json string has no name, should return error
    String badJsonString =
        """
            {
              "constraints": [
                {
                  "name": "OnKeyPressed",
                  "parameter": "SPACE",
                  "parameterType": "KeyCode"
                }
              ],
              "actions": [
                {
                  "name": "ChangeViewSceneAction",
                  "parameter": "Example Main Scene",
                  "parameterType": "String"
                }
              ]
            }
            """;

    JsonNode node = myMapper.readTree(badJsonString);
    assertThrows(ParsingException.class, () -> myBehaviorParser.parse(node));
  }

  @Test
  void write_validBehaviorOneOfEach_success() throws IOException, ParsingException {
    JsonNode node = myMapper.readTree(goodJsonString);
    Behavior behavior = myBehaviorParser.parse(node);

    JsonNode writeNode = myBehaviorParser.write(behavior);
    System.out.println(writeNode.toPrettyString());
    assertTrue(writeNode.has("Name"));
    assertTrue(writeNode.has(CONSTRAINTS));
    assertTrue(writeNode.has(ACTIONS));

    JsonNode constraints = writeNode.get("constraints");
    JsonNode oneConstraint = constraints.get(0);
    assertTrue(oneConstraint.has("name"));
    assertTrue(oneConstraint.has("parameter"));
    assertTrue(oneConstraint.has("parameterType"));

    JsonNode actions = writeNode.get(ACTIONS);
    JsonNode oneAction = actions.get(0);
    assertTrue(oneAction.has("name"));
    assertTrue(oneAction.has("parameter"));
    assertTrue(oneAction.has("parameterType"));
  }

  @Test
  void write_validBehaviorMultipleConstraintsAndBehaviors_success()
      throws IOException, ParsingException {
    String repeatConstraintsAndActionsJsonString =
        """
             {
                "Name": "JumpWhenPressed",
                "constraints": [
                  {
                    "name": "KeyPressConstraint",
                    "parameter": "SPACE",
                    "parameterType": "KeyCode"
                  },
                  {
                    "name": "TouchingFromAboveConstraint",
                    "parameter": "Ground",
                    "parameterType": "String"
                  }
                ],
                "actions": [
                  {
                    "name": "JumpAction",
                    "parameter": 500,
                    "parameterType": "Double"
                  },
                  {
                    "name": "CrouchAction",
                    "parameter": "",
                    "parameterType": "String"
                   }
                ]
              }
            """;
    JsonNode node = myMapper.readTree(repeatConstraintsAndActionsJsonString);
    Behavior behavior = myBehaviorParser.parse(node);

    JsonNode writeNode = myBehaviorParser.write(behavior);
    System.out.println(writeNode.toPrettyString());
    assertTrue(writeNode.has("Name"));
    assertTrue(writeNode.has(CONSTRAINTS));
    assertTrue(writeNode.has(ACTIONS));

    JsonNode constraints = writeNode.get("constraints");

    JsonNode firstConstraint = constraints.get(0);
    assertTrue(firstConstraint.has("name"));
    assertTrue(firstConstraint.has("parameter"));
    assertTrue(firstConstraint.has("parameterType"));

    JsonNode secondConstraint = constraints.get(1);
    assertTrue(secondConstraint.has("name"));
    assertTrue(secondConstraint.has("parameter"));
    assertTrue(secondConstraint.has("parameterType"));

    JsonNode actions = writeNode.get(ACTIONS);
    JsonNode oneAction = actions.get(0);
    assertTrue(oneAction.has("name"));
    assertTrue(oneAction.has("parameter"));
    assertTrue(oneAction.has("parameterType"));
  }

  @Test
  void write_invalidBehaviorBadActionParameterType_failure() throws IOException, ParsingException {
    String badTypeJsonString =
        """
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
                  "name": "ChangeViewSceneAction",
                  "parameter": "Example Main Scene",
                  "parameterType": "OogaObject"
                }
              ]
            }
            """;
    JsonNode node = myMapper.readTree(badTypeJsonString);
    assertThrows(ParsingException.class, () -> myBehaviorParser.parse(node));
  }

  @Test
  void write_invalidBehaviorBadConstraintParameterType_failure()
      throws IOException, ParsingException {
    String badTypeJsonString =
        """
              {
              "Name": "SceneChanger",
              "constraints": [
                {
                  "name": "KeyPressConstraint",
                  "parameter": "SPACE",
                  "parameterType": "OogasaladObject"
                }
              ],
              "actions": [
                {
                  "name": "ChangeViewSceneAction",
                  "parameter": "Example Main Scene",
                  "parameterType": "String"
                }
              ]
            }
            """;
    JsonNode node = myMapper.readTree(badTypeJsonString);
    assertThrows(ParsingException.class, () -> myBehaviorParser.parse(node));
  }

}