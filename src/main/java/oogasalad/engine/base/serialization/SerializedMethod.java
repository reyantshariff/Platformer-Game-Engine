package oogasalad.engine.base.serialization;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The class that represents the serialized method from method annotated @SerializableMethod
 *
 * @author Hsuan-Kai Liao
 */
public class SerializedMethod {
  private final String methodName;
  private final Method method;
  private final Object targetObject;

  public SerializedMethod(Object targetObject, Method method) {
    this.targetObject = targetObject;
    this.methodName = method.getName();
    this.method = method;
    this.method.setAccessible(true);
  }

  /**
   * Get the name of the serializedMethod.
   */
  public String getMethodName() {
    return methodName;
  }

  /**
   * Get the type of the serializedMethod.
   */
  public Class<?> getReturnType() {
    return method.getReturnType();
  }

  /**
   * Invoke the method and return the result.
   */
  public Object invoke(Object... args) {
    try {
      return method.invoke(targetObject, args);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("Cannot invoke the method: " + methodName, e);
    }
  }

  @Override
  public String toString() {
    return "SerializedMethod{name='" + methodName + "'}";
  }
}
