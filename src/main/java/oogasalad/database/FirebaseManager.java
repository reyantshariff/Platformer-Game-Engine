package oogasalad.database;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Initialize a Firebase database for the project
 *
 * @author Justin Aronwald
 */
public class FirebaseManager {
  private static Firestore db;

  /**
   * Initializes the Firebase app using the service JSON file
   * This should only be called once during application startup.
   *
   * @throws IOException - if the credential file cannot be read, throws error
   */
  public static void initializeFirebase() throws IOException {
    FileInputStream serviceAccount = new FileInputStream("data/DatabaseInformation/oogasalad-a908c-firebase-adminsdk-fbsvc-73ed2b05e6.json");

    FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build();

    FirebaseApp.initializeApp(options);
    db = FirestoreClient.getFirestore();
  }

  /**
   * Returns the Firestone database instance
   * This should only be called after initialization has been done
   *
   * @return - the initialized instance
   */
  public static Firestore getDB() {
    return db;
  }
}