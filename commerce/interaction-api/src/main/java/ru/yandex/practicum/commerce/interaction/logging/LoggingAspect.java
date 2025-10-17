package ru.yandex.practicum.commerce.interaction.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("@annotation(ru.yandex.practicum.commerce.interaction.logging.Loggable)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Entering method: {}", joinPoint.getSignature());

        // Log request parameters
        Object[] args = joinPoint.getArgs();
        log.info("Request Parameters: {}", args);

        Object result = joinPoint.proceed(); // Proceed with method execution

        // Log response
        log.info("Exiting method: {} - Response: {}", joinPoint.getSignature(), result);

        return result;
    }
}

