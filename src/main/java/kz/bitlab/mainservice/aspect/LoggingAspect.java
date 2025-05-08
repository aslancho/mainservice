package kz.bitlab.mainservice.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* kz.bitlab.mainservice.controller.*.*(..))")
    public void logBeforeControllerMethods(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());

        Map<String, Object> parameters = new HashMap<>();
        Object[] args = joinPoint.getArgs();
        Parameter[] params = method.getParameters();

        for (int i = 0; i < params.length; i++) {
            if (i >= args.length) continue;

            Parameter param = params[i];
            Object value = args[i];

            if (value instanceof MultipartFile || value instanceof HttpServletRequest) continue;

            String paramName = param.getName();
            PathVariable pv = param.getAnnotation(PathVariable.class);
            if (pv != null && !pv.value().isEmpty()) paramName = pv.value();

            parameters.put(paramName, value);
        }

        logger.info("\u001B[1;34m✨✨ [LOGGING ASPECT] {}.{} params = {}\u001B[0m", className, methodName, parameters);
    }

}