package oogasalad.engine.component;

import oogasalad.engine.base.architecture.GameComponent;
import oogasalad.engine.base.enumerate.ComponentTag;
import oogasalad.engine.base.serialization.SerializableField;

/**
 * This component is for all text
 *
 * @author Jack F. Regan
 */
public class TextComponent extends GameComponent {

  @Override
  public ComponentTag componentTag() {
    return ComponentTag.NONE;
  }

  @SerializableField
  private String text;
  @SerializableField
  private double x;
  @SerializableField
  private double y;
  @SerializableField
  private double scaleX;
  @SerializableField
  private double scaleY;
  @SerializableField
  private double rotation;
  @SerializableField
  private String styleClass;

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
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

  public double getScaleX() {
    return scaleX;
  }

  public void setScaleX(double scaleX) {
    this.scaleX = scaleX;
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

  public String getStyleClass() {return styleClass;}

  public void setStyleClass(String styleClass) {this.styleClass = styleClass;}
}
