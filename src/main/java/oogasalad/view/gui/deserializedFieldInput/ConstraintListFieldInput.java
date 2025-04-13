package oogasalad.view.gui.deserializedFieldInput;

import java.lang.reflect.ParameterizedType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import oogasalad.model.engine.base.behavior.BehaviorAction;
import oogasalad.model.engine.base.behavior.BehaviorConstraint;
import oogasalad.model.engine.base.enumerate.KeyCode;
import oogasalad.model.engine.base.serialization.SerializedField;
import oogasalad.view.gui.dropDown.ClassSelectionDropDownList;
import oogasalad.view.gui.textField.StringTextField;

import java.util.*;
import java.util.stream.Collectors;

/**
 * GUI component for editing a List<BehaviorConstraint<?>> field.
 *
 * @author Hsuan-Kai Liao
 */
public class ConstraintListFieldInput extends DeserializedFieldUI<List<BehaviorConstraint<?>>> {

  private static final String CONSTRAINT_PACKAGE = "oogasalad.model.engine.constraint";

  private VBox listContainer;
  private SerializedField<List<BehaviorConstraint<?>>> field;

  @Override
  protected Node showGUI(SerializedField<List<BehaviorConstraint<?>>> field) {
    this.field = field;

    Label label = new Label(formatFieldName(field.getFieldName()));
    listContainer = new VBox(5);
    listContainer.setPadding(new Insets(5));

    Optional.ofNullable(field.getValue()).orElseGet(ArrayList::new)
        .forEach(constraint -> listContainer.getChildren().add(createConstraintRow(constraint)));

    Button addButton = new Button("+");
    addButton.setOnAction(e -> listContainer.getChildren().add(createConstraintRow(null)));

    VBox root = new VBox(5, label, listContainer, addButton);
    root.setAlignment(Pos.TOP_LEFT);
    return root;
  }

  private HBox createConstraintRow(BehaviorConstraint<?> initialConstraint) {
    BehaviorConstraint<?>[] constraintRef = new BehaviorConstraint[]{initialConstraint};

    ClassSelectionDropDownList dropDown = new ClassSelectionDropDownList("Select Constraint", CONSTRAINT_PACKAGE, BehaviorConstraint.class);
    StringTextField paramField = new StringTextField("", "param...");
    Button removeButton = new Button("−");

    HBox.setHgrow(paramField, Priority.ALWAYS);
    HBox row = new HBox(5, dropDown, paramField, removeButton);
    row.setAlignment(Pos.CENTER_LEFT);

    // Init if constraint already exists
    if (initialConstraint != null) {
      dropDown.setValue(initialConstraint.getClass().getSimpleName());
      if (getGenericTypeName(initialConstraint).equals("Void")) {
        paramField.setVisible(false);
        paramField.setManaged(false);
      } else {
        SerializedField<?> param = initialConstraint.getSerializedFields().getFirst();
        paramField.setText(Optional.ofNullable(param.getValue()).map(Object::toString).orElse(""));
      }
    }

    // Dropdown change: instantiate new action
    dropDown.setOnAction(e -> {
      String className = dropDown.getValue();
      BehaviorConstraint<?> newConstraint = instantiateConstraint(className);
      constraintRef[0] = newConstraint;

      SerializedField<?> param = newConstraint.getSerializedFields().getFirst();
      if (getGenericTypeName(constraintRef[0]).equals("Void")) {
        paramField.setVisible(false);
        paramField.setManaged(false);
      } else {
        paramField.setVisible(true);
        paramField.setManaged(true);
        paramField.setText(Optional.ofNullable(param.getValue()).map(Object::toString).orElse(""));
      }

      updateFieldList();
    });

    // Param field update - Using setOnAction to detect when user presses Enter
    paramField.setOnAction(e -> {
      Optional.ofNullable(constraintRef[0]).ifPresent(constraint -> {
        SerializedField<?> param = constraint.getSerializedFields().getFirst();
        updateParameter(constraint, param, paramField.getText(), paramField);
      });
    });

    // Alternatively, you can use a focus listener to detect when the field loses focus
    paramField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
      if (!isNowFocused) {
        Optional.ofNullable(constraintRef[0]).ifPresent(constraint -> {
          SerializedField<?> param = constraint.getSerializedFields().getFirst();
          updateParameter(constraint, param, paramField.getText(), paramField);
        });
      }
    });

    // Remove row
    removeButton.setOnAction(e -> {
      listContainer.getChildren().remove(row);
      updateFieldList();
    });

    return row;
  }

  private void updateFieldList() {
    List<BehaviorConstraint<?>> updated = listContainer.getChildren().stream()
        .filter(HBox.class::isInstance)
        .map(HBox.class::cast)
        .map(this::buildConstraintFromRow)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    field.setValue(updated);
  }

  private BehaviorConstraint<?> buildConstraintFromRow(HBox row) {
    try {
      String className = ((ClassSelectionDropDownList) row.getChildren().get(0)).getValue();
      TextField paramField = (TextField) row.getChildren().get(1);

      BehaviorConstraint<?> constraint = instantiateConstraint(className);
      SerializedField<?> param = constraint.getSerializedFields().getFirst();
      updateParameter(constraint, param, paramField.getText(), paramField);

      return constraint;
    } catch (Exception e) {
      System.err.println(e.getMessage());
      return null;
    }
  }

  private BehaviorConstraint<?> instantiateConstraint(String className) {
    try {
      Class<?> clazz = Class.forName(CONSTRAINT_PACKAGE + "." + className);
      return (BehaviorConstraint<?>) clazz.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Failed to instantiate: " + className, e);
    }
  }

  @SuppressWarnings("unchecked")
  private void updateParameter(BehaviorConstraint<?> constraint, SerializedField<?> param, String newVal, TextField field) {
    String typeName = getGenericTypeName(constraint);

    try {
      switch (typeName) {
        case "String" -> ((SerializedField<String>) param).setValue(newVal);
        case "Double" -> ((SerializedField<Double>) param).setValue(Double.parseDouble(newVal));
        case "KeyCode" -> ((SerializedField<KeyCode>) param).setValue(KeyCode.valueOf(newVal));
        default -> throw new RuntimeException("Unsupported type: " + typeName);
      }
    } catch (Exception e) {
      // fallback if parse fails
      Object fallback = param.getValue();
      field.setText(fallback != null ? fallback.toString() : "");
    }
  }

  private String getGenericTypeName(BehaviorConstraint<?> constraint) {
    return Optional.ofNullable(constraint.getClass().getGenericSuperclass())
        .filter(ParameterizedType.class::isInstance)
        .map(ParameterizedType.class::cast)
        .map(t -> t.getActualTypeArguments()[0])
        .filter(Class.class::isInstance)
        .map(Class.class::cast)
        .map(Class::getSimpleName)
        .orElse("Object");
  }

  private String formatFieldName(String fieldName) {
    return Character.toUpperCase(fieldName.charAt(0)) +
        fieldName.substring(1).replaceAll("([a-z])([A-Z])", "$1 $2");
  }
}
