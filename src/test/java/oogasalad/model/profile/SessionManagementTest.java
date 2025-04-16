package oogasalad.model.profile;

import static org.junit.jupiter.api.Assertions.*;

import com.google.cloud.Timestamp;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import oogasalad.database.DatabaseException;
import oogasalad.database.FirebaseManager;
import oogasalad.model.config.PasswordHashingException;
import oogasalad.model.service.PlayerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SessionManagementTest {
  @BeforeEach
  @AfterEach
  public void cleanUp() {
    SessionManagement.logout(); // reset session
    try {
      Files.deleteIfExists(Paths.get("rememberme.properties"));
    } catch (IOException e) {
      System.err.println("Error cleaning up user properties: " + e.getMessage());
    }
  }

  @Test
  public void login_normalUser_loggedIn()
      throws PasswordHashingException, SessionException, IOException {
    Password password = Password.fromPlaintext("testPassword");
    Timestamp createdAt = Timestamp.now();
    String username = "justin";
    String fullName = "Justin Aronwald";

    PlayerData testUser = new PlayerData(username, fullName, password, createdAt);
    SessionManagement.login(testUser, false);

    assertEquals(testUser, SessionManagement.getCurrentUser());
    SessionManagement.logout();
    assertThrows(SessionException.class, SessionManagement::getCurrentUser);
  }

  @Test
  public void isLoggedIn_normalUser_returnsTrue()
      throws PasswordHashingException, SessionException, IOException {
    Password password = Password.fromPlaintext("testPassword");
    Timestamp createdAt = Timestamp.now();
    String username = "justin";
    String fullName = "Justin Aronwald";

    assertFalse(SessionManagement.isLoggedIn());

    PlayerData testUser = new PlayerData(username, fullName, password, createdAt);
    SessionManagement.login(testUser, false);

    assertTrue(SessionManagement.isLoggedIn());
    assertEquals(testUser.getUsername(), SessionManagement.getCurrentUser().getUsername());
  }

  @Test
  public void rememberMe_loginAndAutoLogin_Success()
      throws PasswordHashingException, IOException {
    FirebaseManager.initializeFirebase();

    try {
      String username = "rememberme_user_" + System.currentTimeMillis();
      String password = "securePassword";
      String fullName = "Remember Me Test";

      assertTrue(PlayerService.createNewPlayer(username, password, fullName));

      PlayerService.login(username, password, true);
      assertTrue(SessionManagement.tryAutoLogin());

      SessionManagement.logout();
      assertFalse(SessionManagement.isLoggedIn());

      assertTrue(PlayerService.deletePlayer(username));
      SessionManagement.logout();
      assertFalse(SessionManagement.tryAutoLogin());
    } catch (DatabaseException | IOException e) {
      System.out.println(e.getMessage());
    }
  }

}