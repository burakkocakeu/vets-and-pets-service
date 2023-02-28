package eu.burakkocak.vetsandpetsservice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TrackExecutionTimeAspect {
    @Around("@annotation(eu.burakkocak.vetsandpetsservice.aop.TrackExecutionTime)")
    public Object trackAndLogExecutionTime(ProceedingJoinPoint point) throws Throwable {
        String methodName = point.getSignature().getName();
        String className = point.getTarget().getClass().getSimpleName();

        Long execTimeBegin = System.currentTimeMillis();
        Object o = point.proceed();
        Long execTimeEnd = System.currentTimeMillis();
        log.debug("{}.{}() executed in {} milliseconds...", className, methodName, (execTimeEnd - execTimeBegin));

        return o;
    }
}
