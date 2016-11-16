package com.viskan.uniquecustomer.venueretailgroup.web.extensions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Main class for starting the application
 */
@SpringBootApplication
public class SoapWebService extends SpringBootServletInitializer
{

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(SoapWebService.class);
    }

    public static void main(String[] args)
    {
        SpringApplication.run(SoapWebService.class, args);
    }
}
