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

@Aspect
@Component
public class LoggingAdvice {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAdvice.class);

    @Pointcut("execution(* org.project.multilogin.*.*.*(..))")
    public void logPointCut(){}

    ObjectMapper objectMapper= new ObjectMapper();


    @Around("logPointCut()")
    public Object logMethodExecutionEntryAndExit(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodName = proceedingJoinPoint.getSignature().getName();
        String className = proceedingJoinPoint.getTarget().getClass().toString();
        Object[] args = proceedingJoinPoint.getArgs();

        LogModel entryLogModel = new LogModel(className, methodName, args);
        logger.info("Log Entry: {}", objectMapper.writeValueAsString(entryLogModel));

        Object  object = proceedingJoinPoint.proceed();

        LogModel exitLogModel = new LogModel(className, methodName, object);
        logger.info("Log Exit: {}", objectMapper.writeValueAsString(exitLogModel));

        return object;
    }

    @AfterThrowing(pointcut = "logPointCut()", throwing = "exception")
    public void logExceptions(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        LogModel exceptionLogModel = new LogModel(className, methodName, args);
        logger.error("Global Exception in {} - {}: {}", className, methodName, exception.getMessage());
        logger.error("Exception details: {}", toJson(exceptionLogModel), exception);
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