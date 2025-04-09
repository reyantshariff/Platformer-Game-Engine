package oogasalad.model.config;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main configurations for the Game and program
 *
 * @author Justin Aronwald
 */
public class GameConfig {

  public static final Logger LOGGER = LogManager.getLogger(); // The logger for this program
  private static final String DEFAULT_LANGUAGE = "English";
  private static final String LANGUAGE_FILE_PREFIX = "oogasalad.languages.";
  private static ResourceBundle myMessages =
      ResourceBundle.getBundle(LANGUAGE_FILE_PREFIX + DEFAULT_LANGUAGE);


  /**
   * A method to switch the language to a new language
   *
   * @param language - the new language that is being swapped
   */
  public static void setLanguage(String language) {
    try {
      myMessages = ResourceBundle.getBundle(LANGUAGE_FILE_PREFIX + language.toLowerCase());
    } catch (MissingResourceException e) {
      LOGGER.warn("Language file not found for '{}'. Switching to default.", language);
      myMessages = ResourceBundle.getBundle(LANGUAGE_FILE_PREFIX + DEFAULT_LANGUAGE);
    }
  }

  /**
   * A getter to use the language properties file message
   *
   * @param key - the message you want that is stored in the properties file
   * @return - the value of the message you wish to display/log
   */
  public static String getText(String key) {
    return myMessages.getString(key);
  }
}
