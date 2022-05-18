package com.epam.esm.config;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.*;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableTransactionManagement
//@ComponentScan("com.epam.esm")
//@Profile("prod")
//@Import(DaoConfig.class)
//public class ServiceConfig {
//    private DataSource dataSource;
//
//    @Autowired
//    ServiceConfig(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    @Bean
//    public DataSourceTransactionManager dataSourceTransactionManager() {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//}
