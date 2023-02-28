package eu.burakkocak.vetsandpetsservice.exception.base;

import java.util.Collection;

public interface ErrorCodeTemplate extends IErrorCode {
    static Collection<ErrorCodeItem> values() {
        return ErrorCodeItem.values();
    }
}
