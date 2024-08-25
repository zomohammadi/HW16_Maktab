package exceptions;

public class CreditCardExceptions {
    public static class NotFoundException extends RuntimeException {
        public NotFoundException() {
            super();
        }

        public NotFoundException(String message) {
            super(message);
        }

        public NotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class DatabaseException extends RuntimeException {
        public DatabaseException() {
            super();
        }

        public DatabaseException(String message) {
            super(message);
        }

        public DatabaseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
