package oogasalad.engine.base.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation that tells the class it belongs to to use reflection to read and write this field.
 * Its parent class should always implement the Serializable interface.
 *
 * @author Hsuan-Kai Liao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SerializableMethod {}
