package oogasalad.view.gui.button;

import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

@Deprecated
public class StylizedButton {

  public static final String DEFAULT_BUTTON_FILL = "WHITE"; // default base color
  public static final int BUTTON_CLICK_COLOR_DURATION = 100; // default color change duration (ms)

  private String myIdleColor;
  private String myHoverColor;
  private Button myButton;
  private boolean mouseHover;

  public StylizedButton() {
    myButton = new Button("");
    setToDefaults();
  }

  public StylizedButton(String text) {
    myButton = new Button(text);
    setToDefaults();
  }

  public void setWidth(double width) {
    myButton.setPrefWidth(width);
  }

  public void setHeight(double height) {
    myButton.setPrefHeight(height);
  }

  public Button getButton() {
    return myButton;
  }

  public void setIdleColor(String color) {
    myIdleColor = color;
    myButton.setStyle("-fx-background-color: " + color);
    myButton.setOnMouseExited(e -> {
      myButton.setStyle("-fx-background-color: " + color + ";");
      mouseHover = false;
    });
  }

  public void setHoverColor(String color) {
    myHoverColor = color;
    // Change the button color to hoverColor when hovered over
    myButton.setOnMouseEntered(e -> {
      myButton.setStyle("-fx-background-color:" + color + ";");
      mouseHover = true;
    });
  }

  public void setClickColor(String color) {
    // Change the button color temporarily to clickColor when clicked
    myButton.setOnMousePressed(e -> myButton.setStyle("-fx-background-color:" + color + ";"));
    myButton.setOnMouseReleased(e -> {
      // Use a PauseTransition to make button color change temporarily
      PauseTransition pause = new PauseTransition(Duration.millis(BUTTON_CLICK_COLOR_DURATION));
      pause.setOnFinished(event -> {
        if (mouseHover) {
          myButton.setStyle("-fx-background-color:" + myHoverColor + ";");
        } else {
          myButton.setStyle("-fx-background-color:" + myIdleColor + ";");
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
