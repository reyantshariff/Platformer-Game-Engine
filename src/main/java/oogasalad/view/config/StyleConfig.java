package oogasalad.view.config;

import java.net.URL;
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
  private static String currentTheme = DEFAULT_STYLE;

  /**
   * A method to switch the stylesheet to a new theme.
   *
   * @param theme the new theme that is being swapped
   */
  public static void setStylesheet(Scene scene, String theme) {
    currentTheme = theme.toLowerCase();
    String path = STYLE_FILE_PREFIX + currentTheme + STYLE_FILE_TYPE;
    URL stylesheetURL = StyleConfig.class.getResource(path);

    if (stylesheetURL != null) {
      scene.getStylesheets().clear();
      scene.getStylesheets().add(stylesheetURL.toExternalForm());
    } else {
      LOGGER.warn("Stylesheet '{}' not found. Falling back to default.", path);
      URL defaultStylesheetURL = StyleConfig.class.getResource(STYLE_FILE_PREFIX + DEFAULT_STYLE + STYLE_FILE_TYPE);
      if (defaultStylesheetURL != null) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(defaultStylesheetURL.toExternalForm());
        currentTheme = DEFAULT_STYLE;
      } else {
        LOGGER.error("Default stylesheet also not found. No stylesheet applied.");
      }
    }
  }



  /**
   * @return - The current stylesheet string
   */
  public static String getCurrentTheme() {
    return currentTheme;
  }
}
