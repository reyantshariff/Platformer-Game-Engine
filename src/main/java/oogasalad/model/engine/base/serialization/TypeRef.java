package oogasalad.model.engine.base.serialization;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * This is the class that hold a reference for the actual type of class.
 * @param <T> the class specified
 * @author Hsuan-Kai Liao
 */
public abstract class TypeRef<T> {
  private final Type type;

  protected TypeRef() {
    Type superClass = getClass().getGenericSuperclass();
    if (superClass instanceof ParameterizedType) {
      this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    } else {
      throw new TypeReferenceException("Invalid TypeReference construction");
    }
  }

  /**
   * Get the true type of T.
   */
  public Type getType() {
    return type;
  }

  /**
   * Change the primitive class into its wrapper class.
   * @param primitiveClass the input primitive class
   * @return the wrapperType if the input is a primitive class; else return itself.
   */
  public static Class<?> wrapperForPrimitive(Class<?> primitiveClass) {
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
}
