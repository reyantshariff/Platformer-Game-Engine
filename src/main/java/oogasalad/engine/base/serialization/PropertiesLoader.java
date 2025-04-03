package oogasalad.engine.base.serialization;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {
  public static void loadFromFile(Serializable obj, String filePath) {
    Properties props = new Properties();
    try (FileInputStream fis = new FileInputStream(filePath)) {
      props.load(fis);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read properties file", e);
    }

    for (SerializedField<?> field : obj.getSerializedFields()) {
      String fieldName = field.getFieldName();
      if (props.containsKey(fieldName)) {
        try {
          String value = props.getProperty(fieldName);
          Class<?> type = field.getValue().getClass();
          Object casted = cast(value, type);
          setGenericField(field, casted);
        } catch (Exception e) {
          throw new RuntimeException("Failed to set field: " + fieldName, e);
        }
      }
    }
  }

  private static Object cast(String value, Class<?> type) {
    if (type == Double.class || type == double.class) return Double.parseDouble(value);
    if (type == Integer.class || type == int.class) return Integer.parseInt(value);
    if (type == Boolean.class || type == boolean.class) return Boolean.parseBoolean(value);
    if (type == String.class) return value;
    throw new IllegalArgumentException("Unsupported type: " + type);
  }

  @SuppressWarnings("unchecked")
  private static <T> void setGenericField(SerializedField<T> field, Object value) throws Exception {
    field.setValue((T) value);
  }

}
