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
}
