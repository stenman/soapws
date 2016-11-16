package com.viskan.uniquecustomer.venueretailgroup.web.extensions.configuration;

import javax.sql.DataSource;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class used to initialize application beans
 */
@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfiguration
{
    @Bean
    public SimpleJdbcCall simpleJdbcCall(DataSource dataSource)
    {
        return new SimpleJdbcCall(dataSource);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder)
    {
        return builder.build();
    }
}
