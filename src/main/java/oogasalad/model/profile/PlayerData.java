package oogasalad.model.profile;

import com.google.cloud.Timestamp;

/**
 * This class represents the data of a player profile. It a representation of the data stored in the database
 * This would be a record, but the firebase deserialization doesn't work well with records.
 *
 * @author Justin Aronwald
 */
public class PlayerData {
  private String username;
  private Timestamp createdAt;

  /**
   * This a required constructor for the Firestone deserialization
   *
   */
  public PlayerData() {
    //This is a required no-arg constructor for Firestore deserialization
  }

  /**
   * PlayerData constructor for creating a new player
   *
   * @param username - the unique name for a user
   * @param createdAt - the time the player was created
   */
  public PlayerData(String username, Timestamp createdAt) {
    this.username = username;
    this.createdAt = createdAt;
  }

  /**
   * Getter for the username
   *
   * @return - the unique name for a user
   */
  public String getUsername() {
    return username;
  }

  /**
   * Setter for the username of a player
   *
   * @param username - the unique name for a user
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Getter for the time a player was created
   *
   * @return - the time at which a player was created
   */
  public Timestamp getCreatedAt() {
    return createdAt;
  }

  /**
   * Setter for the time of which a player is created
   *
   * @param createdAt - the timestamp at which a player is created
   */
  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }
}