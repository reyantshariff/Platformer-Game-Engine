package oogasalad.view.gui.textField;

import java.util.function.Consumer;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * A TextField that only accepts English letters and common symbols.
 * Provides ESC cancel, enter/blur validation, and failure recovery.
 */
public class StringTextField extends TextField {

  private String originalValue;
  private Consumer<String> changeListener;

  /**
   * Creates a StringTextField with optional default value and prompt text.
   * @param initialValue the initial String value to set (can be null)
   * @param prompt the prompt text to show when field is empty
   */
  public StringTextField(String initialValue, String prompt) {
    super();
    setPromptText(prompt);
    originalValue = initialValue != null ? initialValue : "";

    // Allow only letters, digits, spaces, and punctuation
    TextFormatter<String> formatter = new TextFormatter<>(
        change -> {
          String newText = change.getControlNewText();
          if (newText.matches("[a-zA-Z0-9 \\p{Punct}]*")) {
            return change;
          } else {
            return null;
          }
        });
    setTextFormatter(formatter);
    setText(originalValue);

    // Cancel editing if ESC is pressed
    setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ESCAPE) {
        onCancel();
      }
      e.consume();
    });

    // Handle "submit" events: Enter key or losing focus
    setOnAction(e -> onSubmit());
    focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
      if (!isNowFocused) {
        onSubmit();
      }
    });
  }

  /**
   * Registers a listener to be called after input is finalized.
   * @param listener the callback with (oldValue, newValue)
   */
  public void addChangeListener(Consumer<String> listener) {
    this.changeListener = listener;
  }

  private void onCancel() {
    try {
       {
        setText(originalValue);
        changeListener.accept(originalValue);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void onSubmit() {
    try {
      String newValue = getText();
      if (!newValue.equals(originalValue) && changeListener != null) {
        changeListener.accept(newValue);
        originalValue = newValue; // update base value if accepted
      }
    } catch (Exception e) {
      onCancel();
    }
  }
}
