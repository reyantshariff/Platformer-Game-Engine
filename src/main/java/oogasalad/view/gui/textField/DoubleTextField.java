package oogasalad.view.gui.textField;

import java.util.Objects;
import java.util.function.Predicate;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.util.converter.DoubleStringConverter;

/**
 * A TextField that only accepts valid double values.
 *
 * @author Hsuan-Kai Liao
 */
public class DoubleTextField extends TextField {

  private static final String DASH = "-";
  private static final String DOT = ".";

  private Double originalValue;
  private Predicate<Double> changeListener;

  /**
   * Creates a DoubleTextField with optional default value and prompt text.
   * @param initialValue the initial double value to set (can be null)
   * @param prompt the prompt text to show when field is empty
   */
  public DoubleTextField(Double initialValue, String prompt) {
    super();
    setPromptText(prompt);

    setTextFormatter(createTextFormatter(initialValue));
    setOnKeyPressed(this::handleKeyEvents);
    textProperty().addListener((observable, oldValue, newValue) -> {
      handleTextChange(newValue);
    });
    focusedProperty().addListener((observable, oldValue, newValue) -> {
      handleFocusChange(oldValue, newValue);
    });
  }

  private TextFormatter<Double> createTextFormatter(Double initialValue) {
    return new TextFormatter<>(
        new DoubleStringConverter(),
        initialValue,
        change -> {
          String newText = change.getControlNewText();
          if (newText.isEmpty() || newText.equals(DASH) || newText.equals(DOT)) return change;
          try {
            Double.parseDouble(newText);
            return change;
          } catch (NumberFormatException e) {
            return null;
          }
        }
    );
  }

  private void handleKeyEvents(javafx.scene.input.KeyEvent e) {
    if (e.getCode() == KeyCode.ESCAPE) {
      onCancel();
    }
    e.consume();
  }

  private void handleTextChange(String isNowText) {
    if (onSubmit(Double.parseDouble(isNowText))) {
      setStyle("-fx-text-fill: black;");
    } else {
      setStyle("-fx-text-fill: red;");
    }
  }

  private void handleFocusChange(boolean wasFocused, boolean isNowFocused) {
    if (!isNowFocused) {
      onComplete();
    } else if (!wasFocused) {
      originalValue = Double.parseDouble(getText());
    }
  }

  /**
   * Registers a listener to be called after input is finalized.
   * @param listener the callback with (oldValue, newValue)
   */
  public void setChangeListener(Predicate<Double> listener) {
    this.changeListener = listener;
  }

  private void onCancel() {
    setText(String.valueOf(originalValue));
    changeListener.test(originalValue);
  }

  private boolean onSubmit(Double newValue) {
    if (!newValue.equals(originalValue) && changeListener != null) {
      return changeListener.test(newValue);
    }
    return true;
  }

  private void onComplete() {
    String newValue = getText();
    Double newDouble = Double.valueOf(newValue);
    if (!(Objects.equals(originalValue, newDouble)) && changeListener != null) {
      if (changeListener.test(newDouble)) {
        originalValue = newDouble; // update base value if accepted
      } else {
        onCancel();
      }
    }
  }
}
