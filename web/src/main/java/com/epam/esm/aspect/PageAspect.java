package com.epam.esm.aspect;

import com.epam.esm.entity.Page;
import com.epam.esm.entity.SelectQueryParameter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
@Aspect
public class PageAspect {

    public static final String PREVIOUS = "previous";
    public static final String FIRST = "first";
    public static final String NEXT = "next";
    public static final String LAST = "last";

    @Pointcut("execution(*..Page<*> com.epam.esm.controller.*.*(..))")
    public void showPageMethodPointcut() {
    }

    @AfterReturning(pointcut = "showPageMethodPointcut() && args(page, size)", returning = "result",
            argNames = "joinPoint, page, size, result")
    public void addPaginationRefToPage(JoinPoint joinPoint, Integer page, Integer size, Page<?> result) throws NoSuchMethodException {
        Class<?> controllerClass = joinPoint.getSignature().getDeclaringType();
        Method controllerMethod = controllerClass.getMethod(joinPoint.getSignature().getName(), Integer.class, Integer.class);

        result.add(linkTo(controllerClass, controllerMethod, page, size).withSelfRel())
                .add(linkTo(controllerClass, controllerMethod, 1, size).withRel(FIRST))
                .addIf(page != 1,
                        () -> linkTo(controllerClass, controllerMethod, page - 1, size).withRel(PREVIOUS))
                .addIf(page < result.getPageMeta().getTotalPages(),
                        () -> linkTo(controllerClass, controllerMethod, page + 1, size).withRel(NEXT))
                .add(linkTo(controllerClass, controllerMethod, result.getPageMeta().getTotalPages(), size).withRel(LAST));

    }

    @AfterReturning(pointcut = "showPageMethodPointcut() && args(id, page, size)", returning = "result",
            argNames = "joinPoint, id, page, size, result")
    public void addPaginationRefToPageById(JoinPoint joinPoint, Integer id, Integer page,
                                           Integer size, Page<?> result) throws NoSuchMethodException {
        Class<?> controllerClass = joinPoint.getSignature().getDeclaringType();
        Method controllerMethod = controllerClass.getMethod(
                joinPoint.getSignature().getName(), Integer.class, Integer.class, Integer.class);

        result.add(linkTo(controllerClass, controllerMethod, id, page, size).withSelfRel())
                .add(linkTo(controllerClass, controllerMethod, id, 1, size).withRel(FIRST))
                .addIf(page != 1,
                        () -> linkTo(controllerClass, controllerMethod, id, page - 1, size).withRel(PREVIOUS))
                .addIf(page < result.getPageMeta().getTotalPages(),
                        () -> linkTo(controllerClass, controllerMethod, id, page + 1, size).withRel(NEXT))
                .add(linkTo(controllerClass, controllerMethod, id, result.getPageMeta().getTotalPages(), size).withRel(LAST));

    }

    @AfterReturning(pointcut = "showPageMethodPointcut() && args(params, page, size)", returning = "result",
            argNames = "joinPoint, params, page, size, result")
    public void addPaginationRefToPageWithParams(JoinPoint joinPoint, SelectQueryParameter params, Integer page,
                                                    Integer size, Page<?> result) throws NoSuchMethodException {
        Class<?> controllerClass = joinPoint.getSignature().getDeclaringType();
        Method controllerMethod = controllerClass.getMethod(
                joinPoint.getSignature().getName(), SelectQueryParameter.class, Integer.class, Integer.class);

        result.add(linkTo(controllerClass, controllerMethod, params, page, size).withSelfRel())
                .add(linkTo(controllerClass, controllerMethod, params, 1, size).withRel(FIRST))
                .addIf(page != 1,
                        () -> linkTo(controllerClass, controllerMethod, params, page - 1, size).withRel(PREVIOUS))
                .addIf(page < result.getPageMeta().getTotalPages(),
                        () -> linkTo(controllerClass, controllerMethod, params, page + 1, size).withRel(NEXT))
                .add(linkTo(controllerClass, controllerMethod, params, result.getPageMeta().getTotalPages(), size).withRel(LAST));
    }


}
