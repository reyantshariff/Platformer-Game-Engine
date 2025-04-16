package oogasalad.view.gui.deserializedFieldInput;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import oogasalad.model.engine.base.serialization.SerializedField;

/**
 * Abstract class for creating a GUI element for a SerializedField.
 *
 * @param <T> the type of the input
 * @author Hsuan-Kai Liao
 */
public abstract class DeserializedFieldUI<T> extends HBox {

  /**
   * Constructor for DeserializedFieldUI.
   */
  protected DeserializedFieldUI() {
    super(10);
    setStyle("-fx-background-color: #dadada; -fx-padding: 5;");
  }

  @SuppressWarnings("unchecked")
  void initGUI(SerializedField<?> field) {
    getChildren().add(showGUI((SerializedField<T>) field));
  }

  /**
   * Create a GUI element for the given SerializedField.
   *
   * @param field the SerializedField to create a GUI element for
   * @return a Node representing the GUI element
   */
  protected abstract Node showGUI(SerializedField<T> field);
}
