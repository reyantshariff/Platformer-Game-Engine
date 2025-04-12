package oogasalad.model.service;

import static oogasalad.model.config.GameConfig.LOGGER;
import static oogasalad.model.config.ServiceConfig.documentExists;
import static oogasalad.model.config.ServiceConfig.getDocument;
import static oogasalad.model.config.ServiceConfig.getDocumentRef;

import com.google.cloud.firestore.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
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

  /**
   * Creates a new player with the given username
   *
   * @param username - the unique username of the player
   * @return - true if the player was successfully created
   * @throws DatabaseException - if the player already exists or a database error occurs
   */
  public static boolean createNewPlayer(String username) throws DatabaseException {
    if (documentExists(username, COLLECTION_NAME)) {
      LOGGER.error("Player {} already exists", username);
      throw new DatabaseException("Player already exists");
    }

    Map<String, Object> playerData = new HashMap<>();
    playerData.put("username", username);
    playerData.put("createdAt", FieldValue.serverTimestamp());

    addToDatabase(username, playerData);
    LOGGER.info("Created player {}", username);
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
      LOGGER.warn("Player {} does not exist", username);
      throw new DatabaseException("Player does not exist");
    }

    deleteFromDatabase(username);
    LOGGER.info("Successfully deleted player {}", username);
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
      LOGGER.warn("Player {} does not exist", username);
      throw new DatabaseException("Player does not exist");
    }
    return snapshot.toObject(PlayerData.class);
  }

  /**
   * adds a document with the given ID and data to the players collection
   *
   * @param documentId the document ID (username)
   * @param data the data to write
   * @throws DatabaseException if the document could not be written
   */
  private static void addToDatabase(String documentId, Map<String, Object> data) throws DatabaseException {
    try {
      getDocumentRef(documentId, COLLECTION_NAME).set(data).get();
    } catch (ExecutionException | InterruptedException e) {
      throw new DatabaseException("Failed to write document: " + documentId, e);
    }
  }

  /**
   * Deletes a document with the given ID from the 'layers collection
   *
   * @param documentId the document ID to delete
   * @throws DatabaseException if the document could not be deleted
   */
  private static void deleteFromDatabase(String documentId) throws DatabaseException {
    try {
      getDocumentRef(documentId, COLLECTION_NAME).delete().get();
    } catch (ExecutionException | InterruptedException e) {
      throw new DatabaseException("Failed to delete document: " + documentId, e);
    }
  }
}