package bowling.exception;

public class ApplicationException extends RuntimeException {

    private ErrorResponse errorResponse;

    public ApplicationException() {
        super();
    }

    public ApplicationException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }

    public ApplicationException(ErrorResponse errorResponse, Throwable cause) {
        super(errorResponse.getMessage(), cause);
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
