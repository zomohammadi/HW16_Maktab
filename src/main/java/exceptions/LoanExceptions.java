package exceptions;

public class LoanExceptions {
    public static class NotFoundException extends RuntimeException {

        public NotFoundException(String message) {
            super(message);
        }

        public NotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
