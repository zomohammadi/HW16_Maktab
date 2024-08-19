package exceptions;

public class StudentExceptions {
    public static class StudentNullPointerException extends RuntimeException {
        public StudentNullPointerException() {
            super();
        }

        public StudentNullPointerException(String message) {
            super(message);
        }

        public StudentNullPointerException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class StudentIllegalArgumentException extends RuntimeException {
        public StudentIllegalArgumentException() {
            super();
        }

        public StudentIllegalArgumentException(String message) {
            super(message);
        }

        public StudentIllegalArgumentException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class StudentEntityExistsException extends RuntimeException {
        public StudentEntityExistsException() {
            super();
        }

        public StudentEntityExistsException(String message) {
            super(message);
        }

        public StudentEntityExistsException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class StudentTransactionRequiredException extends RuntimeException {
        public StudentTransactionRequiredException() {
            super();
        }

        public StudentTransactionRequiredException(String message) {
            super(message);
        }

        public StudentTransactionRequiredException(String message, Throwable cause) {
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

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException() {
            super();
        }

        public UserNotFoundException(String message) {
            super(message);
        }

        public UserNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException() {
            super();
        }

        public InvalidCredentialsException(String message) {
            super(message);
        }

        public InvalidCredentialsException(String message, Throwable cause) {
            super(message, cause);
        }
    }


}
