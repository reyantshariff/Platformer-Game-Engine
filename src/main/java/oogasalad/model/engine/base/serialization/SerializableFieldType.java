package oogasalad.model.engine.base.serialization;

import java.lang.reflect.Type;
import java.util.List;
import oogasalad.model.engine.base.behavior.Behavior;
import oogasalad.model.engine.base.behavior.BehaviorAction;
import oogasalad.model.engine.base.behavior.BehaviorConstraint;
import oogasalad.model.engine.base.architecture.KeyCode;

/**
 * The enum that holds all the valid serializableType. This is used to defined parser and UI element.
 *
 * @author Hsuan-Kai Liao
 */
public enum SerializableFieldType {
  VOID(new TypeRef<Void>() {}),
  STRING(new TypeRef<String>() {}),
  INTEGER(new TypeRef<Integer>() {}),
  DOUBLE(new TypeRef<Double>() {}),
  BOOLEAN(new TypeRef<Boolean>() {}),
  KEYCODE(new TypeRef<KeyCode>() {}),
  LIST_STRING(new TypeRef<List<String>>() {}),
  LIST_BEHAVIOR(new TypeRef<List<Behavior>>() {}),
  LIST_BEHAVIOR_ACTION(new TypeRef<List<BehaviorAction<?>>>() {}),
  LIST_BEHAVIOR_CONSTRAINT(new TypeRef<List<BehaviorConstraint<?>>>() {});

  private final TypeRef<?> typeRef;

  /**
   * The constructor of the enum type. This requires a TypeRef<?> with the valid serializable field
   * types.
   * @param typeRef The typeRef anonymous class instance.
   */
  SerializableFieldType(TypeRef<?> typeRef) {
    this.typeRef = typeRef;
  }

  /**
   * Get the true type with the generic type inferred.
   *
   * @return the true Type.
   */
  public Type getType() {
    return typeRef.getType();
  }

  @Override
  public String toString() {
    return name() + "(" + typeRef + ")";
  }
}
