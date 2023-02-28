package eu.burakkocak.vetsandpetsservice.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingRestControllersAspect {
    private final ObjectMapper MAPPER;

    @Pointcut(value = "execution(* eu.burakkocak.vetsandpetsservice.api.controller.PetController.*(..))")
    public void controllersPointcut() {
    }

    @Around("controllersPointcut()")
    public Object controllersLogger(ProceedingJoinPoint point) throws Throwable {
        String methodName = point.getSignature().getName();
        String className = point.getTarget().getClass().getName();

        Object[] args = point.getArgs();
        log.debug("Incoming message to {}() at {} : {}", methodName, className, MAPPER.writeValueAsString(args));
        Object o = point.proceed();
        log.debug("Outgoing message from {}() at {} : {}", methodName, className, MAPPER.writeValueAsString(o));

        return o;
    }
}
