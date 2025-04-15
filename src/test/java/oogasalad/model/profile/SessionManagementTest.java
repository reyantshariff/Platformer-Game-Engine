package oogasalad.model.profile;

import static org.junit.jupiter.api.Assertions.*;

import com.google.cloud.Timestamp;
import oogasalad.model.config.PasswordHashingException;
import org.junit.jupiter.api.Test;

class SessionManagementTest {


  @Test
  public void login_normalUser_loggedIn() throws PasswordHashingException, SessionException {
    Password password = Password.fromPlaintext("testPassword");
    Timestamp createdAt = Timestamp.now();
    String username = "justin";
    String fullName = "Justin Aronwald";

    PlayerData testUser = new PlayerData(username, fullName, password, createdAt);
    SessionManagement.login(testUser);

    assertEquals(testUser, SessionManagement.getCurrentUser());
    SessionManagement.logout();
    assertThrows(SessionException.class, SessionManagement::getCurrentUser);
  }

  @Test
  public void isLoggedIn_normalUser_returnsTrue() throws PasswordHashingException, SessionException {
    Password password = Password.fromPlaintext("testPassword");
    Timestamp createdAt = Timestamp.now();
    String username = "justin";
    String fullName = "Justin Aronwald";

    assertFalse(SessionManagement.isLoggedIn());

    PlayerData testUser = new PlayerData(username, fullName, password, createdAt);
    SessionManagement.login(testUser);

    assertTrue(SessionManagement.isLoggedIn());
    assertEquals(testUser.getUsername(), SessionManagement.getCurrentUser().getUsername());
  }

}