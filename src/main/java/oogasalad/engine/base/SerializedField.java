package oogasalad.engine.base;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * The class that represents the serialized field from field annotated @SerializableField
 * @param <T> the class type of the field
 */
public class SerializedField<T> {
    private final String fieldName;
    private final Method getter;
    private final Method setter;
    private final Field field;
    private final Object targetObject;

    public SerializedField(Object targetObject, Field field, Method getter, Method setter) {
        this.targetObject = targetObject;
        this.fieldName = field.getName();
        this.getter = getter;
        this.setter = setter;
        this.field = field;
        this.field.setAccessible(true);
    }

    public String getFieldName() {
        return fieldName;
    }

    public T getValue() throws Exception {
        if (getter != null) {
            return (T) getter.invoke(targetObject);
        }
        return (T) field.get(targetObject);
    }

    public void setValue(T value) throws Exception {
        if (setter != null) {
            setter.invoke(targetObject, value);
        } else {
            field.set(targetObject, value);
        }
    }

    @Override
    public String toString() {
        return "SerializableField{name='" + fieldName + "'}";
    }
}


