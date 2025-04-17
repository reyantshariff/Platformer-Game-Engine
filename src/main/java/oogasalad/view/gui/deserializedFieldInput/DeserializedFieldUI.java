package oogasalad.view.gui.deserializedFieldInput;

import javafx.scene.Node;
import javafx.scene.control.Label;
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
  void initGUI(SerializedField field) {
    try {
      T cast = (T) field.getValue();
      assert cast != null;

      getChildren().add(showGUI(field));
    } catch (ClassCastException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Create the label based on the name of the Serializable Field.
   * @param field the input field
   * @return the label
   */
  protected Label createLabel(SerializedField field) {
    String name = field.getFieldName().replaceAll("([a-z])([A-Z])", "$1 $2");
    name = name.substring(0, 1).toUpperCase() + name.substring(1);
    return new Label(name);
  }

  /**
   * Create a GUI element for the given SerializedField.
   *
   * @param field the SerializedField to create a GUI element for
   * @return a Node representing the GUI element
   */
  protected abstract Node showGUI(SerializedField field);
}
