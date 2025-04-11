package oogasalad.view.scene;


import java.util.HashMap;
import java.util.Map;
import javafx.stage.Stage;


/**
 * Handles switching between ViewScenes in the application
 *
 * @author Justin Aronwald
 */
public class MainViewManager {

  private final Stage stage;
  private static MainViewManager instance;
  private ViewScene currentScene;
  private final Map<String, ViewScene> viewScenes = new HashMap<>();

  /**
   * Constructs the MainViewManager with the given primary stage
   *
   * @param stage the JavaFX primary stage
   */
  public MainViewManager(Stage stage) {
    this.stage = stage;
    instance = this;
  }


  /**
   * Get instance - necessary for view scene switching
   *
   * @return - the main view manager object
   */
  public static MainViewManager getInstance() {
    return instance;
  }


  public void switchTo(String viewSceneName) {
    try {
      if (!viewScenes.containsKey(viewSceneName)) {
        Class<?> clazz = Class.forName("oogasalad.view.scene." + viewSceneName);
        ViewScene viewScene = (ViewScene) clazz.getDeclaredConstructor(MainViewManager.class).newInstance(this);
        viewScenes.put(viewSceneName, viewScene);
      }
      switchTo(viewScenes.get(viewSceneName));
    } catch (Exception e) {
      throw new RuntimeException("Failed to switch to scene: " + viewSceneName, e);
    }
  }

  /**
   * Switches to the given ViewScene
   * TODO: Deprecate this method to only use reflection above
   *
   * @param viewScene the new scene to display
   */
  public void switchTo(ViewScene viewScene) {
    // Stop current scene, if applicable
    if (currentScene != null) {
      currentScene.deactivate();
    }

    // Set new scene
    stage.setScene(viewScene.getScene());
    currentScene = viewScene;
    stage.show();
  }


  /**
   * Shortcut to go to the main menu
   */
  public void switchToMainMenu() {
    switchTo("MainMenuScene");
  }
}
