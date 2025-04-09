package oogasalad.model.engine.component;

import oogasalad.model.engine.base.architecture.GameComponent;
import oogasalad.model.engine.base.enumerate.ComponentTag;
import oogasalad.model.engine.base.serialization.SerializableField;

/**
 * This component is for all text
 *
 * @author Jack F. Regan
 */
public class Text extends GameComponent {

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

  /**
   * Constructor for Text. Sets default values for the text, x, y, scaleX, scaleY, rotation, and
   * styleClass.
   */
  public Text() {
    super();
    this.text = "";
    this.x = 0;
    this.y = 0;
    this.scaleX = 1;
    this.scaleY = 1;
    this.rotation = 0;
    this.styleClass = "";
  }

  /**
   * get the text of the text component
   * @return the text of the text component
   */
  public String getText() {
    return text;
  }

  /**
   * set the text of the text component
   * @param text the text to set
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * get the x position of the text component
   * @return the x position of the text component
   */
  public double getX() {
    return x;
  }

  /**
   * set the x position of the text component
   * @param x the x position to set
   */
  public void setX(double x) {
    this.x = x;
  }

  /**
   * get the y position of the text component
   * @return the y position of the text component
   */
  public double getY() {
    return y;
  }

  /**
   * set the y position of the text component
   * @param y the y position to set
   */
  public void setY(double y) {
    this.y = y;
  }

  /**
   * get the scaleX of the text component
   * @return the scaleX of the text component
   */
  public double getScaleX() {
    return scaleX;
  }

  /**
   * set the scaleX of the text component
   * @param scaleX the scaleX to set
   */
  public void setScaleX(double scaleX) {
    this.scaleX = scaleX;
  }

  /**
   * get the scaleY of the text component
   * @return the scaleY of the text component
   */
  public double getScaleY() {
    return scaleY;
  }

  /**
   * set the scaleY of the text component
   * @param scaleY the scaleY to set
   */
  public void setScaleY(double scaleY) {
    this.scaleY = scaleY;
  }

  /**
   * get the rotation of the text component
   * @return the rotation of the text component
   */
  public double getRotation() {
    return rotation;
  }

  /**
   * set the rotation of the text component
   * @param rotation the rotation to set
   */
  public void setRotation(double rotation) {
    this.rotation = rotation;
  }

  /**
   * get the styleClass of the text component
   * @return the styleClass of the text component
   */
  public String getStyleClass() {
    return styleClass;
  }

  /**
   * set the styleClass of the text component
   * @param styleClass the styleClass to set
   */
  public void setStyleClass(String styleClass) {
    this.styleClass = styleClass;
  }
}
