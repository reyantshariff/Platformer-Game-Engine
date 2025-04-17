package oogasalad.view.gui.dropDown;

import java.util.List;
import javafx.scene.control.ComboBox;
import org.reflections.Reflections;

/**
 * A drop-down combo box for selecting classes from a specified package. This class is used to
 * create a GUI component.
 */
public class ClassSelectionDropDownList extends ComboBox<String> {

  /**
   * Constructor for ClassSelectionComboBox.
   *
   * @param packageName the package name to search for classes
   * @param superClass  the superclass to filter classes by
   */
  public ClassSelectionDropDownList(String prompt, String packageName, Class<?> superClass) {
    super();
    setPromptText(prompt);

    Reflections reflections = new Reflections(packageName);
    List<Class<?>> classes = List.copyOf(reflections.getSubTypesOf(superClass));

    for (Class<?> clazz : classes) {
      getItems().add(clazz.getSimpleName());
    }
  }
}
