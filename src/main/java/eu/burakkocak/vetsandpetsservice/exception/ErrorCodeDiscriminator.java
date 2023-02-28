package eu.burakkocak.vetsandpetsservice.exception;

import eu.burakkocak.vetsandpetsservice.exception.base.IErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ErrorCodeDiscriminator implements IErrorCode {
    UNAUTHORIZED_REQUEST(HttpStatus.FORBIDDEN),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST),
    PET_NOT_FOUND(HttpStatus.BAD_REQUEST);

    private final HttpStatus httpStatus;

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }
}
