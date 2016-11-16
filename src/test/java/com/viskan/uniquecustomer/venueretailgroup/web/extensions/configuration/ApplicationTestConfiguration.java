package com.viskan.uniquecustomer.venueretailgroup.web.extensions.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Configuration necessary for tests to work properly
 */
@Configuration
public class ApplicationTestConfiguration
{
    @Bean
    public DataSource dataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
        dataSource.setUrl("xxx");
        dataSource.setUsername("xxx");
        dataSource.setPassword("xxx");
        return dataSource;
    }
}
