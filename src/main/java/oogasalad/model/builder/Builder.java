package oogasalad.model.builder;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.UUID;
import oogasalad.model.builder.actions.DeleteObjectAction;
import oogasalad.model.builder.actions.MoveObjectAction;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.component.Transform;
import oogasalad.model.parser.JsonParser;
import oogasalad.model.parser.ParsingException;

/**
 * Builder API that manages drag/drop and delete functions of the Editor UI
 * @author Reyan Shariff
 */

public class Builder {
  private GameObject selectedObject; //Should be passed from front end to back end. Front end should pass string ID.
  private String filepath = " ";
  private Game game; //Front end should pass a list of selected objects to the backend.
  private boolean fileSaved = false;
  private GameScene gameScene;

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
    this.gameScene = new GameScene("new GameScene");
    game.addScene(gameScene);
  }

  private Deque<EditorAction> undoStack = new ArrayDeque<>();
  private Deque<EditorAction> redoStack = new ArrayDeque<>();

  /**
   * Constructs a new Builder instance with the specified Game.
   *
   * @param scene The GameScene instance to be used by the builder.
   */
  public Builder(GameScene scene) {
    game = new Game();
    gameScene = scene;
    game.addScene(gameScene);
    game.changeScene(scene.getName());
  }

  /**
   Undoes Last User Action
   * */
  public void undoLastAction() {
    if (!undoStack.isEmpty()) {
      EditorAction action = undoStack.pop();
      action.undo();
      redoStack.push(action);
    }
  }

  /**
   Redoes Last User Action
   * */
  public void redoLastAction()
  {
    if (!redoStack.isEmpty()) {
      EditorAction action = redoStack.pop();
      action.redo();
      undoStack.push(action);
    }
  }

  /**
   * Save the file
   */
  public void Save()
  {
    fileSaved = true;
  }

  /**
   * Checks if the file was saved
   */
  public boolean isSaved()
    {
      return fileSaved;
    }

  /**
   *  Records when a game object has been selected to be dragged and dropped on the UI
   */
  public void selectExistingObject(GameObject object)
  {
    selectedObject= object;
  }


  /**
   *  Records when two game objects overlap
   */
  public boolean overlaps(GameObject currentObject)
  {
    for (GameObject object : game.getCurrentScene().getAllObjects())
    {
      if (object.hasComponent(Transform.class) && object.getComponent(Transform.class).getX() == currentObject.getComponent(Transform.class).getX() && object.getComponent(Transform.class).getY() == currentObject.getComponent(Transform.class).getY() && currentObject.getId() != object.getId())
      {
        return true;
      }
    }
    return false;
  }


//   private GameObject findObject(UUID id)
//   {
//     for (GameObject object : game.getCurrentScene().getAllObjects())
//     {
//       if (object.getId() == id)
//       {
//         return object;
//       }
// //      if (object.getComponent(Transform.class).getX() == x && object.getComponent(Transform.class).getY() == y)
// //      {
// //        return object;
// //      }
//     }
//     return null;
//   }

  /**
   *  Stops the preview if the user lifts mouse and cursor is not on the editor screen.
   *  Game object should be instantiated after mouse is released
   */
  public void placeObject(double x, double y) {
    if (selectedObject != null && selectedObject.hasComponent(Transform.class)) {
      undoStack.push(new MoveObjectAction(selectedObject, selectedObject.getComponent(Transform.class).getX(), selectedObject.getComponent(Transform.class).getY(), x, y));
      selectedObject.getComponent(Transform.class).setX(x);
      selectedObject.getComponent(Transform.class).setY(y);
    }
    selectedObject = null; //should I add exception?
  }

  /**
   *  Checks if the user is currently dragging around a game object
   */
  public boolean objectIsSelected() {
    return selectedObject != null;
  }

  /**
   *  Moves the game object to a new location
   */
  public void moveObject(double x, double y)
  {
    if (selectedObject != null && selectedObject.hasComponent(Transform.class))
    {
      selectedObject.getComponent(Transform.class).setX(x);
      selectedObject.getComponent(Transform.class).setY(y);
    }
  }

  /**
   *  Deletes selected game object from the screen
   */
  public void deleteSelectedObject() {
    if (selectedObject != null)
    {
      game.getCurrentScene().unregisterObject(selectedObject);
      undoStack.push(new DeleteObjectAction(game, selectedObject));
    }
  }

  /**
   * Save the currently loaded Game object as a JSON file using the JsonParser
   * @param filepath location of JSON file
   */
  public JsonNode saveGameAs(String filepath) {
    JsonParser parser = new JsonParser(filepath);
    try {
      return parser.write(game);
    } catch (IOException e) {
      throw new RuntimeException("Error saving game to JSON: " + e.getMessage());
      // TODO: replace with custom exception class
    }
  }

  /**
   * Load a new Game into the Builder via a JSON file node
   */
  public void loadGame(JsonNode node) {
    JsonParser parser = new JsonParser(filepath);
    try {
      game = parser.parse(node);
    } catch (ParsingException e) {
      throw new RuntimeException("Error loading game from JSON: " + e.getMessage());
    }
  }
}
