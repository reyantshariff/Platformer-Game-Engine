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

    while (clazz != null) {
      processClassFields(clazz, serializedFields);
      clazz = clazz.getSuperclass();
    }

    return serializedFields;
  }

  private void processClassFields(Class<?> clazz, List<SerializedField> list) {
    for (Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(SerializableField.class)) {
        SerializedField serializedField = createSerializedField(clazz, field);
        if (serializedField != null) {
          list.add(serializedField);
        }
      }
    }
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
      if (matchesFieldType(type, fieldGenericType)) {
        return type;
      }
    }

    // Handle unresolved TypeVariables after all types checked
    if (fieldGenericType instanceof TypeVariable<?> typeVar) {
      Type resolved = resolveTypeVariable(this.getClass(), typeVar);
      return resolved != null ? getValidType(resolved) : null;
    }

    return null;
  }

  private boolean matchesFieldType(SerializableFieldType type, Type fieldGenericType) {
    Type supportedType = type.getType();

    return isPrimitiveOrWrapperMatch(supportedType, fieldGenericType)
        || isParameterizedTypeMatch(supportedType, fieldGenericType);
  }

  private boolean isPrimitiveOrWrapperMatch(Type supportedType, Type fieldType) {
    if (supportedType instanceof Class<?> supportedClass &&
        fieldType instanceof Class<?> fieldClass) {
      return TypeRef.wrapperForPrimitive(supportedClass)
          .equals(TypeRef.wrapperForPrimitive(fieldClass));
    }
    return false;
  }

  private boolean isParameterizedTypeMatch(Type supportedType, Type fieldType) {
    if (!(supportedType instanceof ParameterizedType supportedParam) ||
        !(fieldType instanceof ParameterizedType fieldParam)) {
      return false;
    }

    return rawTypesMatch(supportedParam, fieldParam) &&
        typeArgumentsMatch(supportedParam.getActualTypeArguments(), fieldParam.getActualTypeArguments());
  }

  private boolean rawTypesMatch(ParameterizedType a, ParameterizedType b) {
    return a.getRawType().equals(b.getRawType());
  }

  private boolean typeArgumentsMatch(Type[] aArgs, Type[] bArgs) {
    if (aArgs.length != bArgs.length) return false;
    for (int i = 0; i < aArgs.length; i++) {
      if (!aArgs[i].equals(bArgs[i])) return false;
    }
    return true;
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
