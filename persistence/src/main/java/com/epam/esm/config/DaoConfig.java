//package com.epam.esm.config;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import javax.sql.DataSource;
//
//@Configuration
//@ComponentScan(basePackages = "com.epam.esm")
//@Profile("prod")
//public class DaoConfig {
//    private static final String DATASOURCE_PROPERTY = "/property/datasource.properties";
//
//    @Bean
//    public DataSource dataSource() {
//        HikariConfig config = new HikariConfig(DATASOURCE_PROPERTY);
//
//        return new HikariDataSource(config);
//    }
//
//    @Bean
//    public JdbcTemplate jdbcTemplate() {
//        return new JdbcTemplate(dataSource());
//    }
//}
