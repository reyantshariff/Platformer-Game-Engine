package oogasalad.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import oogasalad.model.builder.Builder;
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
  public void setUp()
  {
    GameScene scene = new GameScene("empty");
    object = new GameObject("GameObject");
    builder = new Builder(scene); // use new constructor
    builder.getCurrentScene().registerObject(object);
    object.addComponent(Transform.class);
    object.getComponent(Transform.class).setY(0);
    object.getComponent(Transform.class).setX(0);

  }
  @Test
  public void deleteSelectedObject_selectedObject_unregister() {
    builder.selectExistingObject(object);
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
  public void overlaps_TestsforOverlapwithItself_returnsFalse()
  {
    assertFalse(builder.overlaps(object));
  }

  @Test
  public void overlaps_TestforOverlapwithOtherObject_returnsTrue()
  {
    GameObject otherObject = new GameObject("GameObject");
    builder.getCurrentScene().registerObject(otherObject);
    otherObject.addComponent(Transform.class);
    otherObject.getComponent(Transform.class).setY(0);
    otherObject.getComponent(Transform.class).setX(0);
    builder.selectExistingObject(object);
    assertTrue(builder.overlaps(otherObject));
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
