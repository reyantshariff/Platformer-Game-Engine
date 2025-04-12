package oogasalad.model.config;

import static oogasalad.model.config.GameConfig.getText;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import oogasalad.database.DatabaseException;
import oogasalad.database.FirebaseManager;

/**
 * A config file with public methods that can be used univerally across all services
 *
 */
public class ProfileServiceConfig {

  /**
   * Returns a Firestore DocumentReference for the given document ID (in Table terms, a reference to a row)
   *
   * @param documentId the document ID
   * @param COLLECTION_NAME - the name of the collection to get the information
   * @return DocumentReference - a specific row reference in the firestone database
   */
  public static DocumentReference getDocumentRef(String documentId, String COLLECTION_NAME) throws DatabaseException {
    try {
      Firestore db = FirebaseManager.getDB();
      return db.collection(COLLECTION_NAME).document(documentId);
    } catch (Exception e) {
      throw new DatabaseException("Failed to get document reference", e);
    }
  }

  /**
   * Retrieves the DocumentSnapshot for the given document ID (the query result row)
   *
   * @param documentId the document ID
   * @param COLLECTION_NAME - the name of the collection to get the information
   * @return the DocumentSnapshot object
   * @throws DatabaseException if the document could not be fetched
   */
  public static DocumentSnapshot getDocument(String documentId, String COLLECTION_NAME) throws DatabaseException {
    try {
      return getDocumentRef(documentId, COLLECTION_NAME).get().get();
    } catch (ExecutionException | InterruptedException e) {
      throw new DatabaseException("Failed to fetch document: " + documentId, e);
    }
  }

  /**
   * Checks whether a document with the given ID exists in the players collection
   *
   * @param documentId the document ID to check
   * @param COLLECTION_NAME - the name of the collection to get the information
   * @return true if the document exists; false otherwise
   * @throws DatabaseException if the document could not be checked
   */
  public static boolean documentExists(String documentId, String COLLECTION_NAME) throws DatabaseException {
    return getDocument(documentId, COLLECTION_NAME).exists();
  }


  /**
   * adds a document with the given ID and data to the players collection
   *
   * @param documentId the document ID (username)
   * @param data the data to write
   * @param COLLECTION_NAME - the name of the collection to get the information
   * @throws DatabaseException if the document could not be written
   */
  public static void addToDatabase(String documentId, String COLLECTION_NAME, Map<String, Object> data) throws DatabaseException {
    try {
      getDocumentRef(documentId, COLLECTION_NAME).set(data).get();
    } catch (ExecutionException | InterruptedException e) {
      throw new DatabaseException(getText("databaseAddError", documentId), e);
    }
  }

  /**
   * Deletes a document with the given ID from the 'layers collection
   *
   * @param documentId the document ID to delete
   * @param COLLECTION_NAME - the name of the collection to get the information
   * @throws DatabaseException if the document could not be deleted
   */
  public static void deleteFromDatabase(String documentId, String COLLECTION_NAME) throws DatabaseException {
    try {
      getDocumentRef(documentId, COLLECTION_NAME).delete().get();
    } catch (ExecutionException | InterruptedException e) {
      throw new DatabaseException(getText("databaseDeleteError", documentId), e);
    }
  }

}
