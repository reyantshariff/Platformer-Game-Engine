package oogasalad.parser;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComponentParserTest {

  ComponentParser myComponentParser;
  ObjectMapper myMapper;

  String goodJsonString =
      """
      {
        "Name": "Transform",
        "Configurations": {
          "position": { "X": 0, "Y": 0 },
          "rotation": { "X": 0, "Y": 0 },
          "scale": { "X": 1, "Y": 1 }
        }
      }
      """;

  String badJsonString = """
      {
        "Configurations": {
          "position": { "X": 0, "Y": 0 },
          "rotation": { "X": 0, "Y": 0 },
          "scale": { "X": 1, "Y": 1 }
        }
      }
      """;

  @BeforeEach
  void setUp() {
    myComponentParser = new ComponentParser();
    myMapper = new ObjectMapper();
  }

  @Test
  void parse_validJsonFile_success() throws JsonProcessingException, ParsingException {
    JsonNode node = myMapper.readTree(goodJsonString);
    assertNotNull(myComponentParser.parse(node));
  }

  @Test
  void parse_invalidJsonFile_catchError() throws JsonProcessingException, ParsingException {
    JsonNode node = myMapper.readTree(badJsonString);
    assertThrows(ParsingException.class, () -> myComponentParser.parse(node));
  }

  @Test
  void write() {
  }
}