package gg.drak.thebase.events.processing.exception;

public class BaseEventException extends Exception {
    private static final long serialVersionUID = 3532808232324183999L;
    private final Throwable cause;

    /**
     * Constructs a new EventException based on the given Exception
     *
     * @param throwable Exception that triggered this Exception
     */
    public BaseEventException(Throwable throwable) {
        cause = throwable;
    }

    /**
     * Constructs a new EventException
     */
    public BaseEventException() {
        cause = null;
    }

    /**
     * Constructs a new EventException with the given message
     *
     * @param cause The exception that caused this
     * @param message The message
     */
    public BaseEventException(Throwable cause, String message) {
        super(message);
        this.cause = cause;
    }

    /**
     * Constructs a new EventException with the given message
     *
     * @param message The message
     */
    public BaseEventException(String message) {
        super(message);
        cause = null;
    }

    /**
     * If applicable, returns the Exception that triggered this Exception
     *
     * @return Inner exception, or null if one does not exist
     */
    @Override
    public Throwable getCause() {
        return cause;
    }
}
