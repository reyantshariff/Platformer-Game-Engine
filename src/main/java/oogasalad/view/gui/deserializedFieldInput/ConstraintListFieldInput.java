package oogasalad.view.gui.deserializedFieldInput;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import oogasalad.model.config.GameConfig;
import oogasalad.model.engine.base.behavior.BehaviorConstraint;
import oogasalad.model.engine.base.enumerate.KeyCode;
import oogasalad.model.engine.base.serialization.SerializedField;
import oogasalad.model.engine.base.serialization.SetSerializedFieldException;
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

    HBox row = new HBox(5, dropDown, paramField, removeButton);
    row.setAlignment(Pos.CENTER_LEFT);
    HBox.setHgrow(paramField, Priority.ALWAYS);

    initializeConstraint(initialConstraint, dropDown, paramField);
    configureDropDown(dropDown, constraintRef, paramField);
    configureParamFieldListeners(paramField, constraintRef);
    configureRemoveButton(removeButton, row);

    // Bind the drop-down and parameter field to the constraint reference
    row.setUserData(constraintRef);

    return row;
  }

  private void initializeConstraint(BehaviorConstraint<?> constraint, ClassSelectionDropDownList dropDown, StringTextField paramField) {
    if (constraint == null) return;

    dropDown.setValue(constraint.getClass().getSimpleName());
    if (getGenericTypeName(constraint).equals(Void.class.getSimpleName())) {
      hideField(paramField);
    } else {
      SerializedField<?> param = constraint.getSerializedFields().getFirst();
      paramField.setText(Optional.ofNullable(param.getValue()).map(Object::toString).orElse(""));
    }
  }

  private void configureDropDown(ClassSelectionDropDownList dropDown, BehaviorConstraint<?>[] constraintRef, StringTextField paramField) {
    dropDown.setOnAction(e -> {
      String className = dropDown.getValue();
      BehaviorConstraint<?> newConstraint = instantiateConstraint(className);
      constraintRef[0] = newConstraint;

      SerializedField<?> param = newConstraint.getSerializedFields().getFirst();
      if (getGenericTypeName(newConstraint).equals(Void.class.getSimpleName())) {
        hideField(paramField);
      } else {
        showField(paramField);
        paramField.setText(Optional.ofNullable(param.getValue()).map(Object::toString).orElse(""));
      }

      updateFieldList();
    });
  }

  private void configureParamFieldListeners(StringTextField paramField, BehaviorConstraint<?>[] constraintRef) {
    paramField.setChangeListener(e -> updateConstraintParam(constraintRef, paramField));
  }

  private boolean updateConstraintParam(BehaviorConstraint<?>[] constraintRef, StringTextField paramField) {
    return Optional.ofNullable(constraintRef[0]).map(constraint -> {
      SerializedField<?> param = constraint.getSerializedFields().getFirst();
      return updateParameter(constraint, param, paramField.getText());
    }).orElse(false);
  }

  private void configureRemoveButton(Button removeButton, HBox row) {
    removeButton.setOnAction(e -> {
      listContainer.getChildren().remove(row);
      updateFieldList();
    });
  }

  private void hideField(StringTextField field) {
    field.setVisible(false);
    field.setManaged(false);
  }

  private void showField(StringTextField field) {
    field.setVisible(true);
    field.setManaged(true);
  }

  private void updateFieldList() {
    List<BehaviorConstraint<?>> updated = listContainer.getChildren().stream()
        .filter(HBox.class::isInstance)
        .map(HBox.class::cast)
        .map(row -> {
          BehaviorConstraint<?>[] ref = (BehaviorConstraint<?>[]) row.getUserData();
          return ref[0];
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    field.setValue(updated);
  }

  private BehaviorConstraint<?> instantiateConstraint(String className) {
    try {
      Class<?> clazz = Class.forName(CONSTRAINT_PACKAGE + "." + className);
      return (BehaviorConstraint<?>) clazz.getDeclaredConstructor().newInstance();
    } catch (NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      GameConfig.LOGGER.error(e);
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  private boolean updateParameter(BehaviorConstraint<?> constraint, SerializedField<?> param, String newVal) {
    String typeName = getGenericTypeName(constraint);

    try {
      switch (typeName) {
        case "String" -> ((SerializedField<String>) param).setValue(newVal);
        case "Double" -> ((SerializedField<Double>) param).setValue(Double.parseDouble(newVal));
        case "KeyCode" -> ((SerializedField<KeyCode>) param).setValue(KeyCode.valueOf(newVal));
        default -> { return false; }
      }
      return true;
    } catch (SetSerializedFieldException | NumberFormatException e) {
      return false;
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
