package oogasalad.view.scene;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import javafx.stage.Stage;
import oogasalad.model.config.GameConfig;
import oogasalad.view.scene.auth.LogInScene;
import oogasalad.view.scene.menu.MainMenuScene;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

class MainViewManagerTest extends DukeApplicationTest {

  private MainViewManager manager;

  @Override
  public void start(Stage stage) {
    manager = MainViewManager.setInstance(stage);
    manager.addViewScene(MainMenuScene.class, GameConfig.getText("defaultScene"));  }

  @Test
  void addAndGetViewScene_addsViewScene_returnsViewScene() {
    LogInScene logInScene = new LogInScene(manager);
    manager.addViewScene(logInScene.getClass(), GameConfig.getText("defaultScene"));

    ViewScene retrieved = manager.getViewScene("MainMenuScene");
    assertNotNull(retrieved);
    assertInstanceOf(MainMenuScene.class, retrieved);
  }
}