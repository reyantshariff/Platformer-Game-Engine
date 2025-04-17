package oogasalad.view.scene;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.stage.Stage;
import oogasalad.view.config.StyleConfig;
import org.reflections.Reflections;
import oogasalad.model.config.GameConfig;


/**
 * Handles switching between ViewScenes in the application
 *
 * @author Justin Aronwald
 */
public class MainViewManager {

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
  private MainViewManager(Stage stage) {
    this.stage = stage;
    instance = this;
  }

  /**
   * Set the instance for the mainView manager.
   */
  public static MainViewManager setInstance(Stage stage) {
    if (instance == null) {
      instance = new MainViewManager(stage);
    }

    return instance;
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
   * Creates and stores a ViewScene instance with the given class and constructor args.
   *
   * @param viewSceneClass The class of the ViewScene
   * @return The created instance
   */
  public <T extends ViewScene> T addViewScene(Class<T> viewSceneClass, String name) {
    try {
      Constructor<T> constructor = viewSceneClass.getDeclaredConstructor();
      constructor.setAccessible(true);
      T instance = constructor.newInstance();
      viewScenes.put(name, instance);
      return instance;
    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      GameConfig.LOGGER.error("Failed to instantiate view scene: {}", viewSceneClass.getName(), e);
      // TODO: handle exception here
    }
    return null;
  }

  /**
   * Gets the viewScene instance with the given name.
   */
  public ViewScene getViewScene(String name) {
    return viewScenes.get(name);
  }

  /**
   * Function that uses reflection to either create or switch to a ViewScene
   *
   * @param viewSceneName - Name of view scene to be switched to - must match class name
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

  private void switchTo(ViewScene viewScene) {
    StyleConfig.setStylesheet(viewScene.getScene(), StyleConfig.getCurrentTheme());

    if (currentScene != null) {
      currentScene.deactivate();
    }

    stage.setScene(viewScene.getScene());
    currentScene = viewScene;
    stage.show();
  }

  /**
   * @return Stage
   */
  public Stage getStage()
  {
    return stage;
  }


  /**
   * Shortcut to go to the main menu
   */
  public void switchToMainMenu() {
    switchTo(GameConfig.getText("defaultScene"));
  }

  /**
   * @return - Name of current scene
   */
  public String getCurrentSceneName() {
    return viewScenes.entrySet().stream()
        .filter(entry -> entry.getValue().equals(currentScene))
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(null);
  }
}
