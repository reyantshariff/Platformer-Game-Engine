package oogasalad.view.scene.BuilderUserControl;

import java.awt.Point;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
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
  private boolean resizing = false;

  private final double HANDLE_SIZE = 8;

  private double oldX = 0;
  private double oldY = 0;

  private int activeHandleIndex = -1;
  private List<Point2D> resize_handles;

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
    canvas.setOnMouseMoved(e -> {
      boolean hovering = isHoveringOverResizeHandle(e.getX(), e.getY());
      canvas.setCursor(hovering ? Cursor.HAND : Cursor.DEFAULT);
    });


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
    Point2D click = new Point2D(oldX, oldY);

    boolean clickedObject = false;
    List<GameObject> objects = new ArrayList<>(gameScene.getAllObjects());
    objects = removeCamerasFromObjects(objects);

    for (GameObject obj : objects) {
      if (!obj.hasComponent(Transform.class)) continue;
      Transform t = obj.getComponent(Transform.class);

      double w = t.getScaleX();
      double h = t.getScaleY();

      if (builder.objectIsSelected() && builder.getSelectedObject().equals(obj) && isHoveringOverResizeHandle(oldX, oldY))
      {
        dragging = true;
        dragOffsetX = oldX - t.getX();
        dragOffsetY = oldY - t.getY();
        return;
      }

      // 🔹 Check if clicked inside bounding box
      if (oldX >= t.getX() && oldX <= t.getX() + w &&
          oldY >= t.getY() && oldY <= t.getY() + h) {
        builder.selectExistingObject(obj);
        dragging = true; // whole object move
        dragOffsetX = oldX - t.getX();
        dragOffsetY = oldY - t.getY();
        builderScene.handleObjectSelectionChange();
        activeHandleIndex = -1;
        clickedObject = true;
      }
    }

    // 🔹 If nothing was clicked
    if (!clickedObject) {
      builder.deselect();
    }

    builderScene.updateGamePreview();
  }


  private void handleDragged(MouseEvent e) {
    if (builder.objectIsSelected())
    {
      double newX = e.getX() - dragOffsetX;
      double newY = e.getY() - dragOffsetY;

      if (activeHandleIndex != -1)
      {
        double dx = e.getX() - oldX;
        double dy = e.getY() - oldY;
        resizing = true;
        resizeFromHandle(builder.getSelectedObject(), dx, dy);
      }

      else if (builder.objectIsSelected()) {
        builder.moveObject(newX, newY);
        renderer.renderWithoutCamera(canvas.getGraphicsContext2D(), gameScene);
      }

      oldX = e.getX();
      oldY = e.getY();

      builderScene.updateGamePreview();  // refresh all sprite visuals in the canvas
    }
  }

  private boolean isHoveringOverResizeHandle(double x, double y)
  {
    Point2D mousePoint = new Point2D(x, y);
    List<GameObject> objects = new ArrayList<>(gameScene.getAllObjects());
    objects = removeCamerasFromObjects(objects);
    for (GameObject obj : objects)
    {
      Transform t = obj.getComponent(Transform.class);

      double w = t.getScaleX();
      double h = t.getScaleY();

      resize_handles = List.of(
          new Point2D(t.getX(), t.getY()),
          new Point2D(t.getX() + w / 2, t.getY()),
          new Point2D(t.getX() + w, t.getY()),
          new Point2D(t.getX() + w, t.getY() + h / 2),
          new Point2D(t.getX() + w, t.getY() + h),
          new Point2D(t.getX() + w / 2, t.getY() + h),
          new Point2D(t.getX(), t.getY() + h),
          new Point2D(t.getX(), t.getY() + h / 2)
      );

      for (int i = 0; i < resize_handles.size(); i++) {
        /**
         checks if the mouse is within a small circular area (radius = half the handle size) around a handle,
         meaning the user clicked on that handle.
         */
        if (mousePoint.distance(resize_handles.get(i)) <= HANDLE_SIZE / 2) {
          activeHandleIndex = i;
          return true;
        }
      }
    }
    return false;
  }

  private void resizeFromHandle(GameObject obj, double dx, double dy) {
    Transform t = obj.getComponent(Transform.class);

    double x = t.getX();
    double y = t.getY();
    double w = t.getScaleX();
    double h = t.getScaleY();

    double newX = x, newY = y, newW = w, newH = h;

    switch (activeHandleIndex) {
      case 0 -> {    // top-left
        newX += dx;
        newY += dy;
        newW -= dx;
        newH -= dy;
      }
      case 1 -> {   // top-center
        newY += dy;
        newH -= dy;
      }
      case 2 -> {   // top-right
        newY += dy;
        newW += dx;
        newH -= dy;
      }
      case 3 ->  {  // mid-right
        newW += dx;
      }
      case 4 -> {   // bottom-right
        newH += dy;
        newW += dx;
      }
      case 5 -> {    // bottom-center
        newH += dy;
      }
      case 6 -> {    // bottom-left
        newX += dx;
        newW -= dx;
        newH += dy;
      }
      case 7 -> {   // mid-left
        newX += dx;
        newW -= dx;
      }
    }

    if (newW > 1 && newH > 1) {
      builder.resizeObject(newX, newY, newW, newH);
    }
  }



  private void handleReleased(MouseEvent e) {
    double newX = e.getX() - dragOffsetX;
    double newY = e.getY() - dragOffsetY;
    builderScene.handleObjectSelectionChange();

    if (isInCanvas(e) && dragging && builder.objectIsSelected() && !resizing) {
      dragging = false;
      builder.placeObject(newX, newY);
      renderer.renderWithoutCamera(canvas.getGraphicsContext2D(), gameScene);
    }
    else if (resizing)
    {
      resizing = false;
      dragging = false;
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
