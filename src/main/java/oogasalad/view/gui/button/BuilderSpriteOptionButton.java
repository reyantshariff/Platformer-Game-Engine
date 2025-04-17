package oogasalad.view.gui.button;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import oogasalad.model.engine.base.architecture.GameObject;

/**
 * A button that represents a sprite option in the builder.
 */

public class BuilderSpriteOptionButton extends ImageButton {

  private GameObject prefab = null;

  /**
   * Constructor for BuilderSpriteOptionButton
   *
   * @param image  the image to be displayed on the button
   * @param width  the width of the button
   * @param height the height of the button
   * @param prefab the prefab associated with this button
   */
  public BuilderSpriteOptionButton(Image image, double width, double height, GameObject prefab) {
    super(image);
    applySizing(width, height);
    applyStyling();
    this.prefab = prefab;
  }

  /**
   * Get the prefab associated with this button
   *
   * @return the prefab associated with this button
   */
  public GameObject getPrefab() {
    return prefab;
  }

  private void applySizing(double width, double height) {
    // Set a large size for the button
    this.setPrefWidth(width);
    this.setPrefHeight(height);
  }

  /**
   * Styles and sizes the button according to specific values
   */
  private void applyStyling() {

    // Apply fun styling
    this.setStyle("-fx-background-color: linear-gradient(to bottom, #ffcc00, #ff9900);"
        + "-fx-background-radius: 15;"
        + "-fx-border-color: #ffaa00;"
        + "-fx-border-width: 3;"
        + "-fx-border-radius: 15;");

    // Add a glow effect on hover
    this.setOnMouseEntered(event -> {
      Glow glow = new Glow(0.5);
      this.setEffect(glow);
    });

    // Remove the glow effect when the mouse exits
    this.setOnMouseExited(event -> {
      this.setEffect(null);
    });

    // Add a drop shadow to give depth
    DropShadow dropShadow = new DropShadow();
    dropShadow.setColor(Color.ORANGE);
    dropShadow.setRadius(10);
    this.setEffect(dropShadow);
  }
}
