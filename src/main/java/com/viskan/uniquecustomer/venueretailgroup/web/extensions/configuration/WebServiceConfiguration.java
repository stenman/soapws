package com.viskan.uniquecustomer.venueretailgroup.web.extensions.configuration;

import com.viskan.uniquecustomer.venueretailgroup.web.extensions.endpoint.UpdateOrderStatusEndpoint;

import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import pipeline_ws.CCWSFacktaPortType;

/**
 * Configuration class used to initialize web service beans and the WS endpoints.
 */
@Configuration
public class WebServiceConfiguration
{

    @Bean
    public ServletRegistrationBean cxfServlet()
    {
        return new ServletRegistrationBean(new CXFServlet(), "/viskan-vrg-soap-ws/*");
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus()
    {
        return new SpringBus();
    }

    @Bean
    public CCWSFacktaPortType statusService()
    {
        return new UpdateOrderStatusEndpoint();
    }

    @Bean
    public SaajSoapMessageFactory messageFactory()
    {
        SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
        messageFactory.setSoapVersion(SoapVersion.SOAP_12);
        messageFactory.afterPropertiesSet();
        return messageFactory;
    }

    @Bean
    public Endpoint endpoint()
    {
        EndpointImpl endpoint = new EndpointImpl(springBus(), statusService(), SOAPBinding.SOAP12HTTP_BINDING);
        endpoint.publish("/CC_WSFackta1.0");
        return endpoint;
    }
}
