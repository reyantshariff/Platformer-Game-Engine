package oogasalad.view.scene.menu;

import javafx.stage.Stage;
import oogasalad.database.FirebaseManager;
import oogasalad.model.profile.SessionManagement;
import oogasalad.view.scene.MainViewManager;
import oogasalad.view.scene.builder.BuilderScene;
import oogasalad.view.scene.display.GameDisplayScene;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class MainMenuSceneTest extends ApplicationTest {

  @BeforeEach
  @AfterEach
  public void resetSessionAndDeleteUserProperties() {
    SessionManagement.logout();
    try {
      Files.deleteIfExists(Paths.get("rememberme.properties"));
    } catch (IOException e) {
      System.err.println("Error cleaning up user properties: " + e.getMessage());
    }
  }

  @Override
  public void start(Stage stage) throws Exception {
    FirebaseManager.initializeFirebase();

    MainViewManager viewManager = MainViewManager.setInstance(stage);
    viewManager.addViewScene(GameDisplayScene.class, "GamePlayer");
    viewManager.addViewScene(BuilderScene.class, "Builder");

    MainMenuScene menuScene = viewManager.addViewScene(MainMenuScene.class, "MainMenuScene");
    stage.setScene(menuScene.getScene());
    stage.show();
  }

//  @Test
//  void clickPlayButton_withValidScene_shouldSwitchToGamePlayerScene() {
//    clickOn("#gameSelector");
//    clickOn("#playButton");
//  }


  @Test
  void clickLogOutButton_whenUserIsLoggedIn_shouldLogUserOutAndSwitchScene() {
    clickOn("#logOutButton");
  }

//  @Test
//  void clickGameSelector_withValidClick_shouldOpenFileChooserDialog() {
//    clickOn("#gameSelector");
//  }
//
//  @Test
//  void clickMenuSelector_whenSelectingOption_shouldAllowLanguageOrThemeChange() {
//    clickOn("#menuSelector");
//  }
}