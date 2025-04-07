package oogasalad.engine.base.architecture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The Game class is the main entry point for the game engine. It manages the game loop, scene
 * management, and game logic.
 *
 * @author Hsuan-Kai Liao, Christian Bepler
 */
public class Game {

  private final Map<UUID, GameScene> loadedScenes = new HashMap<>();
  private final Map<UUID, String> scenePaths = new HashMap<>();

  private GameScene currentScene;
  private GameInfo myGameInfo;

  /**
   * Constructor for game object, initializing currentScene to null
   */
  public Game() {
    currentScene = null;
  }

  /**
   * The main game loop. This method should be called every frame. It updates the current scene and
   * handles any necessary game logic.
   *
   * @param deltaTime The time since the last frame, in seconds.
   */
  public void step(double deltaTime) {
    currentScene.step(deltaTime);
  }

  /**
   * Getter for current gameScene
   *
   * @return - The current scene
   */
  public GameScene getCurrentScene() {
    return currentScene;
  }

  /**
   * Change the current scene to the specified scene
   *
   * @param sceneName The name of the scene to change to
   */
  public void changeScene(String sceneName) {
    for (GameScene scene : loadedScenes.values()) {
      if (scene.getName().equals(sceneName)) {
        if (currentScene != null && !currentScene.equals(scene)) {
          currentScene.onDeactivated();
          currentScene = scene;
          currentScene.onActivated();
        }

        return;
      }
    }
  }

  /**
   * Returns the loaded scenes.
   */
  public Map<UUID, GameScene> getLoadedScenes() {
    return loadedScenes;
  }

  /**
   * Initialize and adds a scene of the specified class to the game
   *
   * @param sceneClass The class of the scene to add
   */
  public <T extends GameScene> void addScene(Class<T> sceneClass, String name) {
    // Check for same name
    for (GameScene scene : loadedScenes.values()) {
      if (scene.getName().equals(name)) {
        throw new IllegalArgumentException("The scene with name" + name + "already exists.");
      }
    }

    // Instantiate scene instance
    try {
      T scene = sceneClass.getDeclaredConstructor(String.class).newInstance(name);
      loadedScenes.put(scene.getId(), scene);

      if (currentScene == null) {
        currentScene = scene;
        currentScene.onActivated();
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds a pre-made scene to the game. The scene is loaded from the specified path.
   *
   * @param scenePath The path to the scene file
   */
  public void addScene(String scenePath) {
    // TODO: Add parser to the addScene
  }

  /**
   * Removes a scene from the game.
   *
   * @param sceneName The name of the scene to remove
   */
  public void removeScene(String sceneName) {
    for (UUID id : loadedScenes.keySet()) {
      if (loadedScenes.get(id).getName().equals(sceneName)) {
        loadedScenes.remove(id);
        scenePaths.remove(id);
        return;
      }
    }
  }

  /**
   * Resets the specified scene to its initial state
   *
   * @param sceneName The name of the scene to reset
   */
  public void resetScene(String sceneName) {
    for (UUID id : loadedScenes.keySet()) {
      if (loadedScenes.get(id).getName().equals(sceneName)) {
        String path = scenePaths.get(id);
        // TODO: Add parser to reload the scene
      }
    }
  }

  /**
   * Setter for the GameInfo
   *
   * @param gameInfo - a class containing the name, author, description, and resolution of a game
   */
  public void setGameInfo(GameInfo gameInfo) {
    myGameInfo = gameInfo;
  }

  /**
   * getter for GameInfo of a Game object
   *
   * @return - a GameInfo object comprised of name, author, description, and resolution
   */
  public GameInfo getGameInfo() {
    return myGameInfo;
  }

  /**
   * getter for the loaded gameScenes
   *
   * @return - a list of all the scenes currently loading in the game object
   */
  public List<GameScene> getAllScenes() {
    return new ArrayList<>(loadedScenes.values());
  }
}
