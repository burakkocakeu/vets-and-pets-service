package eu.burakkocak.vetsandpetsservice.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public final class ErrorCodeItem implements ErrorCodeTemplate {
    private static final Map<String, ErrorCodeItem> enums = new ConcurrentSkipListMap<>();
    private static final Collection<ErrorCodeItem> unmodifiable;
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

    @JsonIgnore
    public static ErrorCodeItem valueOf(String name) {
        ErrorCodeItem code = (ErrorCodeItem) enums.get(name);
        if (code == null) {
            throw new IllegalArgumentException(String.format("No enum constant %s.%s", ErrorCodeTemplate.class.getName(), name));
        } else {
            return code;
        }
    }

    public static Collection<ErrorCodeItem> values() {
        return unmodifiable;
    }

    public Enum<?> getEnumValue() {
        return this.enumValue;
    }

    public String name() {
        return this.enumValue.name();
    }

    public String toString() {
        return this.enumValue.toString();
    }

    public boolean equals(Object  o) {
        return !(o instanceof ErrorCodeItem) ? false : this.enumValue.equals(((ErrorCodeItem)o).enumValue);
    }

    public int hashCode() {
        return this.enumValue.hashCode();
    }

    public HttpStatus httpStatus() {
        return ((IErrorCode)this.enumValue).httpStatus();
    }

    static {
        unmodifiable = Collections.unmodifiableCollection(enums.values());
    }
}
