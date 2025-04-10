package oogasalad.model.parser;

import com.fasterxml.jackson.databind.JsonNode;
import java.awt.Dimension;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.architecture.GameInfo;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.parser.JsonParser;
import oogasalad.view.player.dinosaur.DinosaurGameScene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JSON Parser Test. Cannot test without some other concrete stuff being created.
 * <p>
 * Author: Daniel Rodriguez-Florido
 */
class JsonParserTest {

  JsonParser parser;
  JsonNode myJsonNode;

  @BeforeEach
  void setUp() {
    parser = new JsonParser("Dinosaur1");
    Game myGame = new Game();
    myGame.setGameInfo(new GameInfo("NAME1", "DESCRIPTION1", "RODFLO", new Dimension(0, 0)));
 // TODO: Finish class once this function is done
  }

  @Test
  void parse() {
  }

  @Test
  void write() {
  }
}