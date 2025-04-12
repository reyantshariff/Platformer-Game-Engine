package oogasalad.database;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FirebaseManagerTest {
  @Test
  void testFirebaseInitialization() {
    assertDoesNotThrow(FirebaseManager::initializeFirebase);
  }
}