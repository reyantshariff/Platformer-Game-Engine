package oogasalad.view.scene.auth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.stage.Stage;
import oogasalad.controller.GameController;
import oogasalad.database.FirebaseManager;
import oogasalad.model.config.GameConfig;
import oogasalad.model.profile.SessionManagement;
import oogasalad.view.scene.MainViewManager;
import oogasalad.view.scene.menu.MainMenuScene;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

class LogInSceneTest extends ApplicationTest {

  @BeforeEach
  @AfterEach
  public void cleanUp() {
    SessionManagement.logout(); // reset session
    try {
      Files.deleteIfExists(Paths.get("rememberme.properties"));
    } catch (IOException e) {
      System.err.println("Error cleaning up user properties: " + e.getMessage());
    }
  }

  @Override
  public void start(Stage stage) throws IOException {
    FirebaseManager.initializeFirebase();

    MainViewManager viewManager = MainViewManager.setInstance(stage);
    viewManager.addViewScene(MainMenuScene.class, GameConfig.getText("defaultScene"));

    GameController spyController = Mockito.spy(new GameController(viewManager));
    LogInScene loginScene = new LogInScene(viewManager);
    stage.setScene(loginScene.getScene());
    stage.show();
  }

  @Test
  void loginButtons_incorrectLogin_notChangeInScene() {
    clickOn("#socialEmailPrompt").write("justin");
    clickOn("#socialPasswordPrompt").write("test123");
    clickOn("#rememberMeCheckbox");
    clickOn("#socialLoginButton");
  }

  @Test
  void loginButtons_correctLogin_changeInScene() {
    clickOn("#socialEmailPrompt").write("justin1");
    clickOn("#socialPasswordPrompt").write("justin1");
    clickOn("#socialLoginButton");
  }

  @Test
  void signUpButton_worksProperly_ChangesScene() {
    clickOn("#socialSignUpButton");
  }
}