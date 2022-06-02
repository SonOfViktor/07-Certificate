package com.epam.esm.aspect;

import com.epam.esm.entity.MethodMetadata;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
@Aspect
public class ServiceLoggingAspect {
private static final Logger logger = LogManager.getLogger();

    @Pointcut("execution(int com.epam.esm.service.*..update*(..))")
    public void updateServiceMethodPointcut() {}

    @Pointcut("execution(int com.epam.esm.service.*..delete*(int))")
    public void deleteServiceMethodPointcut() {}

    @Pointcut("execution(* com.epam.esm.service.*.GiftCertificateTagDtoServiceImpl.find*(..))")
    public void findDtoServiceMethodPointcut() {}

    @Pointcut("execution(int com.epam.esm.service.*..add*(..))")
    public void addServiceMethodPointcut() {}

    @Pointcut("execution(int[] com.epam.esm.service.*..*(..))")
    public void serviceMethodReturnIntArrayPointcut() {}

    @Pointcut("execution(* com.epam.esm.service.impl.*.*(..))")
    public void allServiceMethodPointcut() {}

    @Pointcut("execution(* com.epam.esm.builder.SelectSqlBuilder.buildSelectGiftCertificateSQL(..))")
    public void buildSelectGiftCertificateSQLPointcut() {}

    @AfterReturning(pointcut = "updateServiceMethodPointcut() || deleteServiceMethodPointcut()", returning = "affectedRow")
    public void logUpdateDeleteServiceMethod(JoinPoint joinPoint, int affectedRow) {
        MethodMetadata methodMetadata = takeMethodMetadata(joinPoint);

        logger.log(Level.INFO, "Method {}() from class {} worked successfully. {} affected row",
                methodMetadata.methodName(), methodMetadata.className(), affectedRow);
    }

    @AfterReturning(pointcut = "findDtoServiceMethodPointcut()", returning = "result")
    public void logFindDtoServiceMethod(JoinPoint joinPoint, Object result) {
        MethodMetadata methodMetadata = takeMethodMetadata(joinPoint);
        logger.log(Level.INFO, "Method {}() from class {} returned {}",
                methodMetadata.methodName(), methodMetadata.className(), result);
    }

    @AfterReturning(pointcut = "addServiceMethodPointcut()", returning = "id")
    public void logAddServiceMethod(JoinPoint joinPoint, int id) {
        MethodMetadata methodMetadata = takeMethodMetadata(joinPoint);

        logger.log(Level.INFO, "Method {}() from class {} returned id {}",
                methodMetadata.methodName(), methodMetadata.className(), id);
    }

    @AfterReturning(pointcut = "serviceMethodReturnIntArrayPointcut()", returning = "affectedRows")
    public void logMethodReturnAffectedRows(JoinPoint joinPoint, int[] affectedRows) {
        MethodMetadata methodMetadata = takeMethodMetadata(joinPoint);

        logger.log(Level.INFO, "Method {}() from class {} returned array {}",
                methodMetadata.methodName(), methodMetadata.className(), Arrays.toString(affectedRows));
    }

    @AfterReturning(pointcut = "buildSelectGiftCertificateSQLPointcut()", returning = "selectQuerySql")
    public void logBuildSelectGiftCertificateSQL(JoinPoint joinPoint, String selectQuerySql) {
        MethodMetadata methodMetadata = takeMethodMetadata(joinPoint);

        logger.log(Level.INFO, "Method {}() from class {} returned \n{}",
                methodMetadata.methodName(), methodMetadata.className(), selectQuerySql);
    }

    @AfterThrowing(pointcut = "allServiceMethodPointcut()", throwing = "exception")
    public void logExceptionAllServiceMethod(JoinPoint joinPoint, Exception exception) {
        MethodMetadata methodMetadata = takeMethodMetadata(joinPoint);
        logger.log(Level.ERROR, "Method {}() from class {} thrown exception {}",
                methodMetadata.methodName(), methodMetadata.className(), exception);
    }

    private MethodMetadata takeMethodMetadata(JoinPoint joinPoint) {
        Signature methodSignature = joinPoint.getSignature();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        return new MethodMetadata(className, methodName);
    }
}
