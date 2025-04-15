package oogasalad.view.scene.builder.builderControl;

import javafx.scene.input.MouseEvent;

/**
 * DragContext encapsulates the logic of a mouse drag or resize interaction.
 * It is responsible for maintaining and updating internal state during a drag.
 * @author Reyan Shariff
 */
public class DragContext {

  private double oldX, oldY;
  private double dragOffsetX, dragOffsetY;
  private ResizeHandle activeHandle;
  private boolean dragging;
  private boolean resizing;

  /**
   * Constructs a fresh DragContext with default values.
   */
  public DragContext() {
    oldX = 0;
    oldY = 0;
    dragOffsetX = 0;
    dragOffsetY = 0;
    activeHandle = null;
    dragging = false;
    resizing = false;
  }

  // === Behavioral methods ===

  /**
   * monitors coordinates, offsets, and states
   */
  public void beginDrag(MouseEvent e, double offsetX, double offsetY, boolean resizing) {
    oldX = e.getX();
    oldY = e.getY();
    dragOffsetX = offsetX;
    dragOffsetY = offsetY;
    dragging = true;
    this.resizing = resizing;
    if (!resizing) {
      activeHandle = null;
    }
  }

  /**
   * Updates the prev values for the x and y coordinates
   * */
  public void updateCoordinates(MouseEvent e) {
    oldX = e.getX();
    oldY = e.getY();
  }

  /**
   * Reverts states after ending an interaction.
   * */
  public void endInteraction() {
    dragging = false;
    resizing = false;
    activeHandle = null;
  }
  /**
   * sets handle index value
   * */
  public void activateHandle(ResizeHandle handle) {
    activeHandle = handle;
    resizing = true;
  }

  // === State queries ===
  /**
   * checks if the mouse is just moving around an object
   * */
  public boolean isDragging() {
    return dragging && !resizing;
  }
  /**
   * checks if the mouse is resizing an object
   * */
  public boolean isResizing() {
    return resizing;
  }

  /**
   * checks if the mouse is dragging or resizing.
   * */

  public boolean isActive() {
    return dragging || resizing;
  }

  // === Position helpers ===

  /**
   * Tracks distance between object x position and mouse x position.
   * */
  public double deltaX(MouseEvent e) {
    return e.getX() - oldX;
  }

  /**
   * Tracks distance between object y position and mouse y position.
   * */
  public double deltaY(MouseEvent e) {
    return e.getY() - oldY;
  }

  /**
   *Calculates the new x position of an object
   */
  public double newX(MouseEvent e) {
    return e.getX() - dragOffsetX;
  }

  /**
   *Calculates the new y position of an object
   */
  public double newY(MouseEvent e) {
    return e.getY() - dragOffsetY;
  }

  // === Getters ===

  /**
   *returns the old x value
   */

  public double getOldX() {
    return oldX;
  }

  /**
   *returns the old y value
   */
  public double getOldY() {
    return oldY;
  }

  /**
   *returns the x offset
   */
  public double getDragOffsetX() {
    return dragOffsetX;
  }

  /**
   *returns the y offset
   */
  public double getDragOffsetY() {
    return dragOffsetY;
  }

  /**
   *returns the handle enum
   */
  public ResizeHandle getActiveHandle() {
    return activeHandle;
  }
}
