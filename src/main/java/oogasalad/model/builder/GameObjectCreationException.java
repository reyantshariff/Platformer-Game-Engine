package oogasalad.model.builder;

public class GameObjectCreationException extends RuntimeException {
  public GameObjectCreationException(String message) {
    super(message);
  }

  public GameObjectCreationException(String message, Throwable cause) {
    super(message, cause);
  }
    
}
