package oogasalad.engine.base.architecture;

import oogasalad.engine.component.Transform;

public class LevelEditorScene extends GameScene implements EditorAPI{
  private GameObject previewObject;
  public LevelEditorScene(String levelName)
  {
    super(levelName);
  }

  @Override
  public GameObject previewObject(String type)
  {
    if (previewObject != null) {
      this.unregisterObject(previewObject);
    }

    previewObject = GameObjectFactory.create(type);
    this.registerObject(previewObject);
    return previewObject;
  }

  @Override
  public GameObject placeObject(String type, double x, double y)
  {
    GameObject newObj = GameObjectFactory.create(type);
    newObj.addComponent(Transform.class);
    newObj.getComponent(Transform.class).setX(x);
    newObj.getComponent(Transform.class).setY(y);
    this.registerObject(newObj);
    return newObj;
  }

  @Override
  public void deleteObject(GameObject object) {
    this.unregisterObject(object);
  }

}
