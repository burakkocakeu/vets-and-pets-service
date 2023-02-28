package eu.burakkocak.vetsandpetsservice.exception;

import eu.burakkocak.vetsandpetsservice.exception.base.ErrorCodeItem;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ServiceException extends RuntimeException implements Serializable {
    private final ErrorCodeItem errorCode;
    private final List params;

    public ServiceException(ErrorCodeItem errorCode) {
        this.errorCode = errorCode;
        this.params = new ArrayList();
    }

    public ServiceException(ErrorCodeItem errorCode, List params) {
        this.errorCode = errorCode;
        this.params = params;
    }
}
