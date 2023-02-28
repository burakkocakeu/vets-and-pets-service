package eu.burakkocak.vetsandpetsservice.core;

import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
public class ContextHolder {
    private static final ThreadLocal<Context> THREAD_LOCAL = new InheritableThreadLocal<>();

    public static Context get() {
        return Objects.isNull(THREAD_LOCAL.get()) ? Context.builder().build() : THREAD_LOCAL.get();
    }

    public static void set(Context context) {
        THREAD_LOCAL.set(context);
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }
}
