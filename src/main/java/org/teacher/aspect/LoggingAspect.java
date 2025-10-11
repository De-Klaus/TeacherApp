package org.teacher.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.teacher.dto.UserCredentialsDto;
import org.teacher.dto.request.UserRequestDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* org.teacher.service..*(..))")
    public void serviceLayer() {}

    @Before("serviceLayer()")
    public void logMethodEntry(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
        Object[] args = joinPoint.getArgs();

        log.info("➡️ Entering: {} with args {}", methodName, Arrays.toString(args));
    }

    @AfterReturning(value = "serviceLayer()", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();

        log.info("✅ Exiting: {} with result {}", methodName, result);
    }

    @AfterThrowing(value = "serviceLayer()", throwing = "ex")
    public void logMethodException(JoinPoint joinPoint, Throwable ex) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();

        log.error("❌ Exception in {}: {}", methodName, ex.getMessage(), ex);
    }

    @Around("execution(* org.teacher.service..*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();

        Object[] args = joinPoint.getArgs();
        List<Object> logArgs = new ArrayList<>();
        for (Object arg : args) {
            if (arg instanceof UserCredentialsDto || arg instanceof UserRequestDto) {
                logArgs.add("[CREDENTIALS HIDDEN]");
            } else {
                logArgs.add(arg);
            }
        }

        log.info("➡️ Entering: {} with args {}", methodName, logArgs);

        Object result;
        try {
            long start = System.currentTimeMillis();
            result = joinPoint.proceed();
            long end = System.currentTimeMillis();
            log.info("✅ Exiting: {} with result {} ({} ms)", methodName, result, end - start);
        } catch (Throwable e) {
            log.error("❌ Exception in {}: {}", methodName, e.getMessage());
            throw e;
        }
        return result;
    }
}
