package oogasalad.engine.component;

import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.serialization.SerializableField;

/**
 * This component is for all game objects that are rendered from image files
 *
 * @ Jack F. Regan
 */
public class ImageComponent extends GameComponent {

  @SerializableField
  private String imagePath;

  /** The x-coordinate where the image should be rendered. */
  @SerializableField
  private double x;

  /** The y-coordinate where the image should be rendered. */
  @SerializableField
  private double y;
  @SerializableField
  private double scaleX;
  @SerializableField
  private double scaleY;
  @SerializableField
  private double rotation;

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
  public double getScaleX() {
    return scaleX;
  }

  /**
   * Sets the path to the image file used for rendering.
   *
   * @param imagePath the image file path
   */
  public void setScaleX(double scaleX) {
    this.scaleX = scaleX;
  }

  /**
   * Sets the path to the image file used for rendering.
   *
   * @param imagePath the image file path
   */
  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public double getScaleY() {
    return scaleY;
  }

  public void setScaleY(double scaleY) {
    this.scaleY = scaleY;
  }

  public double getRotation() {
    return rotation;
  }

  public void setRotation(double rotation) {
    this.rotation = rotation;
  }
}
