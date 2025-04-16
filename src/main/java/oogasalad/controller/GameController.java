package oogasalad.controller;

import java.io.IOException;
import oogasalad.database.DatabaseException;
import oogasalad.model.config.PasswordHashingException;
import oogasalad.model.profile.SessionManagement;
import oogasalad.model.profile.SignUpRequest;
import oogasalad.model.service.PlayerService;
import oogasalad.view.scene.MainViewManager;
import static oogasalad.model.config.GameConfig.LOGGER;

/**
 * GameController is a class to separate model and view logic and calls the APIs
 */
public class GameController {
  private final MainViewManager mainViewManager;

  /**
   * Creates an instance of the GameController object
   *
   * @param mainViewManager - the view manager that allows for switching of scenes
   */
  public GameController(MainViewManager mainViewManager) {
    this.mainViewManager = mainViewManager;
  }

  /**
   * Method that is called by the view to handle login
   *
   * @param username - the inputted username
   * @param password - the inputted password
   * @return - true if login is successful
   */
  public boolean handleLogin(String username, String password, boolean rememberMe) throws IOException {
    try {
      PlayerService.login(username, password, rememberMe);
      if (SessionManagement.isLoggedIn()) {
        mainViewManager.switchTo("MainMenuScene");
        return true;
      }
    } catch (PasswordHashingException e) {
      LOGGER.warn("Error");
      //Show error on the frontend
    } catch (IOException e) {
      throw new IOException("Error with handling autologin:", e);
    }
    return false;
  }

  /**
   * Method called by the view to handle logging out
   *
   */
  public void handleLogout(){
    PlayerService.logout();
    mainViewManager.switchTo("LogInScene");
  }

  /**
   * Method called by the view to sign a user up
   *
   * @param signUpRequest - object containing the strings used to log in
   * @throws PasswordHashingException - true if there's an issue hashing the password
   * @throws DatabaseException - true if there's a database error or username exists
   */
  public void handleSignUp(SignUpRequest signUpRequest)
      throws PasswordHashingException, DatabaseException {
    String fullName = signUpRequest.firstName() + " " + signUpRequest.lastName();
    try {
      if (PlayerService.createNewPlayer(signUpRequest.username(), signUpRequest.password(), fullName)) {
        mainViewManager.switchTo("MainMenuScene");
      }
    } catch (PasswordHashingException e) {
      throw new PasswordHashingException("Error hashing password", e);
    } catch (DatabaseException e) {
      throw new DatabaseException("Username already exists", e);
    }
  }
}
