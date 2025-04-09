package oogasalad.view.gui.button;

import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;
import oogasalad.model.ResourceBundles;

/**
 * DefaultButton is a class that extends the Button class to provide
 * default styling and behavior for buttons in the application.
 */

public class DefaultButton extends Button {

  public static final String BUTTON_BUNDLE = "oogasalad.button.default";
  public static final String DEFAULT_BUTTON_FILL = ResourceBundles.getString(BUTTON_BUNDLE, "defaultFill");
  public static final int BUTTON_CLICK_COLOR_DURATION = ResourceBundles.getInt(BUTTON_BUNDLE, "clickColorDuration");  //(ms)
  public static final String BACKGROUND_COLOR_SETTER = "-fx-background-color: ";

  private String myIdleColor;

  /**
   * Constructor for DefaultButton
   */
  public DefaultButton() {
    super("");
    setToDefaults();
  }

  /**
   * Constructor for DefaultButton with text
   *
   * @param text the text to display on the button
   */
  public DefaultButton(String text) {
    super(text);
    setToDefaults();
  }

  /**
   * Set the width of the button
   * 
   * @param width the width to set
   */
  public void setWidth(double width) {
    this.setPrefWidth(width);
  }

  /**
   * Set the height of the button
   * 
   * @param height the height to set
   */
  public void setHeight(double height) {
    this.setPrefHeight(height);
  }

  /**
   * Set the color of the button when it is idle
   * 
   * @param color the color to set
   */
  public void setIdleColor(String color) {
    myIdleColor = color;
    this.setStyle(BACKGROUND_COLOR_SETTER + color);
    this.setOnMouseExited(e -> this.setStyle(BACKGROUND_COLOR_SETTER + color + ";"));
  }

  /**
   * Set the color of the button when it is hovered over
   * 
   * @param color the color to set
   */
  public void setHoverColor(String color) {
    // Change the button color to hoverColor when hovered over
    this.setOnMouseEntered(e -> this.setStyle(BACKGROUND_COLOR_SETTER + color + ";"));
  }

  /**
   * Set the color of the button when it is clicked
   * 
   * @param color the color to set
   */
  public void setClickColor(String color) {
    // Change the button color temporarily to clickColor when clicked
    this.setOnMousePressed(e -> this.setStyle("-fx-background-color:" + color + ";"));
    this.setOnMouseReleased(e -> {
      // Use a PauseTransition to make button color change temporarily
      PauseTransition pause = new PauseTransition(Duration.millis(BUTTON_CLICK_COLOR_DURATION));
      pause.setOnFinished(event -> this.setStyle("-fx-background-color:" + myIdleColor + ";"));
      pause.play();
    });
  }

  private void setToDefaults() {
    setIdleColor(DEFAULT_BUTTON_FILL);
    setHoverColor("Yellow");  // TODO: specify via resource file
    setClickColor("Blue");
  }
}
