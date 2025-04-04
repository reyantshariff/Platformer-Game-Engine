package oogasalad.engine.component;

import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.serialization.SerializableField;

/**
 * A visual component that represents an image to be rendered at a specific position.
 * This component stores the image path and its intended render coordinates (x, y).
 */
public class ImageComponent extends GameComponent {

  /** The path to the image file used for rendering. */
  @SerializableField
  private String imagePath;

  /** The x-coordinate where the image should be rendered. */
  @SerializableField
  private double x;

  /** The y-coordinate where the image should be rendered. */
  @SerializableField
  private double y;

  /**
   * Returns the component tag identifying this as a render-related component.
   *
   * @return {@link ComponentTag#RENDER}
   */
  @Override
  public ComponentTag componentTag() {
    return ComponentTag.RENDER;
  }

  /**
   * Returns the x-coordinate for rendering the image.
   *
   * @return the x position
   */
  public double getX() {
    return x;
  }

  /**
   * Sets the x-coordinate for rendering the image.
   *
   * @param x the new x position
   */
  public void setX(double x) {
    this.x = x;
  }

  /**
   * Returns the y-coordinate for rendering the image.
   *
   * @return the y position
   */
  public double getY() {
    return y;
  }

  /**
   * Sets the y-coordinate for rendering the image.
   *
   * @param y the new y position
   */
  public void setY(double y) {
    this.y = y;
  }

  /**
   * Returns the path to the image file used for rendering.
   *
   * @return the image path
   */
  public String getImagePath() {
    return imagePath;
  }

  /**
   * Sets the path to the image file used for rendering.
   *
   * @param imagePath the image file path
   */
  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  /**
   * Updates the component. Image components typically do not require updates over time.
   *
   * @param deltaTime time passed since the last update
   */
  @Override
  public void update(double deltaTime) {
    // Image components typically don't update over time.
  }
}
