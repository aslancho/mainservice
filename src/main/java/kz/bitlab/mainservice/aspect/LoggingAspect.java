package kz.bitlab.mainservice.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* kz.bitlab.mainservice.controller.*.*(..))")
    public void logBeforeControllerMethods(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());

        Map<String, Object> parameters = new HashMap<>();
        Object[] args = joinPoint.getArgs();

        try {
            Parameter[] params = joinPoint.getTarget().getClass()
                    .getMethod(methodName, joinPoint.getSignature().getDeclaringType()
                            .getMethod(methodName, getParameterTypes(args)).getParameterTypes())
                    .getParameters();

            for (int i = 0; i < params.length; i++) {
                if (i < args.length) {
                    // Для PathVariable извлекаем имя из аннотации
                    PathVariable pathVariable = params[i].getAnnotation(PathVariable.class);
                    String paramName = (pathVariable != null && !pathVariable.value().isEmpty())
                            ? pathVariable.value()
                            : (pathVariable != null) ? params[i].getName() : params[i].getName();
                    parameters.put(paramName, args[i]);
                }
            }

        } catch (Exception e) {
            // Если не удалось получить имена параметров, просто пронумеруем их
            for (int i = 0; i < args.length; i++) {
                parameters.put("arg" + i, args[i]);
            }
        }

        logger.info("{}.{} called with parameters: {}", className, methodName, parameters);
    }

    private Class<?>[] getParameterTypes(Object[] args) {
        Class<?>[] types = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            types[i] = args[i] != null ? args[i].getClass() : Object.class;
        }
        return types;
    }
}