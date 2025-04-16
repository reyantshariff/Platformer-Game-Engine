package oogasalad.view.gui.dropDown;

import java.util.List;
import java.util.function.Consumer;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import org.reflections.Reflections;

/**
 * A drop-down menu for selecting classes from a specified package. This class is used to create a
 * GUI component.
 */
public class ClassSelectionDropDownMenu extends MenuButton {

  private Consumer<String> selectionCallback;

  /**
   * Constructor for ClassSelectionDropDown.
   *
   * @param packageName the package name to search for classes
   * @param superClass  the superclass to filter classes by
   */
  public ClassSelectionDropDownMenu(String title, String packageName, Class<?> superClass) {
    super(title);

    Reflections reflections = new Reflections(packageName);
    List<Class<?>> classes = List.copyOf(reflections.getSubTypesOf(superClass));

    for (Class<?> clazz : classes) {
      String className = clazz.getSimpleName();
      MenuItem menuItem = new MenuItem(className);
      menuItem.setOnAction(event -> {
        if (selectionCallback != null) {
          selectionCallback.accept(className);
        }
      });
      getItems().add(menuItem);
    }
  }

  /**
   * Sets the action to be performed when a class is selected.
   *
   * @param callback the callback to be invoked when an item is selected
   */
  public void setOnClassSelected(Consumer<String> callback) {
    this.selectionCallback = callback;
  }
}
