package exceptions;

public class CityExceptions {
    public static class CityNotFoundException extends RuntimeException {
        public CityNotFoundException() {
            super();
        }

        public CityNotFoundException(String message) {
            super(message);
        }

        public CityNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class CityDatabaseException extends RuntimeException {
        public CityDatabaseException() {
            super();
        }

        public CityDatabaseException(String message) {
            super(message);
        }

        public CityDatabaseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
