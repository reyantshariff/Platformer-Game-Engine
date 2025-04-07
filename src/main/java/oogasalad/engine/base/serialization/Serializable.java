package oogasalad.engine.base.serialization;

import java.util.List;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * This is the interface indicating that its field can be annotated as @SerializableField. And all the fields
 * annotated @SerializableField will be added to this SerializedField list.
 *
 * @author Hsuan-Kai Liao
 */
public interface Serializable {
    default List<SerializedField> getSerializedFields() {
        List<SerializedField> serializedFields = new ArrayList<>();
        Class<?> clazz = this.getClass();

        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(SerializableField.class)) {
                    String fieldName = field.getName();
                    String capitalized = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

                    Method getter = null;
                    Method setter = null;

                    try {
                        getter = this.getClass().getMethod("get" + capitalized);
                    } catch (NoSuchMethodException ignored) {}

                    try {
                        setter = this.getClass().getMethod("set" + capitalized, field.getType());
                    } catch (NoSuchMethodException ignored) {}

                    field.setAccessible(true); // In case it's private
                    serializedFields.add(new SerializedField(this, field, getter, setter));
                }
            }

            clazz = clazz.getSuperclass(); // Move up the hierarchy
        }

        return serializedFields;
    }

}

