package bowling.exception;

public class ErrorResponse {

    private ErrorCodes errorCodes;

    private String message;

    public ErrorResponse(ErrorCodes errorCodes) {
        this.errorCodes = errorCodes;
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(ErrorCodes errorCodes, String message) {
        this.errorCodes = errorCodes;
        this.message = message;
    }

    public ErrorCodes getErrorCodes() {
        return errorCodes;
    }

    public String getMessage() {
        return this.message;
    }
}
