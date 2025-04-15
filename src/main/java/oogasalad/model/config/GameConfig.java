package oogasalad.model.config;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
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
  public static String getText(String key, Object... args) {
    try {
      String rawMessage = myMessages.getString(key);
      return MessageFormat.format(rawMessage, args);
    } catch (MissingResourceException e) {
      LOGGER.warn("Key does not exist for '{}'. Returning key.", key);
    }
    return key;
  }

  /**
   * A getter to use the language properties file message that have number values
   *
   * @param key - the message you want that is stored in the properties file
   * @return - the value of the message you wish to display/log
   */
  public static Double getNumber(String key) {
    try {
      String rawMessage = myMessages.getString(key);
      return Double.parseDouble(rawMessage);
    } catch (MissingResourceException e) {
      LOGGER.warn("Key does not exist for '{}'. Returning default.", key);
    } catch (NumberFormatException e) {
      LOGGER.warn("Key does not contain a number. Returning default.");
    }
    return 0.0;
  }

  /**
   * A getter to use the language properties file message that have list of string values
   *
   * @param key - the message you want that is stored in the properties file
   * @return - the value of the message you wish to display/log
   */
  public static List<String> getTextList(String key) {
    try {
      String rawMessage = myMessages.getString(key);
      return List.of(rawMessage.split(","));
    } catch (MissingResourceException e) {
      LOGGER.warn("Key does not exist for '{}'. Returning empty list.", key);
    }
    return List.of();
  }

}
