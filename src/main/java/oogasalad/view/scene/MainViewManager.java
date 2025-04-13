package oogasalad.view.scene;


import java.lang.reflect.InvocationTargetException;
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


  /**
   * Function that uses reflection to either create or switch to a ViewScene
   *
   * @param viewSceneName - Name of view scene to be switchted to - must match class name
   */
  public void switchTo(String viewSceneName) {
    try {
      if (!viewScenes.containsKey(viewSceneName)) {
        Class<?> clazz = Class.forName("oogasalad.view.scene." + viewSceneName);
        ViewScene viewScene = (ViewScene)
            clazz.getDeclaredConstructor(MainViewManager.class).newInstance(this);
        viewScenes.put(viewSceneName, viewScene);
      }
      switchTo(viewScenes.get(viewSceneName));
    } catch (ClassNotFoundException |
        NoSuchMethodException |
        InstantiationException |
        IllegalAccessException |
             InvocationTargetException e) {
      throw new SceneSwitchException("Failed to switch to scene: " + viewSceneName, e);
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
