package oogasalad.engine;

import java.util.HashMap;
import java.util.Map;

/**
 * The Game class is the main entry point for the game engine. It manages the game loop, scene
 * management, and game logic.
 */

public class Game {
  private final Map<String, GameScene> loadedScenes = new HashMap<>();
  private final Map<String, String> scenePaths = new HashMap<>();

  private GameScene currentScene;

  public Game() {

  }

  /**
   * The main game loop. This method should be called every frame. It updates the current scene and
   * handles any necessary game logic.
   * 
   * @param deltaTime The time since the last frame, in seconds.
   */
  public void step(double deltaTime) {

  }

  /**
   * Change the current scene to the specified scene
   * 
   * @param sceneName The name of the scene to change to
   */
  public void changeScene(String sceneName) {

  }

  /**
   * Initialize and adds a scene of the specified class to the game
   * 
   * @param sceneClass The class of the scene to add
   */
  public <T extends GameScene> void addScene(Class<T> sceneClass) {

  }

  /**
   * Adds a pre-made scene to the game. The scene is loaded from the specified path.
   * 
   * @param scenePath The path to the scene file
   */
  public void addScene(String scenePath) {

  }

  /**
   * Removes a scene from the game.
   * 
   * @param sceneName The name of the scene to remove
   */
  public void removeScene(String sceneName) {

  }

  /**
   * Resets the specified scene to its initial state
   * 
   * @param sceneName The name of the scene to reset
   */
  public void resetScene(String sceneName) {

  }
}
