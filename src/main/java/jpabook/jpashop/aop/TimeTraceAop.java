package jpabook.jpashop.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class TimeTraceAop {

    private static final Logger logger = LoggerFactory.getLogger(TimeTraceAop.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @Around("execution(* jpabook.jpashop..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime startTime = LocalDateTime.now();
        String formattedStartTime = startTime.format(formatter);
        String threadName = Thread.currentThread().getName();

        logMessage("INFO", formattedStartTime, threadName, "START: " + joinPoint.toString());

        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            LocalDateTime errorTime = LocalDateTime.now();
            String formattedErrorTime = errorTime.format(formatter);
            logMessage("ERROR", formattedErrorTime, threadName, "EXCEPTION in method: " + joinPoint.toString() + " - " + throwable.getMessage());
            throw throwable;
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            LocalDateTime endTime = LocalDateTime.now();
            String formattedEndTime = endTime.format(formatter);
            logMessage("INFO", formattedEndTime, threadName, "END: " + joinPoint.toString() + " " + timeMs + "ms");
        }
    }

    private void logMessage(String level, String timestamp, String threadName, String message) {
        String formattedMessage = String.format("%s %s %s --- [%s] : %s", timestamp, level, threadName, Thread.currentThread().getId(), message);
        if ("ERROR".equals(level)) {
            logger.error(formattedMessage);
        } else {
            logger.info(formattedMessage);
        }
    }
}
