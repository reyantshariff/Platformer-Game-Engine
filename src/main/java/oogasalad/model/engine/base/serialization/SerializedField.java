package oogasalad.model.engine.base.serialization;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * The class that represents the serialized field from field annotated @SerializableField
 *
 * @author Hsuan-Kai Liao
 */
public class SerializedField<T> {

  private final String fieldName;
  private final Method getter;
  private final Method setter;
  private final Field field;
  private final Object targetObject;

  /**
   * Constructor for a serialized field
   *
   * @param targetObject - Object for field
   * @param field        - Field being assigned
   * @param getter       - Getter for field
   * @param setter       - Setter for field
   */
  public SerializedField(Object targetObject, Field field, Method getter, Method setter) {
    this.targetObject = targetObject;
    this.fieldName = field.getName();
    this.getter = getter;
    this.setter = setter;
    this.field = field;
    this.field.setAccessible(true);
  }

  /**
   * Get the name of the serializedField.
   */
  public String getFieldName() {
    return fieldName;
  }

  /**
   * Get the type of the serializedField.
   */
  public Class<?> getFieldType() {
    return field.getType();
  }

  /**
   * Get the generic type of the serializedField.
   */
  public Type getFieldGenericType() {
    return field.getGenericType();
  }

  /**
   * Get the value of the serializedField.
   */
  @SuppressWarnings("unchecked")
  public T getValue() {
    if (getter != null) {
      try {
        return (T) getter.invoke(targetObject);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException("Cannot get the value to SerializedField: " + fieldName);
      }
    }
    try {
      return (T) field.get(targetObject);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Cannot get the value to SerializedField: " + fieldName);
    }
  }

  /**
   * Set the value of the serializedField. This is automatically casted to the type of the field.
   *
   * @param value - Value to set
   */
  public void setValue(T value) {
    if (setter != null) {
      try {
        setter.invoke(targetObject, value);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException("Cannot set the value to SerializedField: " + fieldName);
      }
    } else {
      try {
        field.set(targetObject, value);
      } catch (IllegalAccessException e) {
        throw new RuntimeException("Cannot set the value to SerializedField: " + fieldName);
      }
    }
  }

  @Override
  public String toString() {
    return "SerializableField{name='" + fieldName + "'}";
  }
}


