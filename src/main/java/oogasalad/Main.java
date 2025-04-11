package oogasalad;

import javafx.application.Application;
import javafx.stage.Stage;
import oogasalad.view.scene.MainViewManager;

/**
 * This is the main class of the OOGASalad Platformer Game Sandbox. Run the start method to open the
 * program.
 */
public class Main extends Application {

  /**
   * Start of the program
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Create a new game and open the gui.
   *
   * @param stage the primary stage for this application, onto which the application scene can be
   *              set. Applications may create other stages, if needed, but they will not be primary
   *              stages.
   */
  @Override
  public void start(Stage stage) {
    MainViewManager viewManager = new MainViewManager(stage);
    viewManager.switchToMainMenu();
  }

}