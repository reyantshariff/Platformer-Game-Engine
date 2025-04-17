package oogasalad.model.engine.base.enumerate;

/**
 * The type that specifies the order of the update of the component. The more the Component is in
 * the front, the earlier it gets updated.
 *
 * @author Hsuan-Kai Liao
 */
public enum ComponentTag {
  NONE, INPUT, TRANSFORM, PHYSICS, COLLISION, BEHAVIOR, SMOOTH_MOVEMENT, RENDER, CAMERA
}
