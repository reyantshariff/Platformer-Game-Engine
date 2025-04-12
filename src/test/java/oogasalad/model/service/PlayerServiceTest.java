package oogasalad.model.service;

import oogasalad.database.DatabaseException;
import oogasalad.database.FirebaseManager;
import oogasalad.model.profile.PlayerData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerServiceTest {

  @BeforeAll
  public static void setUp() throws Exception {
    FirebaseManager.initializeFirebase();
  }

  @Test
  public void createNewPlayer_creatingANewPlayer_Success() {
    String username = "testuser_" + System.currentTimeMillis();

    assertDoesNotThrow(() -> {
      PlayerService.createNewPlayer(username);
    });
  }

  @Test
  public void createNewPlayer_Duplicate_ThrowsError() throws DatabaseException {
    String username = "duplicateUser_" + System.currentTimeMillis();

    boolean result = PlayerService.createNewPlayer(username);
    assertTrue(result);
    Exception exception = assertThrows(DatabaseException.class, () -> PlayerService.createNewPlayer(username));

    assertTrue(exception.getMessage().contains("Player already exists"));
  }

  @Test
  public void deletePlayer_DeletingAPlayer_Success() throws DatabaseException {
    String username = "testuser_" + System.currentTimeMillis();
    boolean result = PlayerService.createNewPlayer(username);
    assertTrue(result);

    boolean deleteResult = PlayerService.deletePlayer(username);
    assertTrue(deleteResult);
  }

  @Test
  public void getPlayerByUsername_FetchPlayer_Success() throws DatabaseException {
    String username = "justin";
    boolean result = PlayerService.createNewPlayer(username);
    assertTrue(result);

    PlayerData player = PlayerService.getPlayerByUsername(username);
    assertEquals(username, player.getUsername());
  }

}