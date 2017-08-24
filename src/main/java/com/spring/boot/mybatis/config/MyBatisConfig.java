package com.spring.boot.mybatis.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import com.spring.boot.mybatis.common.DataSourceEnum;
import com.spring.boot.mybatis.common.DynamicDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by shining on 2017-08-24.
 */
@Configuration
@MapperScan(basePackages = "com.spring.boot.mybatis.mapper")
public class MyBatisConfig implements EnvironmentAware {
    private Environment env;
    @Autowired
    private PageInterceptor pageInterceptor;
    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    @Bean(initMethod = "init", destroyMethod = "close",name = "masterDataSource")
    @Primary
    public DruidDataSource masterDataSource() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(env.getProperty("master.datasource.driver-class-name").trim());
        dataSource.setUrl(env.getProperty("master.datasource.url").trim());
        dataSource.setUsername(env.getProperty("master.datasource.username").trim());
        dataSource.setPassword(env.getProperty("master.datasource.password").trim());
        dataSource.setFilters(env.getProperty("datasource.filters").trim());
        dataSource.setMaxActive(Integer.parseInt(env.getProperty("datasource.maxActive").trim()));
        dataSource.setInitialSize(Integer.parseInt(env.getProperty("datasource.initialSize").trim()));
        dataSource.setMaxWait(Long.parseLong(env.getProperty("datasource.maxWait").trim()));
        dataSource.setMinIdle(Integer.parseInt(env.getProperty("datasource.minIdle").trim()));
        dataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(env.getProperty("datasource.timeBetweenEvictionRunsMillis").trim()));
        dataSource.setMinEvictableIdleTimeMillis(Long.parseLong(env.getProperty("datasource.minEvictableIdleTimeMillis").trim()));
        dataSource.setTestOnBorrow(Boolean.parseBoolean(env.getProperty("datasource.testOnBorrow").trim()));
        dataSource.setTestWhileIdle(Boolean.parseBoolean(env.getProperty("datasource.testOnReturn").trim()));
        dataSource.setValidationQuery(env.getProperty("datasource.validationQuery").trim());
        Properties properties = new Properties();
        properties.setProperty("druid.stat.slowSqlMillis","5000");
        dataSource.setConnectProperties(properties);
        return dataSource;
    }
    @Bean(initMethod = "init", destroyMethod = "close",name = "slaveDataSource")
    public DruidDataSource slaveDataSource() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(env.getProperty("slave.datasource.driver-class-name").trim());
        dataSource.setUrl(env.getProperty("slave.datasource.url").trim());
        dataSource.setUsername(env.getProperty("slave.datasource.username").trim());
        dataSource.setPassword(env.getProperty("slave.datasource.password").trim());
        dataSource.setFilters(env.getProperty("datasource.filters").trim());
        dataSource.setMaxActive(Integer.parseInt(env.getProperty("datasource.maxActive").trim()));
        dataSource.setInitialSize(Integer.parseInt(env.getProperty("datasource.initialSize").trim()));
        dataSource.setMaxWait(Long.parseLong(env.getProperty("datasource.maxWait").trim()));
        dataSource.setMinIdle(Integer.parseInt(env.getProperty("datasource.minIdle").trim()));
        dataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(env.getProperty("datasource.timeBetweenEvictionRunsMillis").trim()));
        dataSource.setMinEvictableIdleTimeMillis(Long.parseLong(env.getProperty("datasource.minEvictableIdleTimeMillis").trim()));
        dataSource.setTestOnBorrow(Boolean.parseBoolean(env.getProperty("datasource.testOnBorrow").trim()));
        dataSource.setTestWhileIdle(Boolean.parseBoolean(env.getProperty("datasource.testOnReturn").trim()));
        dataSource.setValidationQuery(env.getProperty("datasource.validationQuery").trim());
        Properties properties = new Properties();
        properties.setProperty("druid.stat.slowSqlMillis","5000");
        dataSource.setConnectProperties(properties);
        return dataSource;
    }

    @Bean("dynamicDataSource")
    public DynamicDataSource dynamicDataSource(@Qualifier("masterDataSource") DruidDataSource masterDataSource,
                                               @Qualifier("slaveDataSource") DruidDataSource slaveDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put(DataSourceEnum.master, masterDataSource);
        targetDataSources.put(DataSourceEnum.slaver, slaveDataSource);
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
        dataSource.setDefaultTargetDataSource(masterDataSource);// 默认的datasource设置为myTestDbDataSource
        return dataSource;
    }
    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DynamicDataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);// 指定数据源(这个必须有，否则报错)
        // 下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
//        factoryBean.setTypeAliasesPackage(typeAliasesPackage);// 指定基包
//        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));//
        System.out.println(dynamicDataSource);
        Interceptor[] plugins = new Interceptor[] { pageInterceptor };
        sqlSessionFactoryBean.setPlugins(plugins);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.spring.boot.mybatis.entity");
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }

}
