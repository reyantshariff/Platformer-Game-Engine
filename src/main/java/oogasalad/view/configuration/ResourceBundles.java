package oogasalad.view.configuration;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class ResourceBundles {

  private static final Map<String, ResourceBundle> bundles = new HashMap<>();

  /**
   * Loads a ResourceBundle with the given base name and default locale.
   *
   * @param baseName The base name of the resource bundle.
   */
  public static void loadBundle(String baseName) {
    loadBundle(baseName, Locale.getDefault());
  }

  /**
   * Loads a ResourceBundle with the given base name and locale.
   *
   * @param baseName The base name of the resource bundle.
   * @param locale   The locale for the resource bundle.
   */
  public static void loadBundle(String baseName, Locale locale) {
    try {
      ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
      bundles.put(generateKey(baseName, locale), bundle);
    } catch (java.util.MissingResourceException e) {
      System.err.println("Resource bundle not found: " + baseName + " for locale: " + locale);
    }
  }

  /**
   * Retrieves a string from the ResourceBundle with the given base name and key. Uses the default
   * locale.
   *
   * @param baseName The base name of the resource bundle.
   * @param key      The key of the string to retrieve.
   * @return The string value, or null if not found.
   */
  public static String getString(String baseName, String key) {
    return getString(baseName, key, Locale.getDefault());
  }

  /**
   * Retrieves a string from the ResourceBundle with the given base name, key, and locale.
   *
   * @param baseName The base name of the resource bundle.
   * @param key      The key of the string to retrieve.
   * @param locale   The locale for the resource bundle.
   * @return The string value, or null if not found.
   */
  public static String getString(String baseName, String key, Locale locale) {
    ResourceBundle bundle = bundles.get(generateKey(baseName, locale));
    if (bundle != null && bundle.containsKey(key)) {
      return bundle.getString(key);
    }
    return null;
  }

  /**
   * Retrieves an integer from the ResourceBundle with the given base name and key. Uses the default
   * locale.
   *
   * @param baseName The base name of the resource bundle.
   * @param key      The key of the integer to retrieve.
   * @return The integer value, or 0 if not found or invalid.
   */
  public static int getInt(String baseName, String key) {
    return getInt(baseName, key, Locale.getDefault());
  }

  /**
   * Retrieves an integer from the ResourceBundle with the given base name, key, and locale.
   *
   * @param baseName The base name of the resource bundle.
   * @param key      The key of the integer to retrieve.
   * @param locale   The locale for the resource bundle.
   * @return The integer value, or 0 if not found or invalid.
   */
  public static int getInt(String baseName, String key, Locale locale) {
    String value = getString(baseName, key, locale);
    if (value == null) {
      return 0;
    }
    try {
      return (int) cast(value, int.class);
    } catch (IllegalArgumentException e) {
      System.err.println(
          "Invalid integer value for key: " + key + " in bundle: " + baseName + " locale: "
              + locale);
      return 0;
    }
  }

  /**
   * Retrieves a double from the ResourceBundle with the given base name and key. Uses the default
   * locale.
   *
   * @param baseName The base name of the resource bundle.
   * @param key      The key of the double to retrieve.
   * @return The double value, or 0.0 if not found or invalid.
   */
  public static double getDouble(String baseName, String key) {
    return getDouble(baseName, key, Locale.getDefault());
  }

  /**
   * Retrieves a double from the ResourceBundle with the given base name, key, and locale.
   *
   * @param baseName The base name of the resource bundle.
   * @param key      The key of the double to retrieve.
   * @param locale   The locale for the resource bundle.
   * @return The double value, or 0.0 if not found or invalid.
   */
  public static double getDouble(String baseName, String key, Locale locale) {
    String value = getString(baseName, key, locale);
    if (value == null) {
      return 0.0;
    }
    try {
      return (double) cast(value, double.class);
    } catch (IllegalArgumentException e) {
      System.err.println(
          "Invalid double value for key: " + key + " in bundle: " + baseName + " locale: "
              + locale);
      return 0.0;
    }
  }

  /**
   * Retrieves a boolean from the ResourceBundle with the given base name and key. Uses the default
   * locale.
   *
   * @param baseName The base name of the resource bundle.
   * @param key      The key of the boolean to retrieve.
   * @return The boolean value, or false if not found or invalid.
   */
  public static boolean getBoolean(String baseName, String key) {
    return getBoolean(baseName, key, Locale.getDefault());
  }

  /**
   * Retrieves a boolean from the ResourceBundle with the given base name, key, and locale.
   *
   * @param baseName The base name of the resource bundle.
   * @param key      The key of the boolean to retrieve.
   * @param locale   The locale for the resource bundle.
   * @return The boolean value, or false if not found or invalid.
   */
  public static boolean getBoolean(String baseName, String key, Locale locale) {
    String value = getString(baseName, key, locale);
    if (value == null) {
      return false;
    }
    return (boolean) cast(value, boolean.class);
  }

  /**
   * Generates a unique key for storing ResourceBundles in the map.
   *
   * @param baseName The base name of the resource bundle.
   * @param locale   The locale for the resource bundle.
   * @return The generated key.
   */
  private static String generateKey(String baseName, Locale locale) {
    return baseName + "_" + locale.toString();
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
    if (type == Double.class || type == double.class) {
      return Double.parseDouble(value);
    }
    if (type == Integer.class || type == int.class) {
      return Integer.parseInt(value);
    }
    if (type == Boolean.class || type == boolean.class) {
      return Boolean.parseBoolean(value);
    }
    if (type == String.class) {
      return value;
    }
    throw new IllegalArgumentException("Unsupported type: " + type);
  }
}