package oogasalad.view.scene.display;

import javafx.stage.Stage;
import oogasalad.database.FirebaseManager;
import oogasalad.model.config.GameConfig;
import oogasalad.view.scene.MainViewManager;
import oogasalad.view.scene.menu.MainMenuScene;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;

class GameDisplaySceneTest extends ApplicationTest {

  @Override
  public void start(Stage stage) throws IOException {
    FirebaseManager.initializeFirebase();
    MainViewManager viewManager = MainViewManager.setInstance(stage);

    viewManager.addViewScene(MainMenuScene.class, GameConfig.getText("defaultScene"));

    GameDisplayScene displayScene = viewManager.addViewScene(GameDisplayScene.class, "GamePlayer");
    displayScene.setReturnSceneName(GameConfig.getText("defaultScene"));
    stage.setScene(displayScene.getScene());
    stage.show();
  }

  @Test
  void clickReturnButton_whenInGameDisplay_shouldSwitchToMainMenuScene() {
    clickOn("#returnButton");
  }
}