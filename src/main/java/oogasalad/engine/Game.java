package oogasalad.engine;

import java.util.HashMap;
import java.util.Map;

public class Game {
  private final Map<String, GameScene> loadedScenes = new HashMap<>();
  private final Map<String, String> scenePaths = new HashMap<>();

  private GameScene currentScene;

  public Game() {

  }

  public void step(double deltaTime) {

  }

  public void changeScene(String sceneName) {

  }

  public <T extends GameScene> void addScene(Class<T> sceneClass) {

  }

  public void addScene(String scenePath) {

  }

  public void removeScene(String sceneName) {

  }

  public void resetScene(String sceneName) {

  }
}
