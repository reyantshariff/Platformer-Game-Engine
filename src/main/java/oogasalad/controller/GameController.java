package oogasalad.controller;

import java.io.IOException;
import oogasalad.model.config.PasswordHashingException;
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
   */
  public void handleLogin(String username, String password, boolean rememberMe) throws IOException {
    try {
      PlayerService.login(username, password, rememberMe);
      mainViewManager.switchTo("MainMenuScene");
    } catch (PasswordHashingException e) {
      LOGGER.warn("Error");
      //Show error on the frontend
    } catch (IOException e) {
      throw new IOException("Error with handling autologin:", e);
    }
  }

  /**
   * Method called by the view to handle logging out
   *
   */
  public void handleLogout(){
    PlayerService.logout();
    mainViewManager.switchTo("LogInScene");
  }
}
