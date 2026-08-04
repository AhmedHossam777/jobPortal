package com.ahmed.learning.jobportal.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAndPerformanceAspect {
	@Around("execution(* com.ahmed.learning.jobportal..*.*(..))")
	public Object logAndMeasureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		String methodName = joinPoint.getSignature().toShortString();
		Object[] args = joinPoint.getArgs();
		log.debug("-> Entering Method: {}", methodName);
		log.debug("-> Arguments: {}", args);

		Object result = joinPoint.proceed();
		long executionTime = System.currentTimeMillis() - startTime;
		log.debug("-> Method executed successfully: {}", methodName);
		log.debug("-> Execution time: {}", executionTime);

		return result;
	}
}
