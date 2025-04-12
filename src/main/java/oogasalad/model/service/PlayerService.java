package oogasalad.model.service;

import static oogasalad.model.config.GameConfig.LOGGER;
import static oogasalad.model.config.GameConfig.getText;
import static oogasalad.model.config.ProfileServiceConfig.addToDatabase;
import static oogasalad.model.config.ProfileServiceConfig.deleteFromDatabase;
import static oogasalad.model.config.ProfileServiceConfig.documentExists;
import static oogasalad.model.config.ProfileServiceConfig.getDocument;

import com.google.cloud.firestore.*;
import java.util.HashMap;
import java.util.Map;
import oogasalad.database.DatabaseException;
import oogasalad.model.profile.PlayerData;

/**
 * PlayerService methods for creating, retrieving, and deleting player profiles
 * from the Firestore database
 *
 * @author Justin Aronwald
 */
public class PlayerService {

  private static final String COLLECTION_NAME = "players";
  private static final String PLAYER_NOT_EXIST_ERROR_MESSAGE = "playerNotExistError";

  /**
   * Creates a new player with the given username
   *
   * @param username - the unique username of the player
   * @return - true if the player was successfully created
   * @throws DatabaseException - if the player already exists or a database error occurs
   */
  public static boolean createNewPlayer(String username) throws DatabaseException {
    if (documentExists(username, COLLECTION_NAME)) {
      LOGGER.error(getText("playerExistsError"), username);
      throw new DatabaseException(getText("playerExistsError", username));
    }

    Map<String, Object> playerData = new HashMap<>();
    playerData.put("username", username);
    playerData.put("createdAt", FieldValue.serverTimestamp());

    addToDatabase(username, COLLECTION_NAME, playerData);
    LOGGER.info(getText("createPlayerMessage"), username);
    return true;
  }

  /**
   * Deletes the player associated with the given username
   *
   * @param username - the unique username of the player to delete
   * @return - true if the player was successfully deleted
   * @throws DatabaseException - if the player does not exist or a database error occurs
   */
  public static boolean deletePlayer(String username) throws DatabaseException {
    if (!documentExists(username, COLLECTION_NAME)) {
      LOGGER.warn(getText(PLAYER_NOT_EXIST_ERROR_MESSAGE), username, COLLECTION_NAME);
      throw new DatabaseException(getText(PLAYER_NOT_EXIST_ERROR_MESSAGE, username, COLLECTION_NAME));
    }

    deleteFromDatabase(username, COLLECTION_NAME);
    LOGGER.info(getText("deletePlayerSuccess", username));
    return true;
  }

  /**
   * Gets the PlayerData for the given username
   *
   * @param username - the unique username of the player
   * @return - the PlayerData object, if it exists
   * @throws DatabaseException   if the player does not exist or a database error occurs
   */
  public static PlayerData getPlayerByUsername(String username) throws DatabaseException {
    DocumentSnapshot snapshot = getDocument(username, COLLECTION_NAME);
    if (!snapshot.exists()) {
      LOGGER.warn(getText("playerNotExistError", username, COLLECTION_NAME));
      throw new DatabaseException(getText("playerNotExistError", username, COLLECTION_NAME));
    }
    return snapshot.toObject(PlayerData.class);
  }

}