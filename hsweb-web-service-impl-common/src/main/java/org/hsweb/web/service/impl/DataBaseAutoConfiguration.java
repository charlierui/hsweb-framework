package org.hsweb.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.webbuilder.sql.DataBase;
import org.webbuilder.sql.DataBaseMetaData;
import org.webbuilder.sql.support.MysqlDataBaseMetaData;
import org.webbuilder.sql.support.OracleDataBaseMetaData;
import org.webbuilder.sql.support.common.CommonDataBase;
import org.webbuilder.sql.support.executor.ObjectWrapperFactory;
import org.webbuilder.sql.support.executor.SqlExecutor;
import org.webbuilder.sql.validator.ValidatorFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by zhouhao on 16-4-20.
 */
@Configuration
@ConfigurationProperties(
        prefix = "spring.datasource"
)
public class DataBaseAutoConfiguration {
    @Resource
    private SqlExecutor sqlExecutor;

    @Autowired
    private DataSourceProperties properties;

    @Autowired(required = false)
    private ValidatorFactory validatorFactory;

    @Autowired(required = false)
    private ObjectWrapperFactory objectWrapperFactory;

    @PostConstruct
    public void init() {

    }

    @Bean
    public DataBase getDataBase() {
        DataBaseMetaData dataBaseMetaData = null;
        String driverClassName = properties.getDriverClassName();
        if (driverClassName.contains("mysql")) {
            dataBaseMetaData = new MysqlDataBaseMetaData();
        } else if (driverClassName.contains("oracle")) {
            dataBaseMetaData = new OracleDataBaseMetaData();
        } else if (driverClassName.contains("h2")) {
            dataBaseMetaData = new OracleDataBaseMetaData();
        }

        if (dataBaseMetaData == null)
            dataBaseMetaData = new OracleDataBaseMetaData();
        if (validatorFactory != null)
            dataBaseMetaData.setValidatorFactory(validatorFactory);
        CommonDataBase dataBase = new CommonDataBase(dataBaseMetaData, sqlExecutor);
        if (objectWrapperFactory != null)
            dataBase.setWrapperFactory(objectWrapperFactory);
        return dataBase;
    }
}