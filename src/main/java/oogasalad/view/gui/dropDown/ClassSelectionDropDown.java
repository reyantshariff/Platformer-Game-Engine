package oogasalad.view.gui.dropDown;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ComboBox;
import org.reflections.Reflections;

/**
 * A drop-down menu for selecting classes from a specified package.
 * This class is used to create a GUI component
 *
 * @author Hsuan-Kai Liao
 */
public class ClassSelectionDropDown extends ComboBox<String> {

  /**
   * Constructor for ClassSelectionDropDown.
   * @param packageName the package name to search for classes
   * @param superClass the superclass to filter classes by
   */
  public ClassSelectionDropDown(String packageName, Class<?> superClass) {
    super();
    setEditable(true);
    setPromptText("Select");

    Reflections reflections = new Reflections(packageName);
    List<Class<?>> classes = new ArrayList<>(reflections.getSubTypesOf(superClass));

    getItems().setAll(classes.stream().map(Class::getSimpleName).toList());
  }
}
