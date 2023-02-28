package eu.burakkocak.vetsandpetsservice.exception;

import eu.burakkocak.vetsandpetsservice.exception.base.ErrorCodeItem;
import eu.burakkocak.vetsandpetsservice.exception.base.IErrorCode;

public interface ErrorCode extends IErrorCode {
    ErrorCodeItem UNAUTHORIZED_REQUEST = ErrorCodeItem.of(ErrorCodeDiscriminator.UNAUTHORIZED_REQUEST);
    ErrorCodeItem VALIDATION_ERROR = ErrorCodeItem.of(ErrorCodeDiscriminator.VALIDATION_ERROR);
    ErrorCodeItem USER_NOT_FOUND = ErrorCodeItem.of(ErrorCodeDiscriminator.USER_NOT_FOUND);
    ErrorCodeItem PET_NOT_FOUND = ErrorCodeItem.of(ErrorCodeDiscriminator.PET_NOT_FOUND);
}
