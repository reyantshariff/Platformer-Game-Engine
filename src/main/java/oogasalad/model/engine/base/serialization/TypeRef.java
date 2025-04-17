package oogasalad.model.engine.base.serialization;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the class that hold a reference for the actual type of class.
 * @param <T> the class specified
 * @author Hsuan-Kai Liao
 */
public abstract class TypeRef<T> {
  private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER;
  static {
    Map<Class<?>, Class<?>> map = new HashMap<>();
    map.put(int.class, Integer.class);
    map.put(boolean.class, Boolean.class);
    map.put(char.class, Character.class);
    map.put(byte.class, Byte.class);
    map.put(short.class, Short.class);
    map.put(long.class, Long.class);
    map.put(float.class, Float.class);
    map.put(double.class, Double.class);
    PRIMITIVE_TO_WRAPPER = Collections.unmodifiableMap(map);
  }

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
   * @return the wrapper type if input is primitive; else return itself.
   */
  public static Class<?> wrapperForPrimitive(Class<?> primitiveClass) {
    return PRIMITIVE_TO_WRAPPER.getOrDefault(primitiveClass, primitiveClass);
  }
}
