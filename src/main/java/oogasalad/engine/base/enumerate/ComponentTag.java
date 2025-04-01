package oogasalad.engine.base.enumerate;

/**
 * The type that specifies the order of the update of the component.
 * The more the Component is in the front, the earlier it gets updated.
 *
 * @author Hsuan-Kai Liao
 */
public enum ComponentTag {
  NONE,
  TRANSFORM,
  PHYSICS,
  COLLISION,
  BEHAVIOR,
  RENDER
}
