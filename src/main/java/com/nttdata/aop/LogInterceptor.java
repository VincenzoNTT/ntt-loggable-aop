package com.nttdata.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
public class LogInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogInterceptor.class);

	/** */
	final StopWatch stopWatch = new StopWatch();
	
	
	@Before(value = "target(bean) && @annotation(Loggable) && @annotation(logme)", argNames = "bean,logme")
	public void logBefore(JoinPoint jp, Object bean, Loggable logme) {
		stopWatch.start();
		LOGGER.debug(String.format("Log Message: %s", logme.message()));
		LOGGER.debug(String.format("Method Called: %s", jp.getSignature().getName()));

	}

	@After(value = "target(bean) && @annotation(Loggable) && @annotation(logme)", argNames = "bean,logme")
	public void logAfter(JoinPoint jp, Object bean, Loggable logme) {

		if (LOGGER.isDebugEnabled()) {
			stopWatch.stop();
 			LOGGER.debug("Method terminated - Execution time : " + stopWatch.getTotalTimeMillis() + " ms");
			LOGGER.debug("******************************************************************************");
		}
 	}

	@Around(value = "@annotation(Loggable)")
	public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
		LOGGER.debug("******************************************************************************");

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
					joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
		}

		try {
			Object result = joinPoint.proceed();
		
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
						joinPoint.getSignature().getName(), result);
			}
			return result;
		} catch (IllegalArgumentException e) {
			stopWatch.stop();
 			LOGGER.debug("Method with error - Execution time : " + stopWatch.getTotalTimeMillis() + " ms");
			LOGGER.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
					joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
			throw e;
		}
	}

	@AfterThrowing(value = "@annotation(Loggable)", throwing = "error")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
		LOGGER.error("An exception has been thrown in " + joinPoint.getSignature().getName() + " ()");
		LOGGER.error("Cause : " + error.getCause());
	}
}
