package oogasalad.model.builder;

public class loadGameException extends RuntimeException {
    /**
     * Constructs a new loadGameException with the specified detail message.
     *
     * @param message the detail message
     */
    public loadGameException(String message) {
        super(message);
    }

    /**
     * Constructs a new loadGameException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public loadGameException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
