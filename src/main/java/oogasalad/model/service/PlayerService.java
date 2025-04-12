package oogasalad.model.service;

import static oogasalad.model.config.GameConfig.LOGGER;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import oogasalad.database.DatabaseException;
import oogasalad.database.FirebaseManager;

/**
 * Service to connect Model and Controller to pass/receive API calls to database
 *
 * @author Justin Aronwald
 */
public class PlayerService {

  /**
   * Creates a new player profile with a given username
   *
   * @param username - unique name for a user
   * @return - a boolean on the success of the API call
   * @throws DatabaseException - an error highlighting an issue with database access or putting
   */
  public static boolean createNewPlayer(String username) throws DatabaseException {
    try {
      Firestore db = FirebaseManager.getDB();
      DocumentReference playersRef = db.collection("players").document(username);

      DocumentSnapshot playersSnapshot = playersRef.get().get();
      if (playersSnapshot.exists()) {
        LOGGER.error("Player {} already exists", username);
        throw new DatabaseException("Player already exists");
      }

      Map<String, Object> playerData = new HashMap<>();

      playerData.put("username", username);
      playerData.put("createdAt", FieldValue.serverTimestamp());

      ApiFuture<WriteResult> playersApiFuture = playersRef.set(playerData);
      playersApiFuture.get();
      LOGGER.info("Successfully created player {}", username);
      return true;

    } catch (ExecutionException | InterruptedException e) {
      throw new DatabaseException("Failed to create player: ", e);
    }
  }

  /**
   * Deletes a player with a given username
   *
   * @param username - unique name for a user
   * @return - a boolean on the success of the deleting of a user
   * @throws DatabaseException - an error highlighting an issue with database access or putting
   */
  public static boolean deletePlayer(String username) throws DatabaseException {
    try {
      Firestore db = FirebaseManager.getDB();
      DocumentReference playersRef = db.collection("players").document(username);

      DocumentSnapshot playersSnapshot = playersRef.get().get();
      if (!playersSnapshot.exists()) {
        LOGGER.warn("Player {} does not exist", username);
        throw new DatabaseException("Player does not exist");
      }
      
      playersRef.delete().get();
      LOGGER.info("Successfully deleted player {}", username);
      return true;

    } catch (ExecutionException | InterruptedException e) {
      throw new DatabaseException("Failed to delete player: " + username, e);
    }

  }
}
