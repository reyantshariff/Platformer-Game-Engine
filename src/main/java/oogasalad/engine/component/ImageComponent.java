package oogasalad.engine.component;

import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.serialization.SerializableField;

public class ImageComponent extends GameComponent {
  @SerializableField
  private String imagePath;
  @SerializableField
  private double x;
  @SerializableField
  private double y;

  /**
   * This is the actual updating order of the component.
   *
   * @return the specified component tag
   */
  @Override
  public ComponentTag componentTag() {
    return ComponentTag.RENDER;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  @Override
  public void update(double deltaTime) {
    // Image components typically don't update over time.
  }
}