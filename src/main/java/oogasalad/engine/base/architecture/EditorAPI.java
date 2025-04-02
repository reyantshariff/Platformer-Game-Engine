package oogasalad.engine.base.architecture;

public interface EditorAPI {
  GameObject previewObject(String type);
  GameObject placeObject(String type, double x, double y);
  void deleteObject(GameObject object);
}

