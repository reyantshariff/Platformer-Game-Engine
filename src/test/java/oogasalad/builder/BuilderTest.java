package oogasalad.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.Deque;
import java.util.List;
import oogasalad.model.builder.Builder;
import oogasalad.model.builder.EditorAction;
import oogasalad.model.builder.actions.MoveObjectAction;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.base.architecture.Game;
import oogasalad.model.engine.component.Transform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BuilderTest {

  private Builder builder;
  private GameObject object;
  @BeforeEach
  public void setUp() throws Exception {
    Game game = new Game();
    GameScene scene = new GameScene("test");
    game.addScene(scene);
    game.changeScene("test");
    object = new GameObject("TestObject");
    object.addComponent(Transform.class);
    object.getComponent(Transform.class).setX(0);
    object.getComponent(Transform.class).setY(0);
    scene.registerObject(object);
    builder = new Builder(game);

  }

  @Test
  public void deleteSelectedObject_selectedObject_unregister() {
    builder.selectExistingObject(object);
    assertTrue(builder.getSelectedObject().equals(object));
    builder.deleteSelectedObject();
    assertFalse(builder.getCurrentScene().getAllObjects().contains(object));
  }

  @Test
  public void deleteSelectedObject_noSelectedObject_returnsTrue()
  {
    builder.deleteSelectedObject();
    assertTrue(builder.getCurrentScene().getAllObjects().contains(object));

  }

  @Test
  public void placeObject_selectedObject_movesToCorrectLocation() {

    builder.selectExistingObject(object);

    double newX = 100;
    double newY = 200;

    builder.placeObject(newX, newY);

    // Check that object has been moved to the new location
    assertEquals(newX, object.getComponent(Transform.class).getX());
    assertEquals(newY, object.getComponent(Transform.class).getY());
  }

  @Test
  public void placeObject_noSelectedObject_doesNothing() {
    double originalX = object.getComponent(Transform.class).getX();
    double originalY = object.getComponent(Transform.class).getY();
    builder.placeObject(500, 500); // should not affect anything
    assertEquals(object.getComponent(Transform.class).getX(), originalX);
    assertEquals(object.getComponent(Transform.class).getY(), originalY);
  }

  @Test
  public void undoLastAction_moveObject_restoresPreviousPosition() {
    builder.selectExistingObject(object);

    // Place the object at a new location
    double newX = 100;
    double newY = 200;
    builder.placeObject(newX, newY);

    // Undo the move
    builder.undoLastAction();

    // Should be back at original position
    assertEquals(0, object.getComponent(Transform.class).getX());
    assertEquals(0, object.getComponent(Transform.class).getY());
  }

  @Test
  public void undoLastAction_emptyStack_doesNothing() {
    double originalX = object.getComponent(Transform.class).getX();
    double originalY = object.getComponent(Transform.class).getY();

    // No actions yet, so undo shouldn't do anything
    builder.undoLastAction();

    assertEquals(object.getComponent(Transform.class).getX(), originalX);
    assertEquals(object.getComponent(Transform.class).getY(), originalY);
  }

  @Test
  public void getSelectedObject_returnsCorrectObject() {
    assertNull(builder.getSelectedObject());
    builder.selectExistingObject(object);
    assertEquals(object, builder.getSelectedObject());
  }

  @Test
  public void resizeObject_selectedObject_resizesCorrectly() {
    builder.selectExistingObject(object);

    double newX = 120.0;
    double newY = 80.0;
    double newWidth = 40.0;
    double newHeight = 60.0;

    builder.resizeObject(newX, newY, newWidth, newHeight);

    Transform t = object.getComponent(Transform.class);

    assertEquals(newX, t.getX());
    assertEquals(newY, t.getY());
    assertEquals(newWidth, t.getScaleX());
    assertEquals(newHeight, t.getScaleY());
  }


  @Test
  public void objectIsSelected_returnsCorrectStatus() {
    assertFalse(builder.objectIsSelected());
    builder.selectExistingObject(object);
    assertTrue(builder.objectIsSelected());
    builder.deselect();
    assertFalse(builder.objectIsSelected());
  }

  @Test
  public void getCurrentScene_returnsCorrectScene() {
    assertNotNull(builder.getCurrentScene());
    assertEquals("test", builder.getCurrentScene().getName());
  }

  @Test
  public void pushAction_pushesCorrectAction() throws Exception {
    MoveObjectAction action = new MoveObjectAction(object, 0, 0, 10, 10);
    builder.pushAction(action);

    Field undoStackField = Builder.class.getDeclaredField("undoStack");
    undoStackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Deque<EditorAction> undoStack = (Deque<EditorAction>) undoStackField.get(builder);

    assertFalse(undoStack.isEmpty(), "Undo stack should contain the pushed action");
    assertSame(action, undoStack.peek(), "The pushed action should be on top of the undo stack");
  }

  @Test
  public void redoLastAction_moveObject_redoesMove() {
    builder.selectExistingObject(object);

    double newX = 150;
    double newY = 250;

    // Place the object at a new position (records the action)
    builder.placeObject(newX, newY);

    // Undo the move (restores old position)
    builder.undoLastAction();

    // Redo the move (should go back to new position)
    builder.redoLastAction();

    assertEquals(newX, object.getComponent(Transform.class).getX());
    assertEquals(newY, object.getComponent(Transform.class).getY());
  }

}
