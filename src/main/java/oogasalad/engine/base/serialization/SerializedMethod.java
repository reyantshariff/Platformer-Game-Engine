package oogasalad.engine.base.serialization;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

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
   * Get the types of the arguments this method takes.
   */
  public List<Class<?>> getArgumentTypes() {
    return List.of(method.getParameterTypes());
  }

  /**
   * Invoke the method and return the result.
   */
  public Object invoke(Object... args) {
    try {
      if (args.length != method.getParameterTypes().length) {
        throw new IllegalArgumentException("Argument count mismatch: Expected " + method.getParameterTypes().length + " but got " + args.length);
      }
      return method.invoke(targetObject, args);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("Cannot invoke the method: " + methodName, e);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("SerializedMethod{name='")
        .append(methodName)
        .append("', parameterTypes=[");
    for (Class<?> paramType : method.getParameterTypes()) {
      sb.append(paramType.getSimpleName()).append(", ");
    }
    if (method.getParameterTypes().length > 0) {
      sb.setLength(sb.length() - 2);
    }
    sb.append("]}");
    return sb.toString();
  }
}
