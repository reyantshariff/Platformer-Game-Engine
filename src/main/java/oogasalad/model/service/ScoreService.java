package oogasalad.model.service;

import static oogasalad.model.config.GameConfig.getText;
import static oogasalad.model.config.ProfileServiceConfig.addToDatabase;
import static oogasalad.model.config.GameConfig.LOGGER;
import static oogasalad.model.config.ProfileServiceConfig.deleteFromDatabase;
import static oogasalad.model.config.ProfileServiceConfig.documentExists;
import static oogasalad.model.config.ProfileServiceConfig.getDocument;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;
import oogasalad.database.DatabaseException;
import oogasalad.model.profile.ScoreData;

/**
 * PlayerService methods for creating, retrieving, and deleting player profiles from the Firestore
 * database
 *
 * @author Justin Aronwald
 */
public class ScoreService {

  private static final String COLLECTION_NAME = "scores";
  private static final String SCORE_NOT_EXIST_ERROR_MESSAGE = "scoreNotExistError";

  /**
   * Checks for an existing high score, and if the new score is bigger, then it saves it.
   *
   * @param username - the unique username of the player
   * @param game     - the String name of the game in which the high score was achieved
   * @param newScore - the newScore achiever
   * @return - true if the score was updated/saved, false if not necessary
   * @throws DatabaseException - if the player already exists or a database error occurs
   */
  public static boolean saveHighScore(String username, String game, int newScore)
      throws DatabaseException {
    String docId = username + "_" + game;

    DocumentSnapshot curScore = getDocument(docId, COLLECTION_NAME);

    if (curScore.exists()) {
      Long existingScore = curScore.getLong("score");

      if (existingScore != null && existingScore >= newScore) {
        LOGGER.info(getText("existingScoreMessage", existingScore, username, game, newScore));
        return false;
      }
    }

    return saveOrUpdateScore(username, game, newScore, docId);
  }

  /**
   * Works to either save a field if there isn't one already, otherwise it overwrites it.
   */
  private static boolean saveOrUpdateScore(String username, String game, int newScore, String docId)
      throws DatabaseException {
    Map<String, Object> scoreData = new HashMap<>();
    scoreData.put("username", username);
    scoreData.put("score", newScore);
    scoreData.put("game", game);
    scoreData.put("createdAt", FieldValue.serverTimestamp());

    addToDatabase(docId, COLLECTION_NAME, scoreData);
    LOGGER.info(getText("saveUpdateScoreMessage", newScore, username, game));
    return true;
  }

  /**
   * Deletes the score associated with the given game name and username
   *
   * @param username - the unique username of the player to delete
   * @param game     - the game name (String)
   * @return - true if the player was successfully deleted
   * @throws DatabaseException - if the player does not exist or a database error occurs
   */
  public static boolean deleteScore(String username, String game) throws DatabaseException {
    String docId = username + "_" + game;

    if (!documentExists(docId, COLLECTION_NAME)) {
      LOGGER.warn(getText(SCORE_NOT_EXIST_ERROR_MESSAGE, docId, COLLECTION_NAME));
      return false;
    }

    deleteFromDatabase(docId, COLLECTION_NAME);
    LOGGER.info(getText("deletePlayerSuccess", username));
    return true;
  }

  /**
   * Gets the ScoreData for a given username and game
   *
   * @param username - the unique username of the player to delete
   * @param game     - the game name (String)
   * @return - a ScoreData object containing all the relevant information about the high score
   * @throws DatabaseException - if the player does not exist or a database error occurs
   */
  public static ScoreData getHighScore(String username, String game) throws DatabaseException {
    String docId = username + "_" + game;
    DocumentSnapshot highScore = getDocument(docId, COLLECTION_NAME);
    if (!highScore.exists()) {
      LOGGER.warn(getText(SCORE_NOT_EXIST_ERROR_MESSAGE, docId, COLLECTION_NAME));
      throw new DatabaseException(getText(SCORE_NOT_EXIST_ERROR_MESSAGE, docId, COLLECTION_NAME));
    }
    return highScore.toObject(ScoreData.class);

  }
}
