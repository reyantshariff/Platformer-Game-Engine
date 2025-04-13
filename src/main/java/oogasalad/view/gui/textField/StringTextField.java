package oogasalad.view.gui.textField;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

/**
 * A TextField that only accepts English letters and common symbols.
 *
 * @author Hsuan-Kai Liao
 */
public class StringTextField extends TextField {

  /**
   * Creates a StringTextField with optional default value and prompt text.
   * @param initialValue the initial String value to set (can be null)
   * @param prompt the prompt text to show when field is empty
   */
  public StringTextField(String initialValue, String prompt) {
    super();
    setPromptText(prompt);

    TextFormatter<String> formatter = new TextFormatter<>(
        change -> {
          String newText = change.getControlNewText();
          // Only allow letters (a-z, A-Z), digits, space, and basic symbols
          if (newText.matches("[a-zA-Z0-9 \\p{Punct}]*")) {
            return change;
          } else {
            return null;
          }
        }
    );

    setText(initialValue);
    setTextFormatter(formatter);
  }

  /**
   * Adds a listener to the text property.
   * @param listener the listener to add
   */
  public void addListener(javafx.beans.value.ChangeListener<String> listener) {
    textProperty().addListener(listener);
  }
}
