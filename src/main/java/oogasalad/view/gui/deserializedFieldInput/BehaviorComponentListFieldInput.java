package oogasalad.view.gui.deserializedFieldInput;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import oogasalad.model.config.GameConfig;
import oogasalad.model.engine.base.behavior.BehaviorComponent;
import oogasalad.model.engine.base.enumerate.KeyCode;
import oogasalad.model.engine.base.serialization.Serializable;
import oogasalad.model.engine.base.serialization.SerializedField;
import oogasalad.model.engine.base.serialization.SetSerializedFieldException;
import oogasalad.view.gui.dropDown.ClassSelectionDropDownList;
import oogasalad.view.gui.textField.StringTextField;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Generic GUI component for deserializing a List of BehaviorComponent elements.
 *
 * @author Hsuan-Kai Liao
 */
public class BehaviorComponentListFieldInput<T extends BehaviorComponent<T>> extends
    DeserializedFieldUI<List<T>> {

  private final String componentPackage;
  private final Class<T> componentClass;

  private VBox listContainer;
  private SerializedField<List<T>> field;

  /**
   * Class constructor
   *
   * @param componentPackage: shows components
   * @param componentClass:   shows component type
   */
  public BehaviorComponentListFieldInput(String componentPackage, Class<T> componentClass) {
    this.componentPackage = componentPackage;
    this.componentClass = componentClass;
  }

  @Override
  protected Node showGUI(SerializedField<List<T>> field) {
    this.field = field;

    Label label = new Label(formatFieldName(field.getFieldName()));
    listContainer = new VBox(5);
    listContainer.setPadding(new Insets(5));

    Optional.ofNullable(field.getValue()).orElseGet(ArrayList::new)
        .forEach(component -> listContainer.getChildren().add(createComponentRow(component)));

    Button addButton = new Button("+");
    addButton.setOnAction(e -> listContainer.getChildren().add(createComponentRow(null)));

    VBox root = new VBox(5, label, listContainer, addButton);
    root.setAlignment(Pos.TOP_LEFT);
    return root;
  }

  private HBox createComponentRow(T initialComponent) {
    T[] componentRef = (T[]) Array.newInstance(componentClass, 1);
    componentRef[0] = initialComponent;

    ClassSelectionDropDownList dropDown = new ClassSelectionDropDownList("Select Component",
        componentPackage, componentClass);
    StringTextField paramField = new StringTextField("", "param...");
    Button removeButton = new Button("−");

    HBox row = new HBox(5, dropDown, paramField, removeButton);
    row.setAlignment(Pos.CENTER_LEFT);
    HBox.setHgrow(paramField, Priority.ALWAYS);

    initializeIfComponentExists(initialComponent, dropDown, paramField);
    setDropDownAction(dropDown, componentRef, paramField);
    setParamFieldBehavior(paramField, componentRef);
    setRemoveButtonBehavior(removeButton, row);

    row.setUserData(componentRef);
    return row;
  }

  private void initializeIfComponentExists(T component, ClassSelectionDropDownList dropDown,
      StringTextField paramField) {
    if (component == null) {
      return;
    }

    dropDown.setValue(component.getClass().getSimpleName());
    if (getGenericTypeName(component).equals(Void.class.getSimpleName())) {
      hide(paramField);
    } else {
      SerializedField<?> param = ((Serializable) component).getSerializedFields().getFirst();
      paramField.setText(Optional.ofNullable(param.getValue()).map(Object::toString).orElse(""));
    }
  }

  private void setDropDownAction(ClassSelectionDropDownList dropDown, T[] componentRef,
      StringTextField paramField) {
    dropDown.setOnAction(e -> {
      String className = dropDown.getValue();
      T newComponent = instantiateComponent(className);
      componentRef[0] = newComponent;

      SerializedField<?> param = ((Serializable) newComponent).getSerializedFields().getFirst();
      if (getGenericTypeName(newComponent).equals(Void.class.getSimpleName())) {
        hide(paramField);
      } else {
        show(paramField);
        paramField.setText(Optional.ofNullable(param.getValue()).map(Object::toString).orElse(""));
      }

      updateFieldList();
    });
  }

  private void setParamFieldBehavior(StringTextField paramField, T[] componentRef) {
    paramField.setChangeListener(e -> updateParamField(componentRef, paramField));
  }

  private boolean updateParamField(T[] componentRef, StringTextField paramField) {
    return Optional.ofNullable(componentRef[0]).map(component -> {
      SerializedField<?> param = ((Serializable) component).getSerializedFields().getFirst();
      return updateParameter(component, param, paramField.getText());
    }).orElse(false);
  }

  private void setRemoveButtonBehavior(Button removeButton, HBox row) {
    removeButton.setOnAction(e -> {
      listContainer.getChildren().remove(row);
      updateFieldList();
    });
  }

  private void updateFieldList() {
    List<T> updated = listContainer.getChildren().stream()
        .filter(HBox.class::isInstance)
        .map(HBox.class::cast)
        .map(row -> {
          T[] ref = (T[]) row.getUserData();
          return ref[0];
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    field.setValue(updated);
  }

  private T instantiateComponent(String className) {
    try {
      Class<?> clazz = Class.forName(componentPackage + "." + className);
      return componentClass.cast(clazz.getDeclaredConstructor().newInstance());
    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException
             | IllegalAccessException | InvocationTargetException e) {
      GameConfig.LOGGER.error("Error instantiating component: " + className, e);
    }
    return null;
  }


  @SuppressWarnings("unchecked")
  private boolean updateParameter(T component, SerializedField<?> param, String newVal) {
    String typeName = getGenericTypeName(component);

    try {
      switch (typeName) {
        case "String" -> ((SerializedField<String>) param).setValue(newVal);
        case "Double" -> ((SerializedField<Double>) param).setValue(Double.parseDouble(newVal));
        case "KeyCode" -> ((SerializedField<KeyCode>) param).setValue(KeyCode.valueOf(newVal));
        default -> throw new IllegalArgumentException("Unsupported type: " + typeName);
      }
      return true;
    } catch (SetSerializedFieldException | IllegalArgumentException e) {
      return false;
    }
  }

  private String getGenericTypeName(T component) {
    return Optional.ofNullable(component.getClass().getGenericSuperclass())
        .filter(ParameterizedType.class::isInstance)
        .map(ParameterizedType.class::cast)
        .map(t -> t.getActualTypeArguments()[0])
        .filter(Class.class::isInstance)
        .map(Class.class::cast)
        .map(Class::getSimpleName)
        .orElse("Object");
  }

  private void hide(StringTextField field) {
    field.setVisible(false);
    field.setManaged(false);
  }

  private void show(StringTextField field) {
    field.setVisible(true);
    field.setManaged(true);
  }

  private String formatFieldName(String fieldName) {
    return Character.toUpperCase(fieldName.charAt(0)) +
        fieldName.substring(1).replaceAll("([a-z])([A-Z])", "$1 $2");
  }
}
