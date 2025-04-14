package oogasalad.model.profile;

/**
 * Handles the authentication of users and stores the current user within a session
 *
 * @author Justin Aronwald
 */
public class SessionManagement {
  private static PlayerData currentUser;

  /**
   * Authentication to start a new session and log in a user
   *
   * @param user - the user wishing to login
   */
  public static void login(PlayerData user) {
    currentUser = user;
  }

  /**
   * Authentication to end a session and log out a user
   *
   */
  public static void logout() {
    currentUser = null;
  }

  /**
   * Returns the current user of the session
   *
   * @return - the logged-in user
   * @throws SessionException - if a user is not logged in
   */
  public static PlayerData getCurrentUser() throws SessionException {
    if (currentUser == null) {
      throw new SessionException("No user logged in");
    }

    return currentUser;
  }

  /**
   * Method determining if a user is logged in
   *
   * @return - true if a user is logged in, otherwise false
   */
  public static boolean isLoggedIn() {
    return currentUser != null;
  }

}
