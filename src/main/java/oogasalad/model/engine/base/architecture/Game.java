package oogasalad.model.engine.base.architecture;

import static oogasalad.model.config.GameConfig.LOGGER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The Game class is the main entry point for the game engine. It manages the game loop, scene
 * management, and game logic.
 *
 * @author Hsuan-Kai Liao, Christian Bepler
 */
public class Game {
  private final Map<UUID, GameScene> allScenes = new HashMap<>();
  private final Set<Integer> inputKeys = new HashSet<>();

  private GameScene currentScene;
  private GameInfo myGameInfo;

  private List<String> levelOrder = new ArrayList<>();
  private int currentLevelIndex = 0;

  /**
   * The main game loop. This method should be called every frame. It updates the current scene and
   * handles any necessary game logic.
   *
   * @param deltaTime The time since the last frame, in seconds.
   */
  public void step(double deltaTime) {
    if (currentScene != null) {
      currentScene.step(deltaTime);
    } else {
      throw new AssertionError("No Game Scene has not been loaded yet.");
    }
  }

  /**
   * Change the current scene to the specified scene
   *
   * @param sceneName The name of the scene to change to
   */
  public void changeScene(String sceneName) {
    for (GameScene scene : allScenes.values()) {
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
   * Returns the current scene.
   */
  public GameScene getCurrentScene()
  {
    return currentScene;
  }

  /**
   * Returns the all the loaded scenes.
   */
  public Map<UUID, GameScene> getAllScenes() {
    return Collections.unmodifiableMap(allScenes);
  }

  /**
   * Adds a scene to the game.
   */
  public void addScene(GameScene scene) {
    if (allScenes.containsKey(scene.getId())) {
      LOGGER.error("Scene with ID {} already exists", scene.getId());
      throw new IllegalArgumentException("Scene with ID " + scene.getId() + " already exists");
    } else {
      allScenes.put(scene.getId(), scene);
      scene.setGame(this);
      if (currentScene == null) {
        currentScene = scene;
      }
    }
  }

  /**
   * Removes a scene from the game.
   *
   * @param sceneName The name of the scene to remove
   */
  public void removeScene(String sceneName) {
    for (UUID id : allScenes.keySet()) {
      if (allScenes.get(id).getName().equals(sceneName)) {
        GameScene scene = allScenes.get(id);
        scene.setGame(null);
        allScenes.remove(id);
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
    //TODO: Store the file paths and reload the scene with the parser or store original versions of all scenes
  }
  

  /**
   * Called externally when a key is pressed.
   * Note: This method need to be subscribed to the outer input event bus.
   * @param keyCode the key code of the pressed key
   */
  public void keyPressed(int keyCode) {
    inputKeys.add(keyCode);
  }

  /**
   * Called externally when a key is released.
   * Note: This method need to be subscribed to the outer input event bus.
   * @param keyCode the key code of the released key
   */
  public void keyReleased(int keyCode) {
    inputKeys.remove(keyCode);
  }

  /**
   * Returns a set of all the input keys currently pressed.
   * @return a unmodifiable set of all the input keys currently pressed
   */
  public Set<Integer> getCurrentInputKeys() {
    return Collections.unmodifiableSet(inputKeys);
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
   * Setter for the order of the levels
   *
   * @param levelOrder - a list of strings containing the ordering of levels by name
   */
  public void setLevelOrder(List<String> levelOrder) {
    this.levelOrder = levelOrder;
  }

  /**
   * Method that handles advancing levels -- moves the currentIndex of the level and changes scenes
   *
   */
  public void goToNextLevel() {
    currentLevelIndex++;
    if (currentLevelIndex < levelOrder.size()) {
      changeScene(levelOrder.get(currentLevelIndex));
    }
  }

  /**
   * Getter for the order of the levels
   *
   * @return - a list of strings containing the ordering of levels by name
   */
  public List<String> getLevelOrder() {
    return levelOrder;
  }

  /**
   * Method to go to a new scene based on scene name
   *
   * @param sceneName - the name of the scene that will be switched too
   */
  public void goToScene(String sceneName) {
    int index = levelOrder.indexOf(sceneName);
    if (index != -1) {
      currentLevelIndex = index;
      changeScene(sceneName);
    }
  }

}
