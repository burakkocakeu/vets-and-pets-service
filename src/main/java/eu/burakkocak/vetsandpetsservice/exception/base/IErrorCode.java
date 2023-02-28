package eu.burakkocak.vetsandpetsservice.exception.base;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public interface IErrorCode extends Serializable {
    String name();
    HttpStatus httpStatus();
}
