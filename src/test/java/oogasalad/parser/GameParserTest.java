package oogasalad.parser;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import oogasalad.engine.base.architecture.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameParserTest {

  final static String TEST_FILE_NAME = "src/test/resources/jsonTestFiles/testFile1.json";
  GameParser parser;

  @BeforeEach
  void setUp() {
    parser = new GameParser(TEST_FILE_NAME);
  }

  @Test
  void parse() {
    Game myGame = parser.getMyGame();
    assertNotNull(myGame);
  }

  @Test
  void write() {
  }
}