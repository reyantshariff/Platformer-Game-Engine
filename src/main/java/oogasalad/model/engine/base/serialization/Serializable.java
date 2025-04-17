package oogasalad.model.engine.base.serialization;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the interface indicating that its field can be annotated as @SerializableField. And all
 * the fields annotated @SerializableField will be added to this SerializedField list.
 *
 * @author Hsuan-Kai Liao
 */
public interface Serializable {

  /**
   * Get all the field annotated @SerializableField, including fields from parent classes.
   */
  default List<SerializedField<?>> getSerializedFields() {
    List<SerializedField<?>> serializedFields = new ArrayList<>();
    Class<?> clazz = this.getClass();

    // Traverse class hierarchy to include parent classes
    while (clazz != null) {
      for (Field field : clazz.getDeclaredFields()) {
        if (field.isAnnotationPresent(SerializableField.class)) {
          SerializedField<?> serializedField = createSerializedField(clazz, field);
          serializedFields.add(serializedField);
        }
      }
      clazz = clazz.getSuperclass(); // Move to the parent class
    }

    return serializedFields;
  }

  /**
   * Get the field annotated @SerializableField in the parent class.
   */
  default SerializedField<?> getParentSerializableField() {
    Class<?> superClass = this.getClass().getSuperclass();

    while (superClass != null) {
      for (Field field : superClass.getDeclaredFields()) {
        if (field.isAnnotationPresent(SerializableField.class)) {
          field.setAccessible(true);
          return createSerializedField(superClass, field);
        }
      }
      superClass = superClass.getSuperclass();
    }

    throw new IllegalStateException(
        "No @SerializableField found in parent class for: " + this.getClass().getSimpleName());
  }

  private SerializedField<?> createSerializedField(Class<?> clazz, Field field) {
    String fieldName = field.getName();
    String capitalized = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

    Method getter = null;
    Method setter = null;

    try {
      getter = clazz.getMethod("get" + capitalized);
    } catch (NoSuchMethodException ignored) {
    }

    try {
      setter = clazz.getMethod("set" + capitalized, field.getType());
    } catch (NoSuchMethodException ignored) {
    }

    return new SerializedField<>(this, field, getter, setter);
  }
}
