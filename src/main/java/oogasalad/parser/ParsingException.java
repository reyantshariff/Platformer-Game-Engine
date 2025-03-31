package oogasalad.parser;

public class ParsingException extends Exception {

  /**
   * Creates a new ParsingException
   * @param message the detail message
   */
  public ParsingException(String message) {
    super(message);
  }

  /**
   * Creates a new ParsingException with a message and cause
   * @param message the message
   * @param cause the cause
   */
  public ParsingException(String message, Throwable cause) {
    super(message, cause);
  }
}