package oogasalad.view.scene.builder.builderControl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import oogasalad.model.builder.Builder;
import oogasalad.model.builder.TransformState;
import oogasalad.model.builder.actions.ResizeObjectAction;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.component.Camera;
import oogasalad.model.engine.component.Transform;
import oogasalad.view.renderer.GameSceneRenderer;

import java.util.ArrayList;
import java.util.List;
import oogasalad.view.scene.builder.BuilderScene;

/**
 * ObjectDragger manages mouse drag and drop for Builder pages
 * @author Reyan Shariff
 */
public class ObjectDragger {

  private static final double HANDLE_SIZE = 8;

  private final Canvas canvas;
  private final BuilderScene builderScene;
  private final GameSceneRenderer renderer;
  private final DragContext dragContext = new DragContext();
  private final Map<ResizeHandle, BiConsumer<Double, Double>> enumMap;

  private Builder builder;
  private GameScene gameScene;

  private double oldX = 0;
  private double oldY = 0;

  private double newX, newY, newW, newH;
  private double resizeStartX, resizeStartY, resizeStartW, resizeStartH;

  /**
   * Constructor for ObjectDragger
   * @param canvas is the canvas to draw on
   * @param builderScene is the builder scene
   * @param renderer is the renderer for the game scene
   */
  public ObjectDragger(Canvas canvas, BuilderScene builderScene, GameSceneRenderer renderer) {
    this.canvas = canvas;
    this.builderScene = builderScene;
    this.renderer = renderer;
    this.enumMap = new HashMap<>();
  }

  /**
   * Sets up builder
   */
  public void setUpBuilder(Builder builder) {
    this.builder = builder;
    this.gameScene = builder.getCurrentScene();
  }

  /**
   * Sets up mouse inputs
   */
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

    GameObject prevObject = builder.getSelectedObject();
    List<GameObject> objects = new ArrayList<>(gameScene.getAllObjects());
    objects = removeCamerasFromObjects(objects);

    boolean clickedObject = false;

    for (GameObject obj : objects) {
      if (!obj.hasComponent(Transform.class)) continue;

      Transform t = obj.getComponent(Transform.class);

      if (isSelectedAndResizing(obj, t)) {
        startResizing(e, t);
        return;
      }

      if (isInsideBoundingBox(oldX, oldY, t)) {
        startMoving(obj, e, t);
        clickedObject = true;
      }
    }

    handleDeselection(prevObject, clickedObject);
    builderScene.updateGamePreview();
  }

  private boolean isSelectedAndResizing(GameObject obj, Transform t) {
    return builder.objectIsSelected() && builder.getSelectedObject().equals(obj) && isHoveringOverResizeHandle(oldX, oldY);
  }

  private void startMoving(GameObject obj, MouseEvent e, Transform t)
  {
    builder.selectExistingObject(obj);
    double offsetX = e.getX() - t.getX();
    double offsetY = e.getY() - t.getY();
    dragContext.beginDrag(e, offsetX, offsetY, false);
  }

  private boolean isInsideBoundingBox(double mouseX, double mouseY, Transform t) {
    double w = t.getScaleX();
    double h = t.getScaleY();
    return mouseX >= t.getX() && mouseX <= t.getX() + w &&
        mouseY >= t.getY() && mouseY <= t.getY() + h;
  }

  private void startResizing(MouseEvent e, Transform t) {
    resizeStartX = t.getX();
    resizeStartY = t.getY();
    resizeStartW = t.getScaleX();
    resizeStartH = t.getScaleY();

    double dragOffsetX = oldX - t.getX();
    double dragOffsetY = oldY - t.getY();

    dragContext.beginDrag(e, dragOffsetX, dragOffsetY, true);
  }

  private void handleDeselection(GameObject prevObject, boolean clickedObject) {
    if (builder.objectIsSelected() && !clickedObject) {
      recordResizing();
      builder.deselect();
      dragContext.endInteraction();
    }

    if (prevObject != builder.getSelectedObject()) {
      builderScene.handleObjectSelectionChange();
    }
  }

  private void handleDragged(MouseEvent e) {
    if (!builder.objectIsSelected() || !dragContext.isActive()) return;
    if (dragContext.isResizing()) {
      double dx = dragContext.deltaX(e);
      double dy = dragContext.deltaY(e);
      resizeFromHandle(builder.getSelectedObject(), dx, dy);
    } else {
      builder.moveObject(dragContext.newX(e), dragContext.newY(e));
    }

    dragContext.updateCoordinates(e);
    renderer.renderWithoutCamera(canvas.getGraphicsContext2D(), gameScene, null);
    builderScene.updateGamePreview();
  }


  private boolean isHoveringOverResizeHandle(double x, double y) {
    Point2D mousePoint = new Point2D(x, y);
    List<GameObject> objects = new ArrayList<>(gameScene.getAllObjects());
    objects = removeCamerasFromObjects(objects);
    for (GameObject obj : objects) {
      Transform t = obj.getComponent(Transform.class);

      double w = t.getScaleX();
      double h = t.getScaleY();

      List<Point2D> resize_handles = List.of(
          new Point2D(t.getX(), t.getY()),
          new Point2D(t.getX() + w / 2, t.getY()),
          new Point2D(t.getX() + w, t.getY()),
          new Point2D(t.getX() + w, t.getY() + h / 2),
          new Point2D(t.getX() + w, t.getY() + h),
          new Point2D(t.getX() + w / 2, t.getY() + h),
          new Point2D(t.getX(), t.getY() + h),
          new Point2D(t.getX(), t.getY() + h / 2)
      );

      /**
       checks if the mouse is within a small circular area (radius = half the handle size) around a handle,
       meaning the user clicked on that handle.
       */
      for (int i = 0; i < resize_handles.size(); i++) {
        if (mousePoint.distance(resize_handles.get(i)) <= HANDLE_SIZE / 2) {
          dragContext.activateHandle(ResizeHandle.values()[i]);  // Use enum here
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

    initEnumMap(x, y, w, h);

    ResizeHandle handle = dragContext.getActiveHandle();
    BiConsumer<Double, Double> handleMapping = enumMap.get(handle);
    if (handleMapping != null) {
      handleMapping.accept(dx, dy);
    }

    if (newW > 1 && newH > 1) {
      builder.resizeObject(newX, newY, newW, newH);
    }
  }

  private void initEnumMap(double x, double y, double w, double h) {
    newX = x;
    newY = y;
    newW = w;
    newH = h;

    enumMap.clear(); // Ensure it's fresh each time

    enumMap.put(ResizeHandle.TOP_LEFT, (dx, dy) -> {
      newX += dx; newY += dy; newW -= dx; newH -= dy;
    });

    enumMap.put(ResizeHandle.TOP_CENTER, (dx, dy) -> {
      newY += dy; newH -= dy;
    });

    enumMap.put(ResizeHandle.TOP_RIGHT, (dx, dy) -> {
      newY += dy; newW += dx; newH -= dy;
    });

    enumMap.put(ResizeHandle.MID_RIGHT, (dx, dy) -> {
      newW += dx;
    });

    enumMap.put(ResizeHandle.BOTTOM_RIGHT, (dx, dy) -> {
      newH += dy; newW += dx;
    });

    enumMap.put(ResizeHandle.BOTTOM_CENTER, (dx, dy) -> {
      newH += dy;
    });

    enumMap.put(ResizeHandle.BOTTOM_LEFT, (dx, dy) -> {
      newX += dx; newW -= dx; newH += dy;
    });

    enumMap.put(ResizeHandle.MID_LEFT, (dx, dy) -> {
      newX += dx; newW -= dx;
    });
  }


  private void handleReleased(MouseEvent e) {
    if (!isInCanvas(e) || !builder.objectIsSelected()) return;

    if (dragContext.isDragging() && !dragContext.isResizing()) {
      builder.placeObject(dragContext.newX(e), dragContext.newY(e));
    }

    else if (dragContext.isResizing())
    {
      recordResizing();
    }

    dragContext.endInteraction();
    builderScene.handleObjectAttributeChange();
    builderScene.updateGamePreview();
  }

  private void recordResizing()
  {
    Transform t = builder.getSelectedObject().getComponent(Transform.class);
    TransformState prev = new TransformState(t.getX(), t.getY(), t.getScaleX(), t.getScaleY());
    TransformState next = new TransformState(resizeStartX, resizeStartY, resizeStartW, resizeStartH);
    ResizeObjectAction resizeAction = new ResizeObjectAction(
        builder.getSelectedObject(), prev, next
    );

    builder.pushAction(resizeAction);
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
