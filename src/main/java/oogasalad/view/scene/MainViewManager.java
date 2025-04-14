package oogasalad.view.scene;


import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.stage.Stage;
import org.reflections.Reflections;


/**
 * Handles switching between ViewScenes in the application
 *
 * @author Justin Aronwald
 */
public class MainViewManager {
  private static final String SCENE_PACKAGE = "oogasalad.view.scene";

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
        Reflections reflections = new Reflections(SCENE_PACKAGE);
        List<Class<?>> classes = List.copyOf(reflections.getSubTypesOf(ViewScene.class));
        Class<?> clazz = classes.stream().filter(c -> c.getSimpleName().equals(viewSceneName)).findFirst().orElse(null);

        if (clazz != null) {
          ViewScene viewScene = (ViewScene) clazz.getDeclaredConstructor(MainViewManager.class).newInstance(this);
          viewScenes.put(viewSceneName, viewScene);
        }
      }
      switchTo(viewScenes.get(viewSceneName));
    } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
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
}
