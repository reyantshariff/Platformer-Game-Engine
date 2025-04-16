package oogasalad.view.gui.deserializedFieldInput;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import oogasalad.model.engine.base.serialization.SerializedField;

import java.util.ArrayList;
import java.util.List;
import oogasalad.view.gui.textField.StringTextField;

/**
 * A GUI component for editing a serialized List<String> field
 *
 * @author Hsuan-Kai Liao
 */
public class StringListFieldInput extends DeserializedFieldUI<List<String>> {

  private VBox listContainer;
  private SerializedField<List<String>> field;

  /**
   * Displays the GUI for editing a List<String> field.
   * @param field the serialized field to bind to this input
   * @return a Node representing the interactive list input UI
   */
  @Override
  protected Node showGUI(SerializedField<List<String>> field) {
    this.field = field;

    String name = field.getFieldName().replaceAll("([a-z])([A-Z])", "$1 $2");
    name = name.substring(0, 1).toUpperCase() + name.substring(1);
    Label label = new Label(name);

    listContainer = new VBox(5);
    listContainer.setPadding(new Insets(5));
    List<String> values = field.getValue() != null ? field.getValue() : new ArrayList<>();
    for (String value : values) {
      listContainer.getChildren().add(createItemField(value));
    }

    Button addButton = new Button("+");
    addButton.setOnAction(e -> listContainer.getChildren().add(createItemField("")));

    VBox root = new VBox(5, label, listContainer, addButton);
    HBox.setHgrow(root, Priority.ALWAYS);
    root.setAlignment(Pos.TOP_LEFT);
    return root;
  }

  private HBox createItemField(String initialValue) {
    StringTextField textField = new StringTextField(initialValue, "...");

    // Button to remove this item
    Button removeButton = new Button("−");
    removeButton.setOnAction(e -> {
      listContainer.getChildren().remove(textField.getParent());
      updateFieldValue();
    });

    // Listener to update the field whenever the text changes
    textField.setChangeListener(e -> updateFieldValue());

    HBox itemBox = new HBox(5, textField, removeButton);
    HBox.setHgrow(textField, Priority.ALWAYS);
    itemBox.setAlignment(Pos.CENTER_LEFT);
    return itemBox;
  }

  private boolean updateFieldValue() {
    List<String> updatedValues = listContainer.getChildren().stream()
        .filter(node -> node instanceof HBox)
        .map(node -> extractTextFromItemBox((HBox) node))
        .flatMap(List::stream)
        .filter(val -> !val.isEmpty())
        .toList();

    field.setValue(updatedValues);

    return true;
  }

  private List<String> extractTextFromItemBox(HBox itemBox) {
    List<String> values = new ArrayList<>();
    for (Node sub : itemBox.getChildren()) {
      if (sub instanceof TextField tf) {
        values.add(tf.getText().trim());
      }
    }
    return values;
  }

}
