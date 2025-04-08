package oogasalad.builder;

import java.util.Stack;
import oogasalad.builder.actions.DeleteObjectAction;
import oogasalad.builder.actions.CreateObjectAction;
import oogasalad.builder.actions.MoveObjectAction;
import oogasalad.engine.base.architecture.Game;
import oogasalad.engine.base.architecture.GameObject;
import oogasalad.engine.component.Transform;

/**
 * Builder API that manages drag/drop and delete functions of the Editor UI
 * @author Reyan Shariff
 */

public class Builder {
  private GameObject selectedObject; //Should be passed from front end to back end. Front end should pass string ID.
  private String filepath = " ";
  private Game game; //Front end should pass a list of selected objects to the backend.
//Add validation for placing ob jects

  //Add Backend boolean to keep track of whether user has saved Game.
  /**
   * Constructor for Loading a Game
   *
   * @param filepath - JSON filepath of Game
   */

  public Builder(String filepath) {

    this.filepath = filepath;
    //game would then load filepath
  }

  /**
   * Constructor for Creating a Game from Scratch
   */

  public Builder()
  {
    game = new Game();
  }

  private Stack<EditorAction> actionStack = new Stack<>();

  /**
   Undoes Last User Action
   * */
  public void undoLastAction() {
    if (!actionStack.isEmpty()) {
      actionStack.pop().undo();
    }
  }

  /**
   Redoes Last User Action
   * */
  public void redoLastAction()
  {
    if (!actionStack.isEmpty())
    {
      actionStack.pop().redo();
    }
  }


  /**
   *  Records when a game object has been selected to be dragged and dropped on the UI
   */

  public void selectObject(String type, int x, int y) //type will change to file path
  {
    if (selectedObject != null) {
      selectedObject = null; //Front end should store the image  path and its Game ID. Front end should keep track of selected object.
    }

    if (findObject(x, y) == null)
    {
      selectedObject = game.getCurrentScene().instantiateObject(GameObjectFactory.create(type)); //Change to Unique IDs
      actionStack.push(new CreateObjectAction(game, selectedObject));
      game.getCurrentScene().registerObject(selectedObject); //Make an add object method
    }
    else
    {
      selectedObject = findObject(x, y); // Front end should pass in game object ID
    }
  }

  /**
   *  Records when two game objects overlap
   */
  public boolean overlaps(GameObject currentObject)
  {
    for (GameObject object : game.getCurrentScene().getAllObjects())
    {
      if (object.getComponent(Transform.class).getX() == currentObject.getComponent(Transform.class).getX() && object.getComponent(Transform.class).getY() == currentObject.getComponent(Transform.class).getY() && currentObject.getId() != object.getId())
      {
        return true;
      }
    }
    return false;
  }


  private GameObject findObject(int x, int y)
  {
    for (GameObject object : game.getCurrentScene().getAllObjects())
    {
      object.addComponent(Transform.class);
      if (object.getComponent(Transform.class).getX() == x && object.getComponent(Transform.class).getY() == y)
      {
        return object;
      }
    }
    return null;
  }

  /**
   *  Stops the preview if the user lifts mouse and cursor is not on the editor screen.
   *  Game object should be instantiated after mouse is released
   */
  public void placeObject(double x, double y) {
    if (selectedObject != null) {
      selectedObject.addComponent(Transform.class);
      actionStack.push(new MoveObjectAction(selectedObject, selectedObject.getComponent(Transform.class).getX(), selectedObject.getComponent(Transform.class).getY(), x, y));
      selectedObject.getComponent(Transform.class).setX(x);
      selectedObject.getComponent(Transform.class).setY(y);
    }
    selectedObject = null;
  }

  /**
   *  Checks if the user is currently dragging around a game object
   */
  public boolean objectIsSelected() {
    return selectedObject != null;
  }

  /**
   *  Deletes selected game object from the screen
   */
  public void deleteSelectedObject() {
    if (selectedObject != null)
    {
      game.getCurrentScene().unregisterObject(selectedObject);
      actionStack.push(new DeleteObjectAction(game, selectedObject));
    }
  }

}
