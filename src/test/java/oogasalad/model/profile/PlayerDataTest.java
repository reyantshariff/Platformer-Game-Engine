package oogasalad.model.profile;

import static org.junit.jupiter.api.Assertions.*;

import com.google.cloud.Timestamp;
import org.junit.jupiter.api.Test;

class PlayerDataTest {
  @Test
  void constructor_WithValidUsernameAndTimestamp_ShouldSetFieldsCorrectly() {
    String username = "justin";
    Timestamp createdAt = Timestamp.now();

    PlayerData player = new PlayerData(username, createdAt);

    assertEquals(username, player.getUsername());
    assertEquals(createdAt, player.getCreatedAt());
  }

  @Test
  void setters_WithValidInput_ShouldUpdateFieldsCorrectly() {
    PlayerData player = new PlayerData();
    String username = "tester";
    Timestamp createdAt = Timestamp.now();

    player.setUsername(username);
    player.setCreatedAt(createdAt);

    assertEquals(username, player.getUsername());
    assertEquals(createdAt, player.getCreatedAt());
  }
}