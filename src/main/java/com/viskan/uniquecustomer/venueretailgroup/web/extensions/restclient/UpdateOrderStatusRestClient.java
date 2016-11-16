package com.viskan.uniquecustomer.venueretailgroup.web.extensions.restclient;

import static java.util.Arrays.asList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * REST client for managing calls to ETMServer
 */
@Component
public class UpdateOrderStatusRestClient
{
    @Autowired
    private RestTemplate restTemplate;

    private final static Logger logger = LoggerFactory.getLogger(UpdateOrderStatusRestClient.class);

    private static final String AUTHORIZATION = "Authorization";
    private static final String X_INSTANCE_ID = "X-Instance-ID";

    @Value("${restresource.etmserver.url}")
    private String url;
    @Value("${restresource.etmserver.authorization}")
    private String authorization;
    @Value("${restresource.etmserver.x-instance-id}")
    private String xInstanceId;

    /**
     * POSTs to ETMServers OutdeliveryServiceRest to report delivery
     * 
     * @param deliveryNumber delivery number to report.
     * @return An HttpStatus code indicating the result of the call.
     */
    public HttpStatus reportDeliveryNumber(int deliveryNumber)
    {
        String requestJson = "{\"del_no\":" + deliveryNumber + ",\"fullyDeliverRestOrder\":1}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(asList(MediaType.APPLICATION_JSON));
        headers.add(AUTHORIZATION, "Basic " + authorization);
        headers.set(X_INSTANCE_ID, xInstanceId);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        logger.debug(String.format("Calling ETMServer on %s with the following body: %s", url, requestJson));

        ResponseEntity response = null;
        try
        {
            response = restTemplate.postForEntity(url, entity, ResponseEntity.class);
        }
        catch (Exception e)
        {
            logger.error(String.format("An error was encountered when reporting deliveryNumber=%s. Stacktrace: %s", deliveryNumber, e));
            throw e;
        }

        logger.debug(String.format("Response from ETMServer: %s", response));

        return response == null ? null : response.getStatusCode();
    }
}
