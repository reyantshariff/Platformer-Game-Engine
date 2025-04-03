package oogasalad.gui.button;

import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class DefaultButton {
  public static final String DEFAULT_BUTTON_FILL = "#FFFFFF"; // default base color
  public static final int BUTTON_CLICK_COLOR_DURATION = 100;  // default color change duration (ms)

  protected final Button myButton;
  private String myIdleColor;

  public DefaultButton() {
    myButton = new Button("");
    setToDefaults();
  }

  public DefaultButton(String text) {
    myButton = new Button(text);
    setToDefaults();
  }

  public void setWidth(double width) {
    myButton.setPrefWidth(width);
  }

  public void setHeight(double height) {
    myButton.setPrefHeight(height);
  }

  public void setIdleColor(String color) {
    myIdleColor = color;
    myButton.setStyle("-fx-background-color: " + color);
    myButton.setOnMouseExited(e -> myButton.setStyle("-fx-background-color: " + color + ";"));
  }

  public void setHoverColor(String color) {
    // Change the button color to hoverColor when hovered over
    myButton.setOnMouseEntered(e -> myButton.setStyle("-fx-background-color:" + color + ";"));
  }

  public void setClickColor(String color) {
    // Change the button color temporarily to clickColor when clicked
    myButton.setOnMousePressed(e -> myButton.setStyle("-fx-background-color:" + color + ";"));
    myButton.setOnMouseReleased(e -> {
      // Use a PauseTransition to make button color change temporarily
      PauseTransition pause = new PauseTransition(Duration.millis(BUTTON_CLICK_COLOR_DURATION));
      pause.setOnFinished(event -> myButton.setStyle("-fx-background-color:" + myIdleColor + ";"));
      pause.play();
    });
  }

  private void setToDefaults() {
    setIdleColor(DEFAULT_BUTTON_FILL);
    setHoverColor("Yellow");  // TODO: specify via resource file
    setClickColor("Blue");
  }
}
