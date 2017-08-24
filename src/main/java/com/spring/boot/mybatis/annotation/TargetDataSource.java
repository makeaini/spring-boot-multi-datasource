package com.spring.boot.mybatis.annotation;

import com.spring.boot.mybatis.common.DataSourceEnum;

import java.lang.annotation.*;

/**
 * Created by shining on 2017-08-24.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    DataSourceEnum value() default DataSourceEnum.master;
}
