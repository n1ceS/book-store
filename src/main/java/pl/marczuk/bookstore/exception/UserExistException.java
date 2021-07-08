package pl.marczuk.bookstore.exception;

public final class UserExistException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    private String field;
    public UserExistException() {
        super();
    }

    public UserExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserExistException(final String message, String field) {
        super(message);
        this.field = field;
    }

    public UserExistException(final Throwable cause) {
        super(cause);
    }

    public String getField() {
        return field;
    }
}
