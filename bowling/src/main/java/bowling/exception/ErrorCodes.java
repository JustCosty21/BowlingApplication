package bowling.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCodes {
    UNDEFINED(-1),

    //server error (5xx)
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value()),

    //403 forbidden
    FORBIDDEN(HttpStatus.FORBIDDEN.value()),

    //400 bad request
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value());

    private int httpStatusCode;

    ErrorCodes(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
