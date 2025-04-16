package oogasalad.view.config;

import java.util.Objects;
import javafx.scene.Scene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main configuration for stylesheets.
 *
 * @author Jack F. Regan
 */
public class StyleConfig {

  private static final Logger LOGGER = LogManager.getLogger();
  private static final String DEFAULT_STYLE = "light";
  private static final String STYLE_FILE_TYPE = ".css";
  private static final String STYLE_FILE_PREFIX = "/oogasalad/stylesheets/";

  /**
   * A method to switch the stylesheet to a new theme.
   *
   * @param theme = the new theme that is being swapped
   */
  public static void setStylesheet(Scene scene, String theme) {
    String stylesheet = STYLE_FILE_PREFIX + theme.toLowerCase() + STYLE_FILE_TYPE;
      scene.getStylesheets().clear();
      if(StyleConfig.class.getResource(stylesheet) == null || StyleConfig.class.getResource(stylesheet).toExternalForm() == null) {
        LOGGER.warn("Stylesheet file not found for '{}'. Switching to default.", theme);
        scene.getStylesheets().add(Objects.requireNonNull(StyleConfig.class.getResource(STYLE_FILE_PREFIX + DEFAULT_STYLE + STYLE_FILE_TYPE)).toExternalForm());
      } else {
        scene.getStylesheets().add(Objects.requireNonNull(StyleConfig.class.getResource(stylesheet)).toExternalForm());
      }
  }
}
