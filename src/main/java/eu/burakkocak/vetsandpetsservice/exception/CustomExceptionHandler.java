package eu.burakkocak.vetsandpetsservice.exception;

import eu.burakkocak.vetsandpetsservice.exception.base.ErrorCodeItem;
import eu.burakkocak.vetsandpetsservice.exception.base.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> serviceExceptionHandler(ServiceException e) {
        log.debug("ServiceException occurred: {}", e.getErrorCode(), e);
        return new ResponseEntity<>(new ErrorResponse(e.getErrorCode().name(), e.getParams()), e.getErrorCode().httpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        ErrorCodeItem validationError = ErrorCode.VALIDATION_ERROR;
        String message = String.format("%s: %s", e.getFieldError().getDefaultMessage(), e.getFieldError().getField());
        log.debug("MethodArgumentNotValidException occurred: {}", validationError, e);
        return new ResponseEntity<>(new ErrorResponse(validationError.name(), List.of(message)), validationError.httpStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        log.debug("HttpMessageNotReadableException occurred: ", e);
        return new ResponseEntity<>(new ErrorResponse(null, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCredentialsExceptionHandler(BadCredentialsException e) {
        log.debug("BadCredentialsException occurred: ", e);
        return new ResponseEntity<>(new ErrorResponse(null, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {
        log.debug("Unexpected exception occurred: ", e);
        return new ResponseEntity<>(new ErrorResponse(null, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
