package oogasalad.model.engine.base.serialization;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import oogasalad.model.config.GameConfig;

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
  default List<SerializedField> getSerializedFields() {
    List<SerializedField> serializedFields = new ArrayList<>();
    Class<?> clazz = this.getClass();

    // Traverse class hierarchy to include parent classes
    while (clazz != null) {
      for (Field field : clazz.getDeclaredFields()) {
        if (field.isAnnotationPresent(SerializableField.class)) {
          SerializedField serializedField = createSerializedField(clazz, field);
          if (serializedField != null) {  // Ensure only valid fields are added
            serializedFields.add(serializedField);
          }
        }
      }
      clazz = clazz.getSuperclass(); // Move to the parent class
    }

    return serializedFields;
  }

  private SerializedField createSerializedField(Class<?> clazz, Field field) {
    Type fieldGenericType = field.getGenericType();

    // Check if field's type matches supported types (including primitive/wrapper matching)
    SerializableFieldType fieldType = getValidType(fieldGenericType);
    if (fieldType == null) {
      GameConfig.LOGGER.warn("Unsupported @SerializableField type: {} in {}#{}",
          (fieldGenericType instanceof TypeVariable<?> typeVar ? Objects.requireNonNull(resolveTypeVariable(this.getClass(), typeVar)).getTypeName() : fieldGenericType.getTypeName()),
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

    return new SerializedField(this, field, fieldType, getter, setter);
  }

  private SerializableFieldType getValidType(Type fieldGenericType) {
    for (SerializableFieldType type : SerializableFieldType.values()) {
      Type supportedType = type.getType();

      // Check if both types are the same, considering primitive and wrapper types
      if (supportedType instanceof Class<?> supportedClass
          && fieldGenericType instanceof Class<?> fieldClass) {
        if (TypeRef.wrapperForPrimitive(supportedClass).equals(TypeRef.wrapperForPrimitive(fieldClass))) {
          return type;
        }
      }

      // Handle parameterized types (e.g., List<String>)
      else if (supportedType instanceof ParameterizedType supportedParameterizedType
          && fieldGenericType instanceof ParameterizedType fieldParameterizedType) {
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
              return type;
            }
          }
        }
      }

      // Handle Generic Type
      else if (fieldGenericType instanceof TypeVariable<?> typeVar) {
        Type resolved = resolveTypeVariable(this.getClass(), typeVar);
        if (resolved != null && getValidType(resolved) != null) {
          return type;
        }
      }
    }
    return null;
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
