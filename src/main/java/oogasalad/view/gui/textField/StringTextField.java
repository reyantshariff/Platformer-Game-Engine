package oogasalad.view.gui.textField;

import java.util.function.Predicate;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;

/**
 * A TextField that only accepts English letters and common symbols.
 * Provides ESC cancel, enter/blur validation, and failure recovery.
 */
public class StringTextField extends TextField {

  private String originalValue;
  private Predicate<String> changeListener;

  /**
   * Creates a StringTextField with optional default value and prompt text.
   * @param initialValue the initial String value to set (can be null)
   * @param prompt the prompt text to show when field is empty
   */
  public StringTextField(String initialValue, String prompt) {
    super();
    setPromptText(prompt);
    originalValue = (initialValue != null) ? initialValue : "";

    setTextFormatter(createTextFormatter());
    setText(originalValue);

    setOnKeyPressed(e -> handleKeyPress(e.getCode()));
    textProperty().addListener((obs, wasText, isNowText) -> handleTextChange(isNowText));
    focusedProperty().addListener((obs, wasFocused, isNowFocused) -> handleFocusChange(wasFocused, isNowFocused));
  }

  private void handleFocusChange(boolean wasFocused, boolean isNowFocused) {
    if (!isNowFocused) {
      onComplete();
    } else if (!wasFocused) {
      originalValue = getText();
    }
  }

  private TextFormatter<String> createTextFormatter() {
    return new TextFormatter<>(change -> {
      String newText = change.getControlNewText();
      return newText.matches("[a-zA-Z0-9 \\p{Punct}]*") ? change : null;
    });
  }

  private void handleKeyPress(KeyCode keyCode) {
    if (keyCode == KeyCode.ESCAPE) {
      onCancel();
    }
  }

  private void handleTextChange(String newValue) {
    if (onSubmit(newValue)) {
      setStyle("-fx-text-fill: black;");
    } else {
      setStyle("-fx-text-fill: red;");
    }
  }

  /**
   * Registers a listener to be called after input is finalized.
   * @param listener the callback with (String: newValue) and returns true if accepted
   */
  public void setChangeListener(Predicate<String> listener) {
    this.changeListener = listener;
  }

  private void onCancel() {
    setText(originalValue);
    changeListener.test(originalValue);
  }

  private boolean onSubmit(String newValue) {
    if (!newValue.equals(originalValue) && changeListener != null) {
      return changeListener.test(newValue);
    }
    return true;
  }

  private void onComplete() {
    String newValue = getText();
    if (!newValue.equals(originalValue) && changeListener != null) {
      if (changeListener.test(newValue)) {
        originalValue = newValue; // update base value if accepted
      } else {
        onCancel();
      }
    }
  }
}
