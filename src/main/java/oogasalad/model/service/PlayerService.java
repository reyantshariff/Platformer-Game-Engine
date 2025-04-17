package oogasalad.model.service;

import static oogasalad.model.config.GameConfig.LOGGER;
import static oogasalad.model.config.GameConfig.getText;
import static oogasalad.model.config.ProfileServiceConfig.addToDatabase;
import static oogasalad.model.config.ProfileServiceConfig.deleteFromDatabase;
import static oogasalad.model.config.ProfileServiceConfig.documentExists;
import static oogasalad.model.config.ProfileServiceConfig.getDocument;

import com.google.cloud.firestore.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import oogasalad.database.DatabaseException;
import oogasalad.model.config.PasswordHashingException;
import oogasalad.model.profile.Password;
import oogasalad.model.profile.PlayerData;
import oogasalad.model.profile.SessionManagement;

/**
 * PlayerService methods for creating, retrieving, and deleting player profiles from the Firestore
 * database
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
   * @param password - the String password of the player
   * @param fullName - the concatenation of the first and last name of the player
   * @return - the playerData object
   * @throws DatabaseException - if the player already exists or a database error occurs
   */
  public static boolean createNewPlayer(String username, String password, String fullName)
      throws DatabaseException, PasswordHashingException {
    if (documentExists(username, COLLECTION_NAME)) {
      LOGGER.error(getText("playerExistsError"), username);
      throw new DatabaseException(getText("playerExistsError", username));
    }

    Map<String, Object> playerData = new HashMap<>();
    playerData.put("username", username);
    playerData.put("fullName", fullName);
    Password pass = Password.fromPlaintext(password);
    playerData.put("password", pass);
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
      throw new DatabaseException(
          getText(PLAYER_NOT_EXIST_ERROR_MESSAGE, username, COLLECTION_NAME));
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
   * @throws DatabaseException if the player does not exist or a database error occurs
   */
  public static PlayerData getPlayerByUsername(String username) throws DatabaseException {
    DocumentSnapshot snapshot = getDocument(username, COLLECTION_NAME);
    if (!snapshot.exists()) {
      LOGGER.warn(getText(PLAYER_NOT_EXIST_ERROR_MESSAGE, username, COLLECTION_NAME));
      throw new DatabaseException(
          getText(PLAYER_NOT_EXIST_ERROR_MESSAGE, username, COLLECTION_NAME));
    }
    return snapshot.toObject(PlayerData.class);
  }

  /**
   * Endpoint to authenticate and login a user -- adds user to sessionManagement
   *
   * @param username   - the inputted username
   * @param password   - the inputted password
   * @param rememberMe - true if rememberMe checkbox is selected
   */
  public static void login(String username, String password, boolean rememberMe)
      throws PasswordHashingException, IOException {
    PlayerData curUser;
    try {
      curUser = getPlayerByUsername(username);

      if (!curUser.verifyPassword(password)) {
        LOGGER.warn("Invalid password");
        throw new PasswordHashingException("Password is incorrect");
      }

      SessionManagement.login(curUser, rememberMe);
      LOGGER.info("User: {} has successfully logged in", username);
      //switch screens

    } catch (DatabaseException e) {
      LOGGER.error("Error, no user in the database", e);
      // show on the frontend
    } catch (IOException e) {
      throw new IOException("Failed to store autologin:", e);
    }
  }

  /**
   * Method to handle logging out -- removes the current user from session manager
   */
  public static void logout() {
    SessionManagement.logout();
    LOGGER.info("Logout successful");
    //switch scenes
  }

}