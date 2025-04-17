package oogasalad;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import oogasalad.database.FirebaseManager;
import oogasalad.model.config.GameConfig;
import oogasalad.model.profile.SessionManagement;
import oogasalad.view.scene.MainViewManager;
import oogasalad.view.scene.auth.LogInScene;
import oogasalad.view.scene.menu.MainMenuScene;

/**
 * This is the main class of the OOGASalad Platformer Game Sandbox. Run the start method to open the
 * program.
 */
public class Main extends Application {

  private static final boolean DEBUG = true;

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
  public void start(Stage stage) throws IOException {
    FirebaseManager.initializeFirebase();

    MainViewManager viewManager = MainViewManager.setInstance(stage);
    viewManager.addViewScene(MainMenuScene.class, GameConfig.getText("defaultScene"));

    if (DEBUG || SessionManagement.tryAutoLogin()) {
      viewManager.switchToMainMenu();
    } else {
      LogInScene splashScene = new LogInScene(viewManager);
      stage.setScene(splashScene.getScene());
      stage.show();
    }
  }

}