package oogasalad.model.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import oogasalad.model.engine.base.behavior.Behavior;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.beans.BeanHelper;

class BehaviorParserTest {

  static final int CONSTRAINTS_INDEX = 1;
  static final int ACTIONS_INDEX = 2;

  BehaviorParser myBehaviorParser;
  ObjectMapper myMapper;

  @BeforeEach
  void setUp() {
    myBehaviorParser = new BehaviorParser();
    myMapper = new ObjectMapper();
  }

  @Test
  void parse_validJson_success() throws JsonProcessingException, ParsingException {
    String goodJsonString =
        """
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
            """;
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
                  "parameter": "SPACE"
                },
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
                  "name": "ChangeSceneAction",
                  "parameter": "Example Main Scene"
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
                  "parameter": "SPACE"
                }
              ],
              "actions": [
                {
                  "name": "ChangeSceneAction",
                  "parameter": "Example Main Scene"
                },
                {
                  "name": "ChangeSceneAction",
                  "parameter": "Example Main Scene"
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
                  "parameter": "SPACE"
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
            """;

    JsonNode node = myMapper.readTree(badJsonString);
    assertThrows(ParsingException.class, () -> myBehaviorParser.parse(node));
  }

  // TODO: Create write function
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