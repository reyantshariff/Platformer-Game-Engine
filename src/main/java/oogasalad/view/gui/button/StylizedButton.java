package oogasalad.view.gui.button;

import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

/**
 * StylizedButton is a class that extends the Button class to provide
 * default styling and behavior for buttons in the application.
 */

@Deprecated
public class StylizedButton {

  public static final String DEFAULT_BUTTON_FILL = "WHITE"; // default base color
  public static final int BUTTON_CLICK_COLOR_DURATION = 100; // default color change duration (ms)
  private static final String BACKGROUND_COLOR_SETTER = "-fx-background-color: ";

  private String myIdleColor;
  private String myHoverColor;
  private Button myButton;
  private boolean mouseHover;

  /**
   * Constructor for StylizedButton
   */
  public StylizedButton() {
    myButton = new Button("");
    setToDefaults();
  }

  /**
   * Constructor for StylizedButton with text
   *
   * @param text the text to display on the button
   */
  public StylizedButton(String text) {
    myButton = new Button(text);
    setToDefaults();
  }

  /**
   * Set the width of the button
   *
   * @param width the width to set
   */
  public void setWidth(double width) {
    myButton.setPrefWidth(width);
  }

  /**
   * Set the height of the button
   *
   * @param height the height to set
   */
  public void setHeight(double height) {
    myButton.setPrefHeight(height);
  }

  /**
   * Get the button
   * 
   * @return the button
   */
  public Button getButton() {
    return myButton;
  }

  /**
   * Set the color of the button when it is idle
   *
   * @param color the color to set
   */
  public void setIdleColor(String color) {
    myIdleColor = color;
    myButton.setStyle(BACKGROUND_COLOR_SETTER + color);
    myButton.setOnMouseExited(e -> {
      myButton.setStyle(BACKGROUND_COLOR_SETTER + color + ";");
      mouseHover = false;
    });
  }

  /**
   * Set the color of the button when it is hovered over
   *
   * @param color the color to set
   */
  public void setHoverColor(String color) {
    myHoverColor = color;
    // Change the button color to hoverColor when hovered over
    myButton.setOnMouseEntered(e -> {
      myButton.setStyle(BACKGROUND_COLOR_SETTER + color + ";");
      mouseHover = true;
    });
  }

  /**
   * Set the color of the button when it is clicked
   *
   * @param color the color to set
   */
  public void setClickColor(String color) {
    // Change the button color temporarily to clickColor when clicked
    myButton.setOnMousePressed(e -> myButton.setStyle(BACKGROUND_COLOR_SETTER + color + ";"));
    myButton.setOnMouseReleased(e -> {
      // Use a PauseTransition to make button color change temporarily
      PauseTransition pause = new PauseTransition(Duration.millis(BUTTON_CLICK_COLOR_DURATION));
      pause.setOnFinished(event -> {
        if (mouseHover) {
          myButton.setStyle(BACKGROUND_COLOR_SETTER + myHoverColor + ";");
        } else {
          myButton.setStyle(BACKGROUND_COLOR_SETTER + myIdleColor + ";");
        }
      });
      pause.play();
    });
  }

  private void setToDefaults() {
    setIdleColor(DEFAULT_BUTTON_FILL);
    setHoverColor("Yellow"); // TODO: specify via resource file
    setClickColor("Blue");
    mouseHover = false;
  }
}
