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
	//	@Around("@annotation(com.ahmed.learning.jobportal.aspects.LogAspect)")
	@Around("execution(* com.ahmed.learning.jobportal..*.*(..))")
	public Object logAndMeasureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		String methodName = joinPoint.getSignature().toShortString();
		Object[] args = joinPoint.getArgs();
		log.info("-> Entering Method: {}", methodName);
		log.info("-> Arguments: {}", args);

		Object result = joinPoint.proceed();
		long executionTime = System.currentTimeMillis() - startTime;
		log.info("-> Method executed successfully: {}", methodName);
		log.info("-> Execution time: {}", executionTime);

		return result;
	}
}
