package eu.burakkocak.vetsandpetsservice.exception.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.common.util.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class ErrorResponse implements Serializable {
    private String errorCode;
    private String message;
    @JsonIgnore
    private List params;
    private static MessageSourceAccessor messages;

    public ErrorResponse(String errorCode, List args) {
        this.errorCode = errorCode;
        this.message = getMessage(errorCode, args);
        this.params = args;
    }

    public ErrorResponse(String errorCode, String message, List args) {
        this.errorCode = errorCode;
        this.message = StringUtils.isNotBlank(message) ? message : getMessage(errorCode, args);
        this.params = args;
    }

    private static String getMessage(String errorCode, List args) {
        messageSourceAccessor();

        try {
            return messages.getMessage(String.format("error.%s", errorCode), args.toArray());
        } catch (Exception e) {
            return messages.getMessage(String.format("error.%s", "DEFAULT"));
        }
    }

    private static MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(new String[]{"classpath:messages", "classpath:messages-default"});
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    private static void messageSourceAccessor() {
        if (Objects.isNull(messages)) {
            messages = new MessageSourceAccessor(messageSource());
        }
    }
}
