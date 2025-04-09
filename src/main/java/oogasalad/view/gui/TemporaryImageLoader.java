package oogasalad.view.gui;

import javafx.scene.image.Image;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * TEMPORARY CLASS for loading sprites into the game builder by their image
 */
public class TemporaryImageLoader {

  public static List<Image> loadImages(String directoryPath) {
    List<Image> images = new ArrayList<>();
    File directory = new File(directoryPath);

    // Ensure the directory exists and contains files
    if (directory.exists() && directory.isDirectory()) {
      for (File file : directory.listFiles()) {
        try {
          // Convert file path to a URL and load it as a JavaFX Image
          String imageUrl = file.toURI().toURL().toString();
          images.add(new Image(imageUrl));
        } catch (MalformedURLException e) {
          System.err.println("Invalid file URL: " + file.getName());
        }
      }
    } else {
      System.err.println("Directory does not exist: " + directoryPath);
    }
    return images;
  }
}

