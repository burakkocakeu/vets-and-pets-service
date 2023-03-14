package eu.burakkocak.vetsandpetsservice.exception.base;

import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public final class ErrorCodeItem {
    private static final Map<String, ErrorCodeItem> enums = new ConcurrentSkipListMap<>();
    private final Enum<?> enumValue;

    private <T extends Enum<T> & IErrorCode> ErrorCodeItem(T enumValue) {
        this.enumValue = enumValue;
        String name = enumValue.name();
        if (enums.put(name, this) != null) {
            throw new IllegalArgumentException(String.format("Duplicate error code '%s'", name));
        }
    }

    public static <T extends Enum<T> & IErrorCode> ErrorCodeItem of(T enumInstance) {
        return new ErrorCodeItem(enumInstance);
    }

    public String name() {
        return this.enumValue.name();
    }

    public String toString() {
        return this.enumValue.toString();
    }

    public boolean equals(Object  o) {
        return o instanceof ErrorCodeItem && this.enumValue.equals(((ErrorCodeItem) o).enumValue);
    }

    public int hashCode() {
        return this.enumValue.hashCode();
    }

    public HttpStatus httpStatus() {
        return ((IErrorCode)this.enumValue).httpStatus();
    }
}
