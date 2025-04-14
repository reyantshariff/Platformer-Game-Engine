package oogasalad.model.profile;

public class SessionException extends Exception {

/**
 * Exception thrown when attempting to access the current session and it fails
 */
  public SessionException(String message) {
    super(message);
  }

}
