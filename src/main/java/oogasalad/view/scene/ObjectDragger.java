package oogasalad.view.scene;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import oogasalad.model.builder.Builder;
import oogasalad.model.engine.base.architecture.GameObject;
import oogasalad.model.engine.base.architecture.GameScene;
import oogasalad.model.engine.component.Transform;
import oogasalad.view.gui.GameObjectRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * ObjectDragger manages mouse drag and drop
 * @author Reyan Shariff
 */
public class ObjectDragger {

  private final Canvas canvas;
  private final Builder builder;
  private final BuilderView builderView;
  private final GameScene gameScene;
  private final GameObjectRenderer renderer;

  private boolean dragging = false;
  private double dragOffsetX = 0;
  private double dragOffsetY = 0;

  public ObjectDragger(Canvas canvas, Builder builder, BuilderView builderView, GameScene gameScene, GameObjectRenderer renderer) {
    this.canvas = canvas;
    this.builder = builder;
    this.builderView = builderView;
    this.gameScene = gameScene;
    this.renderer = renderer;
    setupListeners();
  }

  /**
   * Sets up mouse inputs
   * **/

  private void setupListeners() {
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
    double x = e.getX(), y = e.getY();
    List<GameObject> objects = new ArrayList<>(gameScene.getAllObjects());

    for (GameObject obj : objects) {
      if (!obj.hasComponent(Transform.class)) continue;
      Transform t = obj.getComponent(Transform.class);
      double w = obj.getComponent(Transform.class).getScaleX(), h = obj.getComponent(Transform.class).getScaleY();

      if (x >= t.getX() && x <= t.getX() + w && y >= t.getY() && y <= t.getY() + h) {
        builder.selectExistingObject(obj);
        dragOffsetX = x - t.getX();
        dragOffsetY = y - t.getY();
        dragging = true;
        break;
      }
    }

    builderView.updateGamePreview();  // refresh all sprite visuals in the canvas
  }

  private void handleDragged(MouseEvent e) {
    if (dragging && builder.objectIsSelected()) {
      double newX = e.getX() - dragOffsetX;
      double newY = e.getY() - dragOffsetY;
      builder.moveObject(newX, newY);
      renderer.render(canvas.getGraphicsContext2D(), gameScene);
    }

    builderView.updateGamePreview();  // refresh all sprite visuals in the canvas
  }

  private void handleReleased(MouseEvent e) {
    if (isInCanvas(e) && dragging && builder.objectIsSelected()) {
      builder.placeObject(e.getX(), e.getY());
      dragging = false;
      renderer.render(canvas.getGraphicsContext2D(), gameScene);
    }

    builderView.updateGamePreview();  // refresh all sprite visuals in the canvas
  }
}
