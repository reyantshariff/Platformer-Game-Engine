package oogasalad.view.gui.deserializedFieldInput;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import oogasalad.model.engine.base.behavior.BehaviorAction;
import oogasalad.model.engine.base.serialization.SerializedField;
import oogasalad.view.gui.dropDown.ClassSelectionDropDownList;
import oogasalad.view.gui.textField.StringTextField;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * GUI component for editing a List<BehaviorAction<?>> field.
 *
 * @author Hsuan-Kai Liao
 */
public class ActionListFieldInput extends DeserializedFieldUI<List<BehaviorAction<?>>> {

  private static final String ACTION_PACKAGE = "oogasalad.model.engine.action";

  private VBox listContainer;
  private SerializedField<List<BehaviorAction<?>>> field;

  @Override
  protected Node showGUI(SerializedField<List<BehaviorAction<?>>> field) {
    this.field = field;

    Label label = new Label(formatFieldName(field.getFieldName()));
    listContainer = new VBox(5);
    listContainer.setPadding(new Insets(5));

    Optional.ofNullable(field.getValue()).orElseGet(ArrayList::new)
        .forEach(action -> listContainer.getChildren().add(createActionRow(action)));

    Button addButton = new Button("+");
    addButton.setOnAction(e -> listContainer.getChildren().add(createActionRow(null)));

    VBox root = new VBox(5, label, listContainer, addButton);
    root.setAlignment(Pos.TOP_LEFT);
    return root;
  }

  private HBox createActionRow(BehaviorAction<?> initialAction) {
    BehaviorAction<?>[] actionRef = new BehaviorAction[]{initialAction};

    ClassSelectionDropDownList dropDown = new ClassSelectionDropDownList("Select Action", ACTION_PACKAGE, BehaviorAction.class);
    StringTextField paramField = new StringTextField("", "param...");
    Button removeButton = new Button("−");

    HBox row = new HBox(5, dropDown, paramField, removeButton);
    row.setAlignment(Pos.CENTER_LEFT);
    HBox.setHgrow(paramField, Priority.ALWAYS);

    initializeIfActionExists(initialAction, dropDown, paramField);

    setDropDownBehavior(dropDown, actionRef, paramField);
    setParamFieldBehavior(paramField, actionRef);
    setRemoveButtonBehavior(removeButton, row);

    return row;
  }

  private void initializeIfActionExists(BehaviorAction<?> action, ClassSelectionDropDownList dropDown, StringTextField paramField) {
    if (action == null) return;

    dropDown.setValue(action.getClass().getSimpleName());
    if (getGenericTypeName(action).equals("Void")) {
      hide(paramField);
    } else {
      SerializedField<?> param = action.getSerializedFields().getFirst();
      paramField.setText(Optional.ofNullable(param.getValue()).map(Object::toString).orElse(""));
    }
  }

  private void setDropDownBehavior(ClassSelectionDropDownList dropDown, BehaviorAction<?>[] actionRef, StringTextField paramField) {
    dropDown.setOnAction(e -> {
      String className = dropDown.getValue();
      BehaviorAction<?> newAction = instantiateAction(className);
      actionRef[0] = newAction;

      SerializedField<?> param = newAction.getSerializedFields().getFirst();
      if (getGenericTypeName(newAction).equals("Void")) {
        hide(paramField);
      } else {
        show(paramField);
        paramField.setText(Optional.ofNullable(param.getValue()).map(Object::toString).orElse(""));
      }

      updateFieldList();
    });
  }

  private void setParamFieldBehavior(StringTextField paramField, BehaviorAction<?>[] actionRef) {
    paramField.setOnAction(e -> updateParamField(actionRef, paramField));
    paramField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
      if (!isNowFocused) {
        updateParamField(actionRef, paramField);
      }
    });
  }

  private void updateParamField(BehaviorAction<?>[] actionRef, StringTextField paramField) {
    Optional.ofNullable(actionRef[0]).ifPresent(action -> {
      SerializedField<?> param = action.getSerializedFields().getFirst();
      updateParameter(action, param, paramField.getText(), paramField);
    });
  }

  private void setRemoveButtonBehavior(Button removeButton, HBox row) {
    removeButton.setOnAction(e -> {
      listContainer.getChildren().remove(row);
      updateFieldList();
    });
  }

  private void hide(StringTextField field) {
    field.setVisible(false);
    field.setManaged(false);
  }

  private void show(StringTextField field) {
    field.setVisible(true);
    field.setManaged(true);
  }


//  private HBox createActionRow(BehaviorAction<?> initialAction) {
//    BehaviorAction<?>[] actionRef = new BehaviorAction[]{initialAction};
//
//    ClassSelectionDropDownList dropDown = new ClassSelectionDropDownList("Select Action", ACTION_PACKAGE, BehaviorAction.class);
//    StringTextField paramField = new StringTextField("", "param...");
//    Button removeButton = new Button("−");
//
//    HBox.setHgrow(paramField, Priority.ALWAYS);
//    HBox row = new HBox(5, dropDown, paramField, removeButton);
//    row.setAlignment(Pos.CENTER_LEFT);
//
//    // Init if action already exists
//    if (initialAction != null) {
//      dropDown.setValue(initialAction.getClass().getSimpleName());
//      if (getGenericTypeName(initialAction).equals("Void")) {
//        paramField.setVisible(false);
//        paramField.setManaged(false);
//      } else {
//        SerializedField<?> param = initialAction.getSerializedFields().getFirst();
//        paramField.setText(Optional.ofNullable(param.getValue()).map(Object::toString).orElse(""));
//      }
//    }
//
//    // Dropdown change: instantiate new action
//    dropDown.setOnAction(e -> {
//      String className = dropDown.getValue();
//      BehaviorAction<?> newAction = instantiateAction(className);
//      actionRef[0] = newAction;
//
//      SerializedField<?> param = newAction.getSerializedFields().getFirst();
//      if (getGenericTypeName(actionRef[0]).equals("Void")) {
//        paramField.setVisible(false);
//        paramField.setManaged(false);
//      } else {
//        paramField.setVisible(true);
//        paramField.setManaged(true);
//        paramField.setText(Optional.ofNullable(param.getValue()).map(Object::toString).orElse(""));
//      }
//
//      updateFieldList();
//    });
//
//    // Param field update - Using setOnAction to detect when user presses Enter
//    paramField.setOnAction(e -> {
//      Optional.ofNullable(actionRef[0]).ifPresent(action -> {
//        SerializedField<?> param = action.getSerializedFields().getFirst();
//        updateParameter(action, param, paramField.getText(), paramField);
//      });
//    });
//
//    // Alternatively, you can use a focus listener to detect when the field loses focus
//    paramField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
//      if (!isNowFocused) {
//        Optional.ofNullable(actionRef[0]).ifPresent(action -> {
//          SerializedField<?> param = action.getSerializedFields().getFirst();
//          updateParameter(action, param, paramField.getText(), paramField);
//        });
//      }
//    });
//
//    // Remove row
//    removeButton.setOnAction(e -> {
//      listContainer.getChildren().remove(row);
//      updateFieldList();
//    });
//
//    return row;
//  }

  private void updateFieldList() {
    List<BehaviorAction<?>> updated = listContainer.getChildren().stream()
        .filter(HBox.class::isInstance)
        .map(HBox.class::cast)
        .map(this::buildActionFromRow)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    field.setValue(updated);
  }

  private static final Logger logger = LogManager.getLogger(ActionListFieldInput.class);

  private BehaviorAction<?> buildActionFromRow(HBox row) {
    try {
      String className = ((ClassSelectionDropDownList) row.getChildren().get(0)).getValue();
      TextField paramField = (TextField) row.getChildren().get(1);

      BehaviorAction<?> action = instantiateAction(className);
      SerializedField<?> param = action.getSerializedFields().getFirst();
      updateParameter(action, param, paramField.getText(), paramField);

      return action;
    } catch (Exception e) {
      logger.error("Failed to build action from row", e);
      return null;
    }
  }

  private BehaviorAction<?> instantiateAction(String className) {
    try {
      Class<?> clazz = Class.forName(ACTION_PACKAGE + "." + className);
      return (BehaviorAction<?>) clazz.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Failed to instantiate: " + className, e);
    }
  }

  @SuppressWarnings("unchecked")
  private void updateParameter(BehaviorAction<?> action, SerializedField<?> param, String newVal, TextField field) {
    String typeName = getGenericTypeName(action);

    try {
      switch (typeName) {
        case "String" -> ((SerializedField<String>) param).setValue(newVal);
        case "Double" -> ((SerializedField<Double>) param).setValue(Double.parseDouble(newVal));
        default -> throw new RuntimeException("Unsupported type: " + typeName);
      }
    } catch (Exception e) {
      // fallback if parse fails
      Object fallback = param.getValue();
      field.setText(fallback != null ? fallback.toString() : "");
    }
  }

  private String getGenericTypeName(BehaviorAction<?> action) {
    return Optional.ofNullable(action.getClass().getGenericSuperclass())
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
