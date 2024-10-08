package org.project.multilogin.advice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.project.multilogin.model.LogModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Arrays;

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
        Object[] args = proceedingJoinPoint.getArgs();

        logEntry(className, methodName, args);

        Object result = proceedingJoinPoint.proceed();

        logExit(className, methodName, result);

        return result;
    }

    private void logEntry(String className, String methodName, Object[] args) {
        try {
            LogModel entryLogModel = new LogModel(className, methodName, args);
            logger.info("Log Entry: {}", objectMapper.writeValueAsString(entryLogModel));
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize entry log for {}.{}: {}", className, methodName, e.getMessage());
            logger.info("Log Entry: Class: {}, Method: {}, Args: {}", className, methodName, Arrays.toString(args));
        }
    }

    private void logExit(String className, String methodName, Object result) {
        try {
            if (result != null) {
                LogModel exitLogModel = new LogModel(className, methodName, result);
                logger.info("Log Exit: {}", objectMapper.writeValueAsString(exitLogModel));
            } else {
                logger.info("Log Exit: {} - {}: method returned void or null", className, methodName);
            }
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize exit log for {}.{}: {}", className, methodName, e.getMessage());
            logger.info("Log Exit: Class: {}, Method: {}, Result: {}", className, methodName, result);
        }
    }



    @AfterThrowing(pointcut = "logPointCut()", throwing = "exception")
    public void logExceptions(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        LogModel exceptionLogModel = new LogModel(className, methodName, args);
        logger.error("Log Global Exception in {} - {}: {}", className, methodName, exception.getMessage());
        try {
            logger.error("Exception details: {}", toJson(exceptionLogModel), exception);
        } catch (Exception e) {
            logger.error("Failed to serialize exception log for {}.{}: {}", className, methodName, e.getMessage());
            logger.info("Log Exception : Class: {}, Method: {}, Result: {}", className, methodName, args);
        }
    }


    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("JSON processing error: {}", e.getMessage());
            return "Unable to serialize to JSON";  // Fallback message in case of failure
        }
    }
}