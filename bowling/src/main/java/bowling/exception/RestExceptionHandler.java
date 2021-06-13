package bowling.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityExistsException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleResourceNotFound(Exception ex) {
        return new ResponseEntity<>(getErrorResponse(ex), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EntityExistsException.class})
    public ResponseEntity<ErrorResponse> handleEntityExistsException(Exception ex) {
        return new ResponseEntity<>(getErrorResponse(ex), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ApplicationException.class})
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex) {
        return new ResponseEntity<>(
                ex.getErrorResponse(),
                new HttpHeaders(),
                ex.getErrorResponse().getErrorCodes() != null
                        ? ex.getErrorResponse().getErrorCodes().getHttpStatusCode()
                        : HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ErrorResponse getErrorResponse(Exception ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
