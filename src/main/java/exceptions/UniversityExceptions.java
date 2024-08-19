package exceptions;

public class UniversityExceptions {
    public static class NullPointerException extends RuntimeException {
        public NullPointerException() {
            super();
        }

        public NullPointerException(String message) {
            super(message);
        }

        public NullPointerException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InvalidArgumentException extends RuntimeException {
        public InvalidArgumentException() {
            super();
        }

        public InvalidArgumentException(String message) {
            super(message);
        }

        public InvalidArgumentException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class EntityExistsException extends RuntimeException {
        public EntityExistsException() {
            super();
        }

        public EntityExistsException(String message) {
            super(message);
        }

        public EntityExistsException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class TransactionRequiredException extends RuntimeException {
        public TransactionRequiredException() {
            super();
        }

        public TransactionRequiredException(String message) {
            super(message);
        }

        public TransactionRequiredException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class DatabaseAccessException extends RuntimeException {
        public DatabaseAccessException() {
            super();
        }

        public DatabaseAccessException(String message) {
            super(message);
        }

        public DatabaseAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }

        public NotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
