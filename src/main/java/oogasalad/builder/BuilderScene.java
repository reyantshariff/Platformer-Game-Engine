package oogasalad.builder;

import oogasalad.engine.base.architecture.Game;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.component.Transform;

/**
 * Builder API that manages drag/drop and delete functions of the Editor UI
 *
 * @author Reyan Shariff
 */

public class BuilderScene {

  private GameObject previewObject;
  private String name;
  private Game game = new Game(); //TODO: Link this to main somehow

  /**
   * Constructor for BuilderScene
   *
   * @param name - Name of scene
   */
  public BuilderScene(String name) {
    this.name = name;
  }

  /**
   * Returns the object name
   */
  public String getName() {
    return name;
  }

  /**
   * Records when a game object has been selected to be dragged and dropped on the UI
   */
  public void previewObject(String type) {
    if (previewObject != null) {
      game.getCurrentScene().unregisterObject(previewObject);
    }

    previewObject = game.getCurrentScene().instantiateObject(GameObjectFactory.create(type));
    game.getCurrentScene().registerObject(previewObject);
  }

  /**
   * Moves the game object around the screen.
   */
  public void movePreview(double x, double y) {
    if (previewObject != null) {
      previewObject.addComponent(Transform.class);
      Transform t = previewObject.getComponent(Transform.class);
      t.setX(x);
      t.setY(y);
    }
  }

  /**
   * Stops the preview if the user lifts mouse and cursor is not on the editor screen.
   */

  public void cancelPreview() {
    if (previewObject != null) {
      game.getCurrentScene().unregisterObject(previewObject);
      previewObject = null;
    }
  }

  /**
   * Checks if the user is currently dragging around a game object
   */
  public boolean hasActivePreview() {
    return previewObject != null;
  }

  /**
   * Returns X position of object in preview mode
   */
  public double getPreviewX() {
    return previewObject.getComponent(Transform.class).getX();
  }

  /**
   * Returns Y position of object in preview mode
   */
  public double getPreviewY() {
    return previewObject.getComponent(Transform.class).getY();
  }

  /**
   * Places game object onto screen
   */
  public void placeObject() {
    previewObject = null;
  }

  /**
   * Deletes game object from the screen
   */
  public void deleteObject(GameObject object) { //TODO: Change the parameter to String
    game.getCurrentScene().unregisterObject(object);
  }

}
