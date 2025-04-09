package oogasalad.view.gui.button;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * ImageButton is a class that extends the StylizedButton class to provide buttons with images
 */

@Deprecated
public class ImageButton extends StylizedButton {

  public static final double IMAGE_BUTTON_MARGIN = 0.1;

  private Image myImage;

  /**
   * Constructor for ImageButton
   * 
   * @param image the image to display on the button
   */
  public ImageButton(Image image) {
    super("");
    setImage(image);
  }

  @Override
  public void setWidth(double width) {
    getButton().setPrefWidth(width);
    getButton().setGraphic(getImageViewFromImage(myImage)); // update image size based on button
                                                            // size
  }

  @Override
  public void setHeight(double height) {
    getButton().setPrefHeight(height);
    getButton().setGraphic(getImageViewFromImage(myImage)); // update image size based on button
                                                            // size
  }

  /**
   * Set the image for the button
   * 
   * @param image the image to set
   */
  public void setImage(Image image) {
    myImage = image;
    getButton().setGraphic(getImageViewFromImage(image));
  }

  /**
   * Get the image for the button
   * 
   * @return the image
   */
  public Image getImage() {
    return myImage;
  }

  private ImageView getImageViewFromImage(Image image) {
    ImageView imageView = new ImageView(image);

    // Calculate the scaling factors for both width and height
    double widthScale = getButton().getWidth() * (1 - IMAGE_BUTTON_MARGIN) / image.getWidth();
    double heightScale = getButton().getHeight() * (1 - IMAGE_BUTTON_MARGIN) / image.getHeight();

    // Scale image to fit in button based on the axis (width/height) requiring the lesser scale
    double scale = Math.min(widthScale, heightScale);

    // Set fit dimensions based on the chosen scale
    imageView.setFitWidth(image.getWidth() * scale);
    imageView.setFitHeight(image.getHeight() * scale);

    return imageView;
  }
}
