package ca.uqam.projet.exceptions;

public class BadRequestParamException extends Exception {

    public BadRequestParamException(String message) {
        super(message);
    }

    public BadRequestParamException(String message, Throwable throwable) {
        super(message, throwable);
    }

    @Override
    public String getMessage() {
        return String.format("Parameter error: %s", super.getMessage());
    }
}
