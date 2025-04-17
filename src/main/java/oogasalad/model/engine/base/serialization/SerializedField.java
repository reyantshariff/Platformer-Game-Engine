package oogasalad.model.engine.base.serialization;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The class that represents the serialized field from field annotated @SerializableField
 *
 * @author Hsuan-Kai Liao
 */
public class SerializedField {

  private final String fieldName;
  private final Method getter;
  private final Method setter;
  private final Field field;
  private final SerializableFieldType serializedFieldType;
  private final Object targetObject;

  /**
   * Constructor for a serialized field
   *
   * @param targetObject - Object for field
   * @param field        - Field being assigned
   * @param getter       - Getter for field
   * @param setter       - Setter for field
   */
  public SerializedField(Object targetObject, Field field, SerializableFieldType type, Method getter, Method setter) {
    this.targetObject = targetObject;
    this.fieldName = field.getName();
    this.getter = getter;
    this.setter = setter;
    this.serializedFieldType = type;
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
   * Get the Serializable FieldType of the serializedField.
   */
  public SerializableFieldType getFieldType() {
    return serializedFieldType;
  }

  /**
   * Get the value of the serializedField.
   */
  public Object getValue() {
    if (getter != null) {
      try {
        return getter.invoke(targetObject);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new GetSerializedFieldException("Cannot get the value to SerializedField: " + fieldName, e);
      }
    }
    try {
      return field.get(targetObject);
    } catch (IllegalAccessException e) {
      throw new GetSerializedFieldException("Cannot get the value to SerializedField: " + fieldName, e);
    }
  }

  /**
   * Set the value of the serializedField.
   *
   * @param value - Value to set
   */
  public void setValue(Object value) {
    if (setter != null) {
      try {
        setter.invoke(targetObject, value);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new SetSerializedFieldException("Cannot set the value to SerializedField: " + fieldName, e);
      }
    } else {
      try {
        field.set(targetObject, value);
      } catch (IllegalAccessException e) {
        throw new SetSerializedFieldException("Cannot set the value to SerializedField: " + fieldName, e);
      }
    }
  }

  @Override
  public String toString() {
    return "SerializableField{name='" + fieldName + "'}";
  }
}


