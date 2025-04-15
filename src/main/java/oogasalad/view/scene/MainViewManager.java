package oogasalad.view.scene;


import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import javafx.stage.Stage;
import oogasalad.model.config.GameConfig;


/**
 * Handles switching between ViewScenes in the application
 *
 * @author Justin Aronwald
 */
public class MainViewManager {
  private final Map<String, java.util.function.Function<MainViewManager, ViewScene>> sceneFactories = new HashMap<>();

  private static final String GAME_PLAYER = "GamePlayerScene";

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

  /**
   * @param viewSceneName - String name of the viewscene to be added
   * @param factory - Factory function to be put in the map
   */
  public void registerSceneFactory(String viewSceneName, java.util.function.Function<MainViewManager, ViewScene> factory) {
    sceneFactories.put(viewSceneName, factory);
  }


  /**
   * Function that uses reflection to either create or switch to a ViewScene
   *
   * @param viewSceneName - Name of view scene to be switchted to - must match class name
   */
  public void switchTo(String viewSceneName) {
    try {
      if (!viewScenes.containsKey(viewSceneName)) {
        ViewScene viewScene;

        if (sceneFactories.containsKey(viewSceneName)) {
          viewScene = sceneFactories.get(viewSceneName).apply(this);
        } else {
          Class<?> clazz = Class.forName("oogasalad.view.scene." + viewSceneName);
          viewScene = (ViewScene)
              clazz.getDeclaredConstructor(MainViewManager.class).newInstance(this);
        }

        viewScenes.put(viewSceneName, viewScene);
      }
      switchTo(viewScenes.get(viewSceneName));
      removeCachedGameScene(viewSceneName);
    } catch (ClassNotFoundException |
        NoSuchMethodException |
        InstantiationException |
        IllegalAccessException |
             InvocationTargetException e) {
      throw new SceneSwitchException(GameConfig.getText("noSuchScene", viewSceneName), e);
    }
  }

  private void removeCachedGameScene(String viewSceneName) {
    if(viewSceneName.equals(GAME_PLAYER)) {
      viewScenes.remove(GAME_PLAYER);
    }
  }

  /**
   * Switches to the given ViewScene
   *
   * @param viewScene the new scene to display
   */
  private void switchTo(ViewScene viewScene) {
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
    switchTo(GameConfig.getText("defaultScene"));
  }

  /**
   * Custom exception for any issues with scene switching. Especially for the
   * reflection in String
   */
  public static class SceneSwitchException extends RuntimeException {
    /**
     * Constructor for SceneSwitchException
     * @param message exception messages
     * @param cause the cause of the exception
     */
    public SceneSwitchException(String message, Throwable cause) {
      super(message, cause);
    }
  }

}
