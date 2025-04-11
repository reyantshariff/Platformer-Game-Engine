package oogasalad.model.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import oogasalad.model.engine.base.architecture.GameObject;

public class PrefabLoader {

  // Directory where prefab JSON files are located
  public static final String PREFAB_DIRECTORY = "src/main/java/oogasalad/model/engine/prefab/";

  /**
   * Loads all prefab JSON files in the directory and returns the list of GameObjects
   * that are allowed to be placed (i.e., only Bird and Cactus in this case).
   *
   * @param directory the package within /prefab/ containing the desired prefabs, such as "dinosaur"
   */
  public static List<GameObject> loadAvailablePrefabs(String directory) {
    List<GameObject> validPrefabs = new ArrayList<>();
    File dir = new File(PREFAB_DIRECTORY + directory + "/");
    if (dir.exists() && dir.isDirectory()) {
      File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
      if (files != null) {
        ObjectMapper mapper = new ObjectMapper();
        GameObjectParser parser = new GameObjectParser();
        for (File file : files) {
          try {
            JsonNode node = mapper.readTree(file);
            GameObject obj = parser.parse(node);
            validPrefabs.add(obj);
          } catch (IOException | ParsingException e) {
            System.err.println("Error parsing prefab " + file.getName() + ": " + e.getMessage());
          }
        }
      }
    } else {
      System.err.println("Prefab directory not found: " + PREFAB_DIRECTORY);
    }
    return validPrefabs;
  }
}

