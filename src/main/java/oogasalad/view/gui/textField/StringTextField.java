package oogasalad.view.gui.textField;

import javafx.scene.control.TextFormatter;

/**
 * A TextField that only accepts English letters and common symbols.
 * Provides ESC cancel, enter/blur validation, and failure recovery.
 */
public class StringTextField extends TypedTextField<String> {

  /**
   * Create a text field with easier way to add listener
   */
  public StringTextField(String initialValue, String prompt) {
    super(initialValue, prompt);
  }

  @Override
  protected TextFormatter<String> createTextFormatter(String initialValue) {
    return new TextFormatter<>(change -> {
      String newText = change.getControlNewText();
      return newText.matches("[a-zA-Z0-9 \\p{Punct}]*") ? change : null;
    });
  }

  @Override
  protected String parseText(String text) {
    return text;
  }
}