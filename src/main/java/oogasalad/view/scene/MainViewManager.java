package oogasalad.view.scene;


import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javafx.stage.Stage;
import org.reflections.Reflections;
import oogasalad.model.config.GameConfig;


/**
 * Handles switching between ViewScenes in the application
 *
 * @author Justin Aronwald
 */
public class MainViewManager {
  private final Map<String, java.util.function.Function<MainViewManager, ViewScene>> sceneFactories = new HashMap<>();

  private static final String SCENE_PACKAGE = "oogasalad.view.scene";
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
  public void registerSceneFactory(String viewSceneName, Function<MainViewManager, ViewScene> factory) {
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
        Reflections reflections = new Reflections(SCENE_PACKAGE);
        List<Class<?>> classes = List.copyOf(reflections.getSubTypesOf(ViewScene.class));
        Class<?> clazz = classes.stream().filter(c -> c.getSimpleName().equals(viewSceneName)).findFirst().orElse(null);

        if (clazz != null) {
          ViewScene viewScene = (ViewScene) clazz.getDeclaredConstructor(MainViewManager.class).newInstance(this);
          viewScenes.put(viewSceneName, viewScene);
        }
      }
      switchTo(viewScenes.get(viewSceneName));
      removeCachedGameScene(viewSceneName);
    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new SceneSwitchException(GameConfig.getText("noSuchScene", viewSceneName), e);
    }
  }

  private void removeCachedGameScene(String viewSceneName) {
    if (viewSceneName.equals(GAME_PLAYER)) {
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
}
