package org.project.multilogin.advice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAdvice {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAdvice.class);
    private static final ObjectMapper objectMapper= new ObjectMapper();

    @Pointcut("execution(* org.project.multilogin.*.*.*(..))")
    public void logPointCut(){}

    @Around("logPointCut()")
    public Object logMethodExecutionEntryAndExit(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodName = proceedingJoinPoint.getSignature().getName();
        String className = proceedingJoinPoint.getTarget().getClass().getSimpleName();
        Object[] arguments = proceedingJoinPoint.getArgs();

        logEntry(className, methodName, arguments);

        Object result = proceedingJoinPoint.proceed();

        logExit(className, methodName, result);

        return result;
    }

    private void logEntry(String className, String methodName, Object[] arguments) {
        try {
            logger.info("Log Entry: Class: {}, Method: {}, Args: {}", className, methodName, objectMapper.writeValueAsString(arguments));
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize entry log for Class: {}, Method: {}, Error Message: {}", className, methodName, e.getMessage());
        }
    }

    private void logExit(String className, String methodName, Object result) {
        try {
            if (result != null) {
                logger.info("Log Exit: Class: {}, Method: {}, Args: {}", className, methodName, objectMapper.writeValueAsString(result));
            } else {
                logger.info("Log Exit: Class: {}, Method: {}: method returned void or null", className, methodName);
            }
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize exit log for Class: {}, Method: {}, Exception Message: {}", className, methodName, e.getMessage());
        }
    }

    @AfterThrowing(pointcut = "logPointCut()", throwing = "exception")
    public void logExceptions(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] arguments = joinPoint.getArgs();

        logger.error("Global Exception Log in - Class: {}, Method: {}, Exception Message: {}, Args: {}", className, methodName, exception.getMessage(), arguments);
    }
}