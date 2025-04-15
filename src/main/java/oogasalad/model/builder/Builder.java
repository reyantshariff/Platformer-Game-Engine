package oogasalad.model.builder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Deque;
import java.util.ArrayDeque;
import oogasalad.model.builder.actions.DeleteObjectAction;
import oogasalad.model.builder.actions.CreateObjectAction;
import oogasalad.model.builder.actions.MoveObjectAction;
import oogasalad.model.builder.actions.ResizeObjectAction;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.component.Transform;
import oogasalad.model.parser.JsonParser;
import oogasalad.model.parser.Parser;
import oogasalad.model.parser.ParsingException;

/**
 * Builder API that manages drag/drop and delete functions of the Editor UI
 * @author Reyan Shariff
 */

public class Builder {
  private GameObject selectedObject; // Should be passed from front end to back end. Front end should pass string ID.
  private final String filepath;
  private Game game; // Front end should pass a list of selected objects to the backend.
  private boolean fileSaved = false;
  private GameScene currentScene;
  private double selectedObjectPrevX;
  private double selectedObjectPrevY;

  private final Deque<EditorAction> undoStack = new ArrayDeque<>();
  private final Deque<EditorAction> redoStack = new ArrayDeque<>();

  /**
   * Constructor for Loading a Game
   *
   * @param filepath - JSON filepath of Game
   */
  public Builder(String filepath) {
    this.filepath = filepath;

    try {
      Parser<?> parser = new JsonParser(filepath);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode newNode = mapper.createObjectNode();
      game = (Game) parser.parse(newNode);
    } catch (ParsingException e) {
      throw new IllegalStateException("Failed to parse game JSON file: " + e.getMessage(), e);
    }

    game.goToScene(game.getLevelOrder().getFirst());
    game.step(0);
    currentScene = game.getCurrentScene();
  }

  /**
   Allows for actions to be pushed into the undo deque outside of Builder.
   * */
  public void pushAction(EditorAction action)
  {
    undoStack.push(action);
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
    if (object.hasComponent(Transform.class))
    {
      selectedObjectPrevX = object.getComponent(Transform.class).getX();
      selectedObjectPrevY = object.getComponent(Transform.class).getY();
    }
    selectedObject = object;
  }

  /**
   * Sets selectedObject pointer to null
   * */
  public void deselect()
  {
    selectedObject = null;
  }

  /**
   *  Records when two game objects overlap
   */
  public boolean overlaps(GameObject currentObject)
  {
    for (GameObject object : game.getCurrentScene().getAllObjects())
    {
      if (!currentObject.equals(object) && object.hasComponent(Transform.class) && object.getComponent(Transform.class).getX() == currentObject.getComponent(Transform.class).getX() && object.getComponent(Transform.class).getY() == currentObject.getComponent(Transform.class).getY())
      {
        return true;
      }
    }
    return false;
  }

  /**
   *  Stops the preview if the user lifts mouse and cursor is not on the editor screen.
   *  Game object should be instantiated after mouse is released
   */
  public void placeObject(double x, double y) {
    if (selectedObject != null && selectedObject.hasComponent(Transform.class)) {
      undoStack.push(new MoveObjectAction(selectedObject, selectedObjectPrevX, selectedObjectPrevY, x, y));
      selectedObject.getComponent(Transform.class).setX(x);
      selectedObject.getComponent(Transform.class).setY(y);
    }
  //  selectedObject = null; //should I add exception?
  }

  /**
   *  Checks if the user is currently dragging around a game object
   */
  public boolean objectIsSelected() {
    return selectedObject != null;
  }

  /**
   * Tracks coordinates of the object as its dragged
   * @param x tracks the x position of the object
   * @param y tracks the y position of the object
   * */
  public void moveObject(double x, double y)
  {
    if (selectedObject != null && selectedObject.hasComponent(Transform.class))
    {
      selectedObject.getComponent(Transform.class).setX(x);
      selectedObject.getComponent(Transform.class).setY(y);
    }
  }

  /**
   * Loads new objects into the scene
   * @param object prefabricated game object
   * @param previewHorizontalMidpoint horizontal midpoint of the screen
   * @param previewVerticalMidpoint vertical midpoint of the screen.
   * */
  public void addObject(GameObject object, double previewHorizontalMidpoint, double previewVerticalMidpoint)
  {
    Transform t = object.getComponent(Transform.class);
    if (t != null)
    {
      double objectWidth = object.getComponent(Transform.class).getScaleX();
      double objectHeight = object.getComponent(Transform.class).getScaleY();
      object.getComponent(Transform.class).setX(previewHorizontalMidpoint - (objectWidth / 2));
      object.getComponent(Transform.class).setY(previewVerticalMidpoint - (objectHeight / 2));
      currentScene.registerObject(object);
      undoStack.add(new CreateObjectAction(game, object));
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
      deselect();
    }
  }

  /**
   *  Resizes the selected object
   * @param x - x position
   * @param y  - y position
   * @param h - height
   * @param w - width
   */
  public void resizeObject(double x, double y, double w, double h) {
    if (selectedObject != null && selectedObject.hasComponent(Transform.class)) {
      Transform t = selectedObject.getComponent(Transform.class);
      TransformState prev = new TransformState(t.getX(), t.getY(), t.getScaleX(), t.getScaleY());
      TransformState next = new TransformState(x, y, w, h);
      undoStack.push(new ResizeObjectAction(selectedObject, prev, next));
      t.setX(x);
      t.setY(y);
      t.setScaleX(w);
      t.setScaleY(h);
    }
  }


  /**
   *  Returns the currently selected object
   */
  public GameObject getSelectedObject()
  {
    return selectedObject;
  }

  /**
   *  Returns the current scene
   */

  public GameScene getCurrentScene()
  {
    return currentScene;
  }

  /**
   * Save the currently loaded Game object as a JSON file using the JsonParser
   * @param filepath location of JSON file
   */
  public JsonNode saveGameAs(String filepath) {
    // TODO: Need to create a new file if it doesn't exist
    JsonParser parser = new JsonParser(filepath);
    try {
      return parser.write(game);
    } catch (IOException e) {
      throw new SaveGameException("Error saving game to JSON: " + e.getMessage(), e);
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
      throw new LoadGameException("Error loading game from JSON: " + e.getMessage(), e);
    }
  }
}
