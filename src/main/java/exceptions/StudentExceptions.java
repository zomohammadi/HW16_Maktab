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
}