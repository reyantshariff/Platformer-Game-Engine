package oogasalad.model.profile;

import com.google.cloud.Timestamp;
import oogasalad.model.config.PasswordHashingException;

/**
 * This class represents the data of a player profile. It a representation of the data stored in the database
 * This would be a record, but the firebase deserialization doesn't work well with records.
 *
 * @author Justin Aronwald
 */
public class PlayerData {
  private String username;
  private String fullName;
  private Password password;
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
  public PlayerData(String username, String fullName, Password password, Timestamp createdAt) {
    this.username = username;
    this.password = password;
    this.createdAt = createdAt;
    this.fullName = fullName;
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
   * Getter for the full-name
   *
   * @return - the full name of the user
   */
  public String getFullName() {
    return fullName;
  }

  /**
   * Setter for the full name
   *
   * @param fullName - the full name of the users
   */
  public void setFullName(String fullName) {
    this.fullName = fullName;
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

  /**
   * Method that delegates verification logic to Password class
   *
   * @param input - the password being tried
   * @return - true if the password is verified
   * @throws PasswordHashingException - if failure occurs throughout the process
   */
  public boolean verifyPassword(String input) throws PasswordHashingException {
    return password.verify(input);
  }

  /**
   * A getter exclusively to be used internally or for Firebase
   *
   * @return - the Password object
   */
  public Password getPassword() {
    return password;
  }

  /**
   * Setter for the password -- exclusively to be used internally or for Firebase
   *
   * @param password - the Password object for a user
   */
  public void setPassword(Password password) {
    this.password = password;
  }

  /**
   * Override the toString to protect the password
   *
   * @return - a random string to protect the password
   */
  @Override
  public String toString() {
    return "[PROTECTED]";
  }


}