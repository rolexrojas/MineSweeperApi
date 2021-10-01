package com.deviget.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class Database {

    ApplicationProperties applicationProperties;

    @Autowired
    public void AppProperties(ApplicationProperties applicationProperties){
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public DataSource dataSource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(applicationProperties.getDatasourceDriverClassName());
        dataSource.setJdbcUrl(applicationProperties.getDataSourceUrl());
        dataSource.setUsername(applicationProperties.getDataSourceUsername());
        dataSource.setPassword(applicationProperties.getDataSourcePassword());

        return dataSource;
    }

}
