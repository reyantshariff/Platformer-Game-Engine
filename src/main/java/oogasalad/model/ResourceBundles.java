package oogasalad.model;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.text.MessageFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import oogasalad.model.config.GameConfig;
/**
 * The ResourceBundles class reads and casts the value pairings of properties files.
 *
 * @author Jack F. Regan
 */
public class ResourceBundles {

  private static final Map<String, ResourceBundle> bundles = new HashMap<>();
  private static final Logger logger = LogManager.getLogger(ResourceBundles.class);
  private static String activeBundleBaseName = null;

  private static final Map<Class<?>, Function<String, Object>> typeParsers = new HashMap<>();

  static {
    typeParsers.put(Double.class, Double::parseDouble);
    typeParsers.put(double.class, Double::parseDouble);
    typeParsers.put(Integer.class, Integer::parseInt);
    typeParsers.put(int.class, Integer::parseInt);
    typeParsers.put(Boolean.class, Boolean::parseBoolean);
    typeParsers.put(boolean.class, Boolean::parseBoolean);
    typeParsers.put(String.class, s -> s);
  }

  /**
   * Loads a ResourceBundle with the given base name.
   *
   * @param baseName The base name of the resource bundle.
   */
  public static void loadBundle(String baseName) {
    try {
      ResourceBundle bundle = ResourceBundle.getBundle(baseName);
      bundles.put(baseName, bundle);
    } catch (MissingResourceException e) {
      logger.error(MessageFormat.format(GameConfig.getText("bundleNotFound"), baseName));
    }
  }

  /**
   * Defines which style bundle should be treated as the active/current bundle. Loads the
   * ResourceBundle with the given baseName if it does not already exist in the map.
   *
   * @param baseName The base name of the resource bundle.
   */
  public static void setActiveBundle(String baseName) {
    try {
      if (!bundles.containsKey(baseName)) {
        loadBundle(baseName);
      }
      activeBundleBaseName = baseName;
    } catch (MissingResourceException e) {
      logger.error(MessageFormat.format(GameConfig.getText("bundleSetFailed"), baseName));
    }
  }

  /**
   * Retrieves a string from the ResourceBundle with the given base name and key.
   *
   * @param baseName The base name of the resource bundle.
   * @param key      The key of the string to retrieve.
   * @return The string value, or null if not found.
   */
  public static String getString(String baseName, String key) {
    ResourceBundle bundle = bundles.get(baseName);
    if (bundle != null && bundle.containsKey(key)) {
      return bundle.getString(key);
    }
    return null;
  }

  /**
   * Retrieves a string from the currently "active" ResourceBundle with the given key.
   *
   * @param key The key of the string to retrieve.
   * @return The string value, or null if not found.
   */
  public static String getString(String key) {
    if (activeBundleBaseName == null) {
      logger.error(GameConfig.getText("setBundle"));
    }
    return getString(activeBundleBaseName, key);
  }

  /**
   * Retrieves an integer from the ResourceBundle with the given base name and key.
   *
   * @param baseName The base name of the resource bundle.
   * @param key      The key of the integer to retrieve.
   * @return The integer value, or 0 if not found or invalid.
   */
  public static int getInt(String baseName, String key) {
    String value = getString(baseName, key);
    if (value == null) {
      return 0;
    }
    try {
      return (int) cast(value, int.class);
    } catch (IllegalArgumentException e) {
      logger.error(MessageFormat.format(GameConfig.getText("invalidKey"), key, baseName));
      return 0;
    }
  }

  /**
   * Retrieves an integer from the currently "active" ResourceBundle with the given key.
   *
   * @param key The key of the integer to retrieve.
   * @return The integer value, or 0 if not found or invalid.
   */
  public static int getInt(String key) {
    if (activeBundleBaseName == null) {
      logger.error(GameConfig.getText("setBundle"));
    }
    return getInt(activeBundleBaseName, key);
  }

  /**
   * Retrieves a double from the ResourceBundle with the given base name and key.
   *
   * @param baseName The base name of the resource bundle.
   * @param key      The key of the double to retrieve.
   * @return The double value, or 0.0 if not found or invalid.
   */
  public static double getDouble(String baseName, String key) {
    String value = getString(baseName, key);
    if (value == null) {
      return 0.0;
    }
    try {
      return (double) cast(value, double.class);
    } catch (IllegalArgumentException e) {
      logger.error(MessageFormat.format(GameConfig.getText("invalidKey"), key, baseName));
      return 0.0;
    }
  }

  /**
   * Retrieves a double from the currently "active" ResourceBundle with the given key.
   *
   * @param key The key of the double to retrieve.
   * @return The double value, or 0.0 if not found or invalid.
   */
  public static double getDouble(String key) {
    if (activeBundleBaseName == null) {
      logger.error(GameConfig.getText("setBundle"));
    }
    return getDouble(activeBundleBaseName, key);
  }

  /**
   * Retrieves a boolean from the ResourceBundle with the given base name and key.
   *
   * @param baseName The base name of the resource bundle.
   * @param key      The key of the boolean to retrieve.
   * @return The boolean value, or false if not found or invalid.
   */
  public static boolean getBoolean(String baseName, String key) {
    String value = getString(baseName, key);
    if (value == null) {
      return false;
    }
    return (boolean) cast(value, boolean.class);
  }

  /**
   * Casts a string value to the given type.
   *
   * @param value The string value to cast.
   * @param type  The type to cast to.
   * @return The casted value.
   * @throws IllegalArgumentException if the type is not supported.
   */
  private static Object cast(String value, Class<?> type) {
    Function<String, Object> parser = typeParsers.get(type);
    if (parser == null) {
      throw new IllegalArgumentException(
          MessageFormat.format(GameConfig.getText("unsupportedType"), type.getName()));
    }
    return parser.apply(value);
  }
}