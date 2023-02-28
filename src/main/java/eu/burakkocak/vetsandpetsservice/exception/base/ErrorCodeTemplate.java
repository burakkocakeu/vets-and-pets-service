package eu.burakkocak.vetsandpetsservice.exception;

import java.util.Collection;

public interface ErrorCodeTemplate extends IErrorCode {
    static ErrorCodeItem valueOf(String name) {
        return ErrorCodeItem.valueOf(name);
    }
    static Collection<ErrorCodeItem> values() {
        return ErrorCodeItem.values();
    }
}
