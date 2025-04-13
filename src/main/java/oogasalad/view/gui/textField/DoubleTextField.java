package oogasalad.view.gui.textField;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.DoubleStringConverter;

/**
 * A TextField that only accepts valid double values.
 *
 * @author Hsuan-Kai Liao
 */
public class DoubleTextField extends TextField {
  /**
   * Creates a DoubleTextField with optional default value and prompt text.
   * @param initialValue the initial double value to set (can be null)
   * @param prompt the prompt text to show when field is empty
   */
  public DoubleTextField(Double initialValue, String prompt) {
    super();
    setPromptText(prompt);

    TextFormatter<Double> formatter = new TextFormatter<>(
        new DoubleStringConverter(),
        initialValue,
        change -> {
          String newText = change.getControlNewText();
          if (newText.isEmpty() || newText.equals("-") || newText.equals(".")) return change;
          try {
            Double.parseDouble(newText);
            return change;
          } catch (NumberFormatException e) {
            return null;
          }
        }
    );

    setTextFormatter(formatter);
  }

  /**
   * Adds a listener to the value property of the TextFormatter.
   * @param listener the listener to add
   */
  @SuppressWarnings("unchecked")
  public void addListener(javafx.beans.value.ChangeListener<Double> listener) {
    ((TextFormatter<Double>) getTextFormatter()).valueProperty().addListener(listener);
  }
}
