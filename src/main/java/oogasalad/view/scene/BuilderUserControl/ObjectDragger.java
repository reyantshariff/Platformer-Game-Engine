package oogasalad.view.scene.BuilderUserControl;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import oogasalad.model.builder.Builder;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.component.Camera;
import oogasalad.model.engine.component.Transform;
import oogasalad.view.gui.GameObjectRenderer;

import java.util.ArrayList;
import java.util.List;
import oogasalad.view.scene.BuilderScene;

/**
 * ObjectDragger manages mouse drag and drop for Builder pages
 * @author Reyan Shariff
 */
public class ObjectDragger {

  private final Canvas canvas;
  private final Builder builder;
  private final BuilderScene builderScene;
  private final GameScene gameScene;
  private final GameObjectRenderer renderer;

  private boolean dragging = false;
  private double dragOffsetX = 0;
  private double dragOffsetY = 0;

  private double oldX = 0;
  private double oldY = 0;

  public ObjectDragger(Canvas canvas, Builder builder, BuilderScene builderScene, GameObjectRenderer renderer) {
    this.canvas = canvas;
    this.builder = builder;
    this.builderScene = builderScene;
    this.gameScene = builder.getCurrentScene();
    this.renderer = renderer;
  }

  /**
   * Sets up mouse inputs
   * **/

  public void setupListeners() {
    canvas.setOnMousePressed(this::handlePressedIfInBounds);
    canvas.setOnMouseDragged(this::handleDraggedIfInBounds);
    canvas.setOnMouseReleased(this::handleReleasedIfInBounds);
  }

  private void handlePressedIfInBounds(MouseEvent e) {
    if (!isInCanvas(e)) return;
    handlePressed(e);
  }

  private void handleDraggedIfInBounds(MouseEvent e) {
    if (!isInCanvas(e)) return;
    handleDragged(e);
  }

  private void handleReleasedIfInBounds(MouseEvent e) {
    if (!isInCanvas(e)) return;
    handleReleased(e);
  }

  private boolean isInCanvas(MouseEvent e) {
    double x = e.getX();
    double y = e.getY();
    return x >= 0 && x <= canvas.getWidth() &&
        y >= 0 && y <= canvas.getHeight();
  }

  /**
   * Checks if mouse is touching sprite and then records it as selected
   * @param e is the event click
   * */

  private void handlePressed(MouseEvent e) {
    oldX = e.getX();
    oldY = e.getY();
    List<GameObject> objects = new ArrayList<>(gameScene.getAllObjects());
    objects = removeCamerasFromObjects(objects);  // FOR THE BUILDER: remove all cameras

    for (GameObject obj : objects) {
      if (!obj.hasComponent(Transform.class)) continue;
      Transform t = obj.getComponent(Transform.class);
      double w = obj.getComponent(Transform.class).getScaleX(), h = obj.getComponent(Transform.class).getScaleY();

      if (oldX >= t.getX() && oldX <= t.getX() + w && oldY >= t.getY() && oldY <= t.getY() + h) {
        builder.selectExistingObject(obj);
        dragOffsetX = oldX - t.getX();
        dragOffsetY = oldY - t.getY();
        dragging = true;
        break;
      }
    }

    builderScene.updateGamePreview();  // refresh all sprite visuals in the canvas
  }

  private void handleDragged(MouseEvent e) {
    if (dragging && builder.objectIsSelected()) {
      double newX = e.getX() - dragOffsetX;
      double newY = e.getY() - dragOffsetY;
      builder.moveObject(newX, newY);
      renderer.renderWithoutCamera(canvas.getGraphicsContext2D(), gameScene);
    }

    builderScene.updateGamePreview();  // refresh all sprite visuals in the canvas
  }

  private void handleReleased(MouseEvent e) {
    if (isInCanvas(e) && dragging && builder.objectIsSelected()) {
      dragging = false;
      double newX = e.getX() - dragOffsetX;
      double newY = e.getY() - dragOffsetY;
      builder.placeObject(newX, newY);
      renderer.renderWithoutCamera(canvas.getGraphicsContext2D(), gameScene);
    }

    builderScene.updateGamePreview();  // refresh all sprite visuals in the canvas
  }

  private List<GameObject> removeCamerasFromObjects(List<GameObject> objects) {
    // remove any camera objects so they cannot be "pressed"
    List<GameObject> camerasToRemove = new ArrayList<>();
    for (GameObject object : objects) {
      try {
        object.getComponent(Camera.class);
        camerasToRemove.add(object);
      } catch (IllegalArgumentException e) {
        continue;
      }
    }
    for (GameObject cameraObj : camerasToRemove) {
      objects.remove(cameraObj);
    }

    return objects;
  }
}
