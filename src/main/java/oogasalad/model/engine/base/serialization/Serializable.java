package oogasalad.model.engine.base.serialization;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import oogasalad.model.config.GameConfig;
import oogasalad.model.engine.base.enumerate.SerializableFieldType;

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
          if (serializedField != null) {  // Ensure only valid fields are added
            serializedFields.add(serializedField);
          }
        }
      }
      clazz = clazz.getSuperclass(); // Move to the parent class
    }

    return serializedFields;
  }

  private SerializedField<?> createSerializedField(Class<?> clazz, Field field) {
    Type fieldGenericType = field.getGenericType();

    // Check if field's type matches supported types (including primitive/wrapper matching)
    if (!isIsValid(fieldGenericType)) {
      GameConfig.LOGGER.warn("Unsupported @SerializableField type: {} in {}#{}",
          (fieldGenericType instanceof TypeVariable<?> typeVar ? resolveTypeVariable(this.getClass(), typeVar).getTypeName() : fieldGenericType.getTypeName()),
          clazz.getSimpleName(),
          field.getName());

      return null;
    }

    // Construct getter and setter method names
    String fieldName = field.getName();
    String capitalized = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

    Method getter = null;
    Method setter = null;

    try {
      getter = clazz.getMethod("get" + capitalized);
    } catch (NoSuchMethodException ignored) {}

    try {
      setter = clazz.getMethod("set" + capitalized, field.getType());
    } catch (NoSuchMethodException ignored) {}

    return new SerializedField<>(this, field, getter, setter);
  }

  private boolean isIsValid(Type fieldGenericType) {
    for (SerializableFieldType type : SerializableFieldType.values()) {
      Type supportedType = type.getType();

      // Check if both types are the same, considering primitive and wrapper types
      if (supportedType instanceof Class<?> supportedClass && fieldGenericType instanceof Class<?> fieldClass) {
        if (wrapperForPrimitive(supportedClass).equals(wrapperForPrimitive(fieldClass))) {
          return true;
        }
      }

      // Handle parameterized types (e.g., List<String>)
      else if (supportedType instanceof ParameterizedType supportedParameterizedType && fieldGenericType instanceof ParameterizedType fieldParameterizedType) {
        if (supportedParameterizedType.getRawType().equals(fieldParameterizedType.getRawType())) {
          Type[] supportedArgs = supportedParameterizedType.getActualTypeArguments();
          Type[] fieldArgs = fieldParameterizedType.getActualTypeArguments();

          if (supportedArgs.length == fieldArgs.length) {
            boolean allMatch = true;
            for (int i = 0; i < supportedArgs.length; i++) {
              if (!supportedArgs[i].equals(fieldArgs[i])) {
                allMatch = false;
                break;
              }
            }
            if (allMatch) {
              return true;
            }
          }
        }
      }

      // Handle Generic Type
      else if (fieldGenericType instanceof TypeVariable<?> typeVar) {
        Type resolved = resolveTypeVariable(this.getClass(), typeVar);
        if (resolved != null && isIsValid(resolved)) {
          return true;
        }
      }
    }
    return false;
  }

  private static Class<?> wrapperForPrimitive(Class<?> primitiveClass) {
    if (primitiveClass == int.class) return Integer.class;
    if (primitiveClass == boolean.class) return Boolean.class;
    if (primitiveClass == char.class) return Character.class;
    if (primitiveClass == byte.class) return Byte.class;
    if (primitiveClass == short.class) return Short.class;
    if (primitiveClass == long.class) return Long.class;
    if (primitiveClass == float.class) return Float.class;
    if (primitiveClass == double.class) return Double.class;
    return primitiveClass;
  }

  private static Type resolveTypeVariable(Class<?> clazz, TypeVariable<?> typeVar) {
    Type superclass = clazz.getGenericSuperclass();

    if (superclass instanceof ParameterizedType pt) {
      TypeVariable<?>[] typeParams = ((Class<?>) pt.getRawType()).getTypeParameters();
      Type[] actualTypes = pt.getActualTypeArguments();

      for (int i = 0; i < typeParams.length; i++) {
        if (typeParams[i].getName().equals(typeVar.getName())) {
          return actualTypes[i];
        }
      }
    }

    return null;
  }
}
