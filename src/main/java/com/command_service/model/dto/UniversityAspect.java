package com.command_service.model.dto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UniversityAspect {

    private final Logger LOG = LogManager.getLogger(UniversityAspect.class);

    @Before("execution(* com.command_service.service.UniversityService.getUniversityById(..))")
    public void logBeforeUniversityById(JoinPoint joinPoint) {
        LOG.info("ASPECT WAS READ BEFORE logBeforeUniversityById");
    }

    @After("execution(* com.command_service.service.UniversityService.getUniversityById(..))")
    public void logAfterUniversityById(JoinPoint joinPoint) {
        LOG.info("ASPECT WAS READ AFTER: logAfterUniversityById");
    }

    @Before("execution(* com.command_service.service.UniversityService.*(..))")
    public void logBeforeUniversityGetAll(JoinPoint joinPoint)
    {
        LOG.info("ASPECT WAS READ BEFORE - logBeforeUniversityGetAll");
    }

    @After("execution(* com.command_service.service.UniversityService.*(..))")
    public void logAfterUniversityGetAll(JoinPoint joinPoint)
    {
        LOG.info("ASPECT WAS READ AFTER - logAfterUniversityGetAll");
    }
}
