package oogasalad.model.parser;

import static oogasalad.model.config.GameConfig.LOGGER;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import oogasalad.model.engine.base.architecture.Game;
import org.apache.logging.log4j.Level;

/**
 * Json Parser Class. Highest level of hierarchy (JsonParser uses GameParser uses SceneParser uses
 * GameObjectParser uses Component/BehaviorParser
 * <p>
 * Author: Daniel Rodriguez-Florido
 */

public class JsonParser implements Parser {

  private static final String FILE_PATH = "./src/main/gameFiles/";
  private static final GameParser GAME_PARSER = new GameParser();

  private final ObjectMapper mapper;
  private final String fileName;

  /**
   * Constructor to create parser that can read and write.
   *
   * @param fileName Either the path of the file you want to read or the name of the file to be
   *                 saved. Automatically appended .json at end.
   */
  public JsonParser(String fileName) {
    this.fileName = FILE_PATH + fileName + ".json";
    mapper = new ObjectMapper();
  }

  /**
   * @param node - the JSON node given to parse
   * @return The Game object to run
   * @throws ParsingException if cannot find file or if error processing file
   */
  @Override
  public Game parse(JsonNode node) throws ParsingException {
    JsonNode rootNode;
    try {
      rootNode = mapper.readTree(new File(fileName));
    } catch (FileNotFoundException e) {
      LOGGER.error("Could not parse file {}. File was not found.", fileName);
      throw new ParsingException("Could not parse file " + fileName);
    } catch (IOException e) {
      LOGGER.error("Could not parse file {}. Is a file corrupted?", fileName);
      throw new ParsingException("Could not parse file " + fileName);
    }

    return GAME_PARSER.parse(rootNode);
  }

  /**
   * @param data - the Game object that we wish to write to JSON
   * @return JsonNode of the game, indicating success
   * @throws IOException
   */
  @Override
  public JsonNode write(Object data) throws IOException {
    Game game = (Game) data;
    JsonNode gameNode = null;

    try {
      gameNode = GAME_PARSER.write(game);
      File outputFile = new File(FILE_PATH, fileName);
      mapper.writeValue(outputFile, gameNode);
    } catch (DatabindException e) {
      LOGGER.error("Could not write file {}. Error mapping to JSON.", fileName);
    }

    handleNullGameNode(gameNode);

    LOGGER.log(Level.INFO, "Created game file {}.", fileName);

    return gameNode;
  }

  private void handleNullGameNode(JsonNode gameNode) throws IOException {
    if (gameNode == null) {
      LOGGER.error("Could not write file {}. Is a file corrupted?", fileName);
      throw new IOException("GameNode unable to be initialized. Could not generate file.");
    }
  }


}
