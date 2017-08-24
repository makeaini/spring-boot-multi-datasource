package com.spring.boot.mybatis.config;

import com.github.pagehelper.PageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Created by shining on 2017-08-23.
 */
@Configuration
public class MyBatisPageConfig {
    @Bean
    public PageInterceptor pageInterceptor() {
        PageInterceptor interceptor = new PageInterceptor();
        Properties p = new Properties();
        p.setProperty("helperDialect", "mysql");
        p.setProperty("reasonable", "true");
        p.setProperty("supportMethodsArguments", "true");
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("params", "count=countSql");
        p.setProperty("autoRuntimeDialect", "true");
        p.setProperty("pageSizeZero", "true");
        interceptor.setProperties(p);
        return interceptor;
    }
}
