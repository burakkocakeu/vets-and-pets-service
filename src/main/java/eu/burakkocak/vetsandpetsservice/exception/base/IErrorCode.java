package eu.burakkocak.vetsandpetsservice.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public interface IErrorCode extends Serializable {
    String name();
    HttpStatus httpStatus();
    default boolean passThrow() {
        return false;
    }
}
