package com.viskan.uniquecustomer.venueretailgroup.web.extensions.restclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * Unit tests for {@code UpdateOrderStatusRestClient}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateOrderStatusRestClientTest
{
    @Autowired
    private UpdateOrderStatusRestClient updateOrderStatusRestClient;

    @MockBean
    private RestTemplate restTemplate;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test_reportDeliveryNumber_happy_flow()
    {
        ResponseEntity response = new ResponseEntity<>(HttpStatus.OK);
        int deliveryNumber = 1;

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), any())).thenReturn(response);
        HttpStatus actual = updateOrderStatusRestClient.reportDeliveryNumber(deliveryNumber);

        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), any());
        assertEquals(HttpStatus.OK, actual);
    }

    @Test
    public void test_reportDeliveryNumber_exception_thrown()
    {
        thrown.expect(Exception.class);

        int deliveryNumber = 1;

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), any())).thenThrow(Exception.class);
        updateOrderStatusRestClient.reportDeliveryNumber(deliveryNumber);

        fail("An exception should have been thrown");
    }
}
