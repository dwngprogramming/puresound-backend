package com.puresound.backend.config.audit;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UnauthenticatedAuditorAspect {
    SpelExpressionParser parser = new SpelExpressionParser();
    static final String ANONYMOUS_USER = "ANONYMOUS";

    @Around("@annotation(unauthenticatedAuditor)")
    public Object handleUnauthenticatedAuditor(ProceedingJoinPoint joinPoint,
                                               UnauthenticatedAuditor unauthenticatedAuditor) throws Throwable {
        String auditorEmail;
        try {
            auditorEmail = resolveEmail(joinPoint, unauthenticatedAuditor);
            UnauthenticatedAuditorContextHolder.setCurrentAuditor(auditorEmail);
            return joinPoint.proceed();
        } finally {
            UnauthenticatedAuditorContextHolder.clear();
        }
    }

    private String resolveEmail(ProceedingJoinPoint joinPoint, UnauthenticatedAuditor annotation) {
        String email = annotation.email();
        return email.matches("^#.*") ?
                evaluateSpelExpression(joinPoint, email) :
                email;
    }

    private String evaluateSpelExpression(ProceedingJoinPoint joinPoint, String spelExpression) {
        try {
            StandardEvaluationContext context = createEvaluationContext(joinPoint);
            Expression expression = parser.parseExpression(spelExpression);
            Object result = expression.getValue(context);
            return result != null ? result.toString() : ANONYMOUS_USER;
        } catch (Exception e) {
            log.warn("Failed to evaluate SpEL expression: {}", spelExpression, e);
            return ANONYMOUS_USER;
        }
    }

    private StandardEvaluationContext createEvaluationContext(ProceedingJoinPoint joinPoint) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] paramValues = joinPoint.getArgs();

        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], paramValues[i]);
        }
        return context;
    }
}
