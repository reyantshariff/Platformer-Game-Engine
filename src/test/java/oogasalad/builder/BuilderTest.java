// package oogasalad.builder;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import oogasalad.model.builder.Builder;
// import oogasalad.model.engine.base.architecture.GameObject;
// import oogasalad.model.engine.base.architecture.GameScene;
// import oogasalad.model.engine.base.architecture.Game;
// import oogasalad.model.engine.component.Transform;
// import org.junit.jupiter.api.Test;

// public class BuilderTest {
//   @Test
//   public void deleteSelectedObject_selectedObject_unregister() {
//     Game game = new Game();
//     GameScene scene = new GameScene("scene");
//     GameObject object = new GameObject("GameObject");

//     game.addScene(scene);
//     scene.registerObject(object);

//     Builder builder = new Builder(game); // use new constructor
//     builder.selectExistingObject(object.getId()); // selects it

//     builder.deleteSelectedObject();

//     assertFalse(scene.getAllObjects().contains(object));
//   }

//   @Test
//   public void deleteSelectedObject_noSelectedObject_returnsTrue()
//   {
//     Game game = new Game();
//     GameScene scene = new GameScene("scene");
//     GameObject object = new GameObject("GameObject");

//     game.addScene(scene);
//     scene.registerObject(object);

//     Builder builder = new Builder(game);

//     builder.deleteSelectedObject();

//     assertTrue(scene.getAllObjects().contains(object));

//   }

//   @Test
//   public void overlaps_TestsforOverlapwithItself_returnsFalse()
//   {
//     Game game = new Game();
//     GameScene scene = new GameScene("scene");
//     game.addScene(scene);
//     game.changeScene("scene");
//     GameObject object = new GameObject("GameObject");
//     scene.registerObject(object);
//     object.getComponent(Transform.class).setY(0);
//     object.getComponent(Transform.class).setX(0);
//     Builder builder = new Builder(game);
//     builder.selectExistingObject(object.getId());
//     assertFalse(builder.overlaps(object));
//   }

//   @Test
//   public void overlaps_TestforOverlapwithOtherObject_returnsTrue()
//   {
//     Game game = new Game();
//     GameScene scene = new GameScene("scene");
//     game.addScene(scene);
//     game.changeScene("scene");

//     GameObject object = new GameObject("GameObject");
//     GameObject otherObject = new GameObject("GameObject");
//     scene.registerObject(object);
//     scene.registerObject(otherObject);
//     object.getComponent(Transform.class).setY(0);
//     object.getComponent(Transform.class).setX(0);
//     otherObject.getComponent(Transform.class).setY(0);
//     otherObject.getComponent(Transform.class).setX(0);
//     Builder builder = new Builder(game);
//     builder.selectExistingObject(object.getId());
//     assertTrue(builder.overlaps(otherObject));
//   }

//   @Test
//   public void placeObject_selectedObject_movesToCorrectLocation() {
//     Game game = new Game();
//     GameScene scene = new GameScene("scene");
//     game.addScene(scene);
//     game.changeScene("scene");

//     GameObject object = new GameObject("GameObject");
//     scene.registerObject(object);

//     // Set initial position
//     object.getComponent(Transform.class).setX(0);
//     object.getComponent(Transform.class).setY(0);

//     Builder builder = new Builder(game);
//     builder.selectExistingObject(object.getId());

//     double newX = 100;
//     double newY = 200;

//     builder.placeObject(newX, newY);

//     // Check that object has been moved to the new location
//     assertEquals(newX, object.getComponent(Transform.class).getX());
//     assertEquals(newY, object.getComponent(Transform.class).getY());
//   }

//   @Test
//   public void placeObject_noSelectedObject_doesNothing() {
//     Game game = new Game();
//     GameScene scene = new GameScene("scene");
//     game.addScene(scene);
//     game.changeScene("scene");

//     GameObject object = new GameObject("GameObject");
//     scene.registerObject(object);

//     double originalX = object.getComponent(Transform.class).getX();
//     double originalY = object.getComponent(Transform.class).getY();

//     Builder builder = new Builder(game);

//     builder.placeObject(500, 500); // should not affect anything

//     assertEquals(object.getComponent(Transform.class).getX(), originalX);
//     assertEquals(object.getComponent(Transform.class).getY(), originalY);
//   }

//   @Test
//   public void undoLastAction_moveObject_restoresPreviousPosition() {
//     Game game = new Game();
//     GameScene scene = new GameScene("scene");
//     game.addScene(scene);
//     game.changeScene("scene");

//     GameObject object = new GameObject("GameObject");
//     scene.registerObject(object);

//     object.getComponent(Transform.class).setX(0);
//     object.getComponent(Transform.class).setY(0);

//     Builder builder = new Builder(game);
//     builder.selectExistingObject(object.getId());

//     // Place the object at a new location
//     double newX = 100;
//     double newY = 200;
//     builder.placeObject(newX, newY);

//     // Undo the move
//     builder.undoLastAction();

//     // Should be back at original position
//     assertEquals(0, object.getComponent(Transform.class).getX());
//     assertEquals(0, object.getComponent(Transform.class).getY());
//   }

//   @Test
//   public void undoLastAction_emptyStack_doesNothing() {
//     Game game = new Game();
//     GameScene scene = new GameScene("scene");
//     game.addScene(scene);
//     game.changeScene("scene");

//     GameObject object = new GameObject("GameObject");
//     scene.registerObject(object);

//     double originalX = object.getComponent(Transform.class).getX();
//     double originalY = object.getComponent(Transform.class).getY();

//     Builder builder = new Builder(game);

//     // No actions yet, so undo shouldn't do anything
//     builder.undoLastAction();

//     assertEquals(object.getComponent(Transform.class).getX(), originalX);
//     assertEquals(object.getComponent(Transform.class).getY(), originalY);
//   }

//   @Test
//   public void redoLastAction_moveObject_redoesMove() {
//     Game game = new Game();
//     GameScene scene = new GameScene("scene");
//     game.addScene(scene);
//     game.changeScene("scene");

//     GameObject object = new GameObject("GameObject");
//     scene.registerObject(object);

//     object.getComponent(Transform.class).setX(0);
//     object.getComponent(Transform.class).setY(0);

//     Builder builder = new Builder(game);
//     builder.selectExistingObject(object.getId());

//     double newX = 150;
//     double newY = 250;

//     // Place the object at a new position (records the action)
//     builder.placeObject(newX, newY);

//     // Undo the move (restores old position)
//     builder.undoLastAction();

//     // Redo the move (should go back to new position)
//     builder.redoLastAction();

//     assertEquals(newX, object.getComponent(Transform.class).getX());
//     assertEquals(newY, object.getComponent(Transform.class).getY());
//   }






// }
