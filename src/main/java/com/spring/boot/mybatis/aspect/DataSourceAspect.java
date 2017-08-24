package com.spring.boot.mybatis.aspect;

import com.spring.boot.mybatis.annotation.TargetDataSource;
import com.spring.boot.mybatis.common.DataSourceContextHolder;
import com.spring.boot.mybatis.common.DataSourceEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by shining on 2017-08-24.
 */
@Component
@Aspect
public class DataSourceAspect {
    @Pointcut("execution(public * com.spring.boot.mybatis.service..*.*(..))" +
            "&& @annotation(com.spring.boot.mybatis.annotation.TargetDataSource)")
    public void dataSourcePointcut() {
    }

    @Around("dataSourcePointcut()")
    public Object doAround(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        System.out.println(method.getAnnotation(TargetDataSource.class));
        if (method.isAnnotationPresent(TargetDataSource.class)) {
            TargetDataSource annotation = method.getAnnotation(TargetDataSource.class);
            DataSourceEnum sourceEnum = annotation.value();
            if (sourceEnum == DataSourceEnum.master) {
                DataSourceContextHolder.setDataSourceType(DataSourceEnum.master);
            } else if (sourceEnum == DataSourceEnum.slaver) {
                DataSourceContextHolder.setDataSourceType(DataSourceEnum.slaver);
            }
        }
        Object result = null;
        try {
            result = pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            DataSourceContextHolder.resetDataSourceType();
        }
        return result;
    }
}
