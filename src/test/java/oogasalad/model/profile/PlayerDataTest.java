package oogasalad.model.profile;

import static org.junit.jupiter.api.Assertions.*;

import com.google.cloud.Timestamp;
import oogasalad.model.config.PasswordHashingException;
import org.junit.jupiter.api.Test;

class PlayerDataTest {
  @Test
  void constructor_WithValidInformation_ShouldSetFieldsCorrectly() throws PasswordHashingException {
    String username = "justin";
    String fullName = "Justin Aronwald";
    Timestamp createdAt = Timestamp.now();
    Password password = Password.fromPlaintext("secure123");


    PlayerData player = new PlayerData(username, fullName, password, createdAt);

    assertEquals(username, player.getUsername());
    assertEquals(createdAt, player.getCreatedAt());
    assertEquals(fullName, player.getFullName());
  }

  @Test
  void setters_WithValidInput_ShouldUpdateFieldsCorrectly() throws PasswordHashingException {
    PlayerData player = new PlayerData();
    String username = "tester";
    Timestamp createdAt = Timestamp.now();
    Password password = Password.fromPlaintext("secure123");
    String fullName = "Justin Aronwald";

    player.setUsername(username);
    player.setFullName(fullName);
    player.setPassword(password);
    player.setCreatedAt(createdAt);

    assertEquals(username, player.getUsername());
    assertEquals(createdAt, player.getCreatedAt());
    assertEquals(fullName, player.getFullName());
  }
}