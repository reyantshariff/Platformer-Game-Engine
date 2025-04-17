package oogasalad.view.gui.deserializedFieldInput;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import oogasalad.model.engine.base.behavior.Behavior;
import oogasalad.model.engine.base.serialization.SerializedField;

import java.util.ArrayList;
import java.util.List;

/**
 * GUI component for editing a List<Behavior> field. Each Behavior can be dynamically added,
 * removed, and edited.
 *
 * @author Hsuan-Kai Liao
 */
public class BehaviorListFieldInput extends DeserializedFieldUI<List<Behavior>> {

  // TODO: make this outside in the css
  private static final String BEHAVIOR_ROW_STYLE = """
      -fx-background-color: #AcAcAc;
      -fx-background-radius: 6;
      -fx-padding: 8;
      """;

  private VBox listContainer;
  private SerializedField field;

  @Override
  protected Node showGUI(SerializedField field) {
    this.field = field;

    listContainer = createListContainer();
    populateExistingBehaviors();

    Button addButton = createAddButton();

    VBox root = new VBox(5, listContainer, addButton);
    root.setAlignment(Pos.TOP_LEFT);
    return root;
  }

  private VBox createListContainer() {
    VBox box = new VBox(5);
    box.setPadding(new Insets(5));
    return box;
  }

  @SuppressWarnings("unchecked")
  private void populateExistingBehaviors() {
    List<Behavior> values = field.getValue() != null ? (List<Behavior>) field.getValue() : new ArrayList<>();
    values.forEach(behavior -> listContainer.getChildren().add(createBehaviorRow(behavior)));
  }

  private Button createAddButton() {
    Button addButton = new Button("+");
    addButton.setOnAction(e -> {
      Behavior newBehavior = new Behavior();
      getBehaviorList().add(newBehavior);
      listContainer.getChildren().add(createBehaviorRow(newBehavior));
    });
    return addButton;
  }

  private HBox createBehaviorRow(Behavior behavior) {
    List<SerializedField> paramList = behavior.getSerializedFields();
    Label nameLabel = new Label("Behavior" + paramList.getFirst().getFieldName());
    HBox constraintList = DeserializedFieldUIFactory.createDeserializedFieldUI(paramList.get(1));
    HBox actionList = DeserializedFieldUIFactory.createDeserializedFieldUI(paramList.get(2));
    Button removeButton = createRemoveButton(behavior);

    VBox container = new VBox(5, nameLabel, constraintList, actionList);
    HBox row = new HBox(5, container, removeButton);

    row.setAlignment(Pos.CENTER_LEFT);
    HBox.setHgrow(container, Priority.ALWAYS);
    row.setStyle(BEHAVIOR_ROW_STYLE);

    return row;
  }

  private Button createRemoveButton(Behavior behavior) {
    Button removeButton = new Button("−");
    removeButton.setOnAction(e -> {
      listContainer.getChildren().remove(removeButton.getParent());
      getBehaviorList().remove(behavior);
    });
    return removeButton;
  }

  @SuppressWarnings("unchecked")
  private List<Behavior> getBehaviorList() {
    if (field.getValue() == null) {
      field.setValue(new ArrayList<>());
    }
    return (List<Behavior>) field.getValue();
  }
}
