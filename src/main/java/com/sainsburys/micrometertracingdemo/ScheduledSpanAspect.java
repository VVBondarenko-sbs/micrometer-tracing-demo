package com.sainsburys.micrometertracingdemo;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ScheduledSpanAspect {
  private final Tracer tracer;

  @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
  public void annotatedWithScheduled() {}

  @Around("annotatedWithScheduled()")
  public Object wrapScheduledIntoSpan(ProceedingJoinPoint pjp) throws Throwable {
    var methodName = pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();

    var span = tracer.nextSpan()
        .name(methodName)
        .start();
    try (var ignoredSpanInScope = tracer.withSpan(span.start())) {
      return pjp.proceed();
    } finally {
      span.end();
    }
  }
}
