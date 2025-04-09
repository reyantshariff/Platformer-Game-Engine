package oogasalad.builder;
/**
 * Interface for Storing actions in the builder editor
 *@author Reyan Shariff
 */
public interface EditorAction {
  void undo();
  void redo();
}
