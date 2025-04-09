package oogasalad.view.gui.button;

import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class DefaultButton extends Button {

  public static final String DEFAULT_BUTTON_FILL = "#FFFFFF"; // default base color
  public static final int BUTTON_CLICK_COLOR_DURATION = 100;  // default color change duration (ms)

  private String myIdleColor;

  public DefaultButton() {
    super("");
    setToDefaults();
  }

  public DefaultButton(String text) {
    super(text);
    setToDefaults();
  }

  public void setWidth(double width) {
    this.setPrefWidth(width);
  }

  public void setHeight(double height) {
    this.setPrefHeight(height);
  }

  public void setIdleColor(String color) {
    myIdleColor = color;
    this.setStyle("-fx-background-color: " + color);
    this.setOnMouseExited(e -> this.setStyle("-fx-background-color: " + color + ";"));
  }

  public void setHoverColor(String color) {
    // Change the button color to hoverColor when hovered over
    this.setOnMouseEntered(e -> this.setStyle("-fx-background-color:" + color + ";"));
  }

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
