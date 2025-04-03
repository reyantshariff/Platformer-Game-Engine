package oogasalad.editor;

import javafx.scene.Scene;
import oogasalad.engine.base.architecture.Game;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.base.architecture.GameScene;
import oogasalad.engine.component.Transform;

/*
Change LevelEditorScene to EditorScene
Store a map/list of all the scenes and each scene will have a record of their objects
Keep a pointer to the current scene

Serializable field passes coordinates to front end
* */
public class LevelEditorScene implements EditorAPI {
  private GameObject previewObject;
  private String levelName;
  private Game game;

  public LevelEditorScene(String levelName)
  {
    this.levelName = levelName;
  }

  public String getLevelName()
  {
    return levelName;
  }

  @Override
  public GameObject previewObject(String type)
  {
    if (previewObject != null) {
      game.getCurrentScene().unregisterObject(previewObject);
    }

    previewObject = game.getCurrentScene().instantiateObject(GameObjectFactory.create(type));
    game.getCurrentScene().registerObject(previewObject);
    return previewObject;
  }

  @Override
  public GameObject placeObject(String type, double x, double y)
  {

    GameObject newObj = game.getCurrentScene().instantiateObject(GameObjectFactory.create(type));
    //newObj.getComponent(Transform.class).getSerializedFields();
    newObj.addComponent(Transform.class);
    newObj.getComponent(Transform.class).setX(x);
    newObj.getComponent(Transform.class).setY(y);
    game.getCurrentScene().registerObject(newObj);
    return newObj;
  }

  @Override
  public void deleteObject(GameObject object) {
    game.getCurrentScene().unregisterObject(object);
  }

}
