package oogasalad.view.config;

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
  private static String stylesheet = STYLE_FILE_PREFIX + DEFAULT_STYLE + STYLE_FILE_TYPE;

  /**
   * Construct a new {@code StyleConfig} and set default stylesheet.
   *
   * @param scene - scene of the program
   */
  public StyleConfig(Scene scene) {
    setStylesheet(scene, DEFAULT_STYLE);
  }

  /**
   * A method to switch the stylesheet to a new theme.
   *
   * @param theme = the new theme that is being swapped
   */
  public static void setStylesheet(Scene scene, String theme) {
    try {
      stylesheet = STYLE_FILE_PREFIX + theme.toLowerCase() + STYLE_FILE_TYPE;
      scene.getStylesheets().add(StyleConfig.class.getResource(stylesheet).toExternalForm());
    } catch (NullPointerException e) {
      LOGGER.warn("Stylesheet file not found for '{}'. Switching to default.", theme);
      scene.getStylesheets().add(StyleConfig.class.getResource(STYLE_FILE_PREFIX + DEFAULT_STYLE + STYLE_FILE_TYPE).toExternalForm());
    }
  }
}
