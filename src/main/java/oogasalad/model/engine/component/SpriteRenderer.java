package oogasalad.model.engine.component;

import java.util.List;
import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import oogasalad.model.engine.base.serialization.SerializableField;

/**
 * A visual component that represents an image to be rendered at a specific position. This component
 * stores the image path and its intended render coordinates (x, y).
 */
public class SpriteRenderer extends GameComponent {
  @Override
  public ComponentTag componentTag() {
    return ComponentTag.RENDER;
  }

  @SerializableField
  private List<String> imagePaths;
  @SerializableField
  private double offsetX;
  @SerializableField
  private double offsetY;

  private int currentSpriteIndex;

  @Override
  public void start() {
    currentSpriteIndex = 0;
  }

  /**
   * Returns the x-coordinate for rendering the image.
   *
   * @return the x position
   */
  public double getOffsetX() {
    return offsetX;
  }

  /**
   * Sets the x-coordinate for rendering the image.
   *
   * @param x the new x position
   */
  public void setOffsetX(double x) {
    this.offsetX = x;
  }

  /**
   * Returns the y-coordinate for rendering the image.
   *
   * @return the y position
   */
  public double getOffsetY() {
    return offsetY;
  }

  /**
   * Sets the y-coordinate for rendering the image.
   *
   * @param y the new y position
   */
  public void setOffsetY(double y) {
    this.offsetY = y;
  }


  /**
   * @param imagePaths - List of all image paths for given object
   */
  public void setImagePaths(List<String> imagePaths) {
    this.imagePaths = imagePaths;
  }


  /**
   * Sets the index of the sprite to be rendered. If the index is out of bounds, it will be clamped
   */
  public void setSpriteIndex(int index) {
    if (index < 0) {
      currentSpriteIndex = 0;
    } else if (index >= imagePaths.size()) {
      currentSpriteIndex = imagePaths.size() - 1;
    } else {
      currentSpriteIndex = index;
    }
  }

  /**
   * Returns the path to the image file used for rendering.
   *
   * @return the image path
   */
  public String getImagePath() {
    return imagePaths.get(currentSpriteIndex);
  }
}
