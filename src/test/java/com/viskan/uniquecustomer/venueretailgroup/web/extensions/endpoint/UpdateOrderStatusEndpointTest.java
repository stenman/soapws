package com.viskan.uniquecustomer.venueretailgroup.web.extensions.endpoint;

import com.viskan.uniquecustomer.venueretailgroup.web.extensions.dao.UpdateOrderStatusDao;
import com.viskan.uniquecustomer.venueretailgroup.web.extensions.restclient.UpdateOrderStatusRestClient;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit tests for {@code UpdateOrderStatusEndpoint}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateOrderStatusEndpointTest
{
    @Autowired
    private UpdateOrderStatusEndpoint updateOrderStatusEndpoint;

    @MockBean
    private UpdateOrderStatusDao updateOrderStatusDao;

    @MockBean
    private UpdateOrderStatusRestClient updateOrderStatusRestClient;

    @MockBean
    private SimpleJdbcCall simpleJdbcCall;

    @Test
    public void test_updateOrderStatus()
    {
        Map<String, Object> daoResult = new HashMap<>();
        daoResult.put("RETURN_VALUE", 0);
        daoResult.put("deliveryNumber", 1);

        when(updateOrderStatusDao.updateTrackingReference(anyLong(), anyString(), anyString())).thenReturn(daoResult);
        when(updateOrderStatusRestClient.reportDeliveryNumber(anyInt())).thenReturn(HttpStatus.OK);
        Integer actual = updateOrderStatusEndpoint.updateOrderStatus("accent", 1001L, "4", "123456789");

        verify(updateOrderStatusDao, times(1)).updateTrackingReference(anyLong(), anyString(), anyString());
        verify(updateOrderStatusRestClient, times(1)).reportDeliveryNumber(anyInt());
        assertEquals(new Integer(0), actual);
    }

    @Test
    public void test_updateOrderStatus_cased_domainName()
    {
        Map<String, Object> daoResult = new HashMap<>();
        daoResult.put("RETURN_VALUE", 0);
        daoResult.put("deliveryNumber", 1);

        when(updateOrderStatusDao.updateTrackingReference(anyLong(), anyString(), anyString())).thenReturn(daoResult);
        when(updateOrderStatusRestClient.reportDeliveryNumber(anyInt())).thenReturn(HttpStatus.OK);
        Integer actual = updateOrderStatusEndpoint.updateOrderStatus("AcCeNt", 1001L, "4", "123456789");

        verify(updateOrderStatusDao, times(1)).updateTrackingReference(anyLong(), anyString(), anyString());
        verify(updateOrderStatusRestClient, times(1)).reportDeliveryNumber(anyInt());
        assertEquals(new Integer(0), actual);
    }

    @Test
    public void test_updateOrderStatus_no_domainName()
    {
        Integer actual = updateOrderStatusEndpoint.updateOrderStatus("", 1001L, "4", "123456789");

        verify(updateOrderStatusDao, never()).updateTrackingReference(anyLong(), anyString(), anyString());
        verify(updateOrderStatusRestClient, never()).reportDeliveryNumber(anyInt());
        assertEquals(new Integer(-1), actual);
    }

    @Test
    public void test_updateOrderStatus_invalid_domainName()
    {
        Integer actual = updateOrderStatusEndpoint.updateOrderStatus("invalid", 1001L, "4", "123456789");

        verify(updateOrderStatusDao, never()).updateTrackingReference(anyLong(), anyString(), anyString());
        verify(updateOrderStatusRestClient, never()).reportDeliveryNumber(anyInt());
        assertEquals(new Integer(-1), actual);
    }

    @Test
    public void test_updateOrderStatus_sp_response_code_nok()
    {
        Map<String, Object> daoResult = new HashMap<>();
        daoResult.put("RETURN_VALUE", -99);
        daoResult.put("deliveryNumber", 666);

        when(updateOrderStatusDao.updateTrackingReference(anyLong(), anyString(), anyString())).thenReturn(daoResult);
        Integer actual = updateOrderStatusEndpoint.updateOrderStatus("accent", 1001L, "4", "123456789");

        verify(updateOrderStatusDao, times(1)).updateTrackingReference(anyLong(), anyString(), anyString());
        verify(updateOrderStatusRestClient, never()).reportDeliveryNumber(anyInt());
        assertEquals(new Integer(-99), actual);
    }

    @Test
    public void test_updateOrderStatus_rest_response_code_nok()
    {
        Map<String, Object> daoResult = new HashMap<>();
        daoResult.put("RETURN_VALUE", 0);
        daoResult.put("deliveryNumber", 666);

        when(updateOrderStatusDao.updateTrackingReference(anyLong(), anyString(), anyString())).thenReturn(daoResult);
        when(updateOrderStatusRestClient.reportDeliveryNumber(anyInt())).thenReturn(HttpStatus.NOT_FOUND);
        Integer actual = updateOrderStatusEndpoint.updateOrderStatus("accent", 1001L, "4", "123456789");

        verify(updateOrderStatusDao, times(1)).updateTrackingReference(anyLong(), anyString(), anyString());
        verify(updateOrderStatusRestClient, times(1)).reportDeliveryNumber(anyInt());
        assertEquals(new Integer(-99), actual);
    }

    @Test
    public void test_updateOrderStatus_rest_response_is_null()
    {
        Map<String, Object> daoResult = new HashMap<>();
        daoResult.put("RETURN_VALUE", 0);
        daoResult.put("deliveryNumber", 666);
        
        when(updateOrderStatusDao.updateTrackingReference(anyLong(), anyString(), anyString())).thenReturn(daoResult);
        when(updateOrderStatusRestClient.reportDeliveryNumber(anyInt())).thenReturn(null);
        Integer actual = updateOrderStatusEndpoint.updateOrderStatus("accent", 1001L, "4", "123456789");

        verify(updateOrderStatusDao, times(1)).updateTrackingReference(anyLong(), anyString(), anyString());
        verify(updateOrderStatusRestClient, times(1)).reportDeliveryNumber(anyInt());
        assertEquals(new Integer(-99), actual);
    }

    @Test
    public void test_updateOrderStatus_rest_response_code_not_delivered()
    {
        Map<String, Object> daoResult = new HashMap<>();
        daoResult.put("RETURN_VALUE", -66);
        daoResult.put("deliveryNumber", 1337);

        when(updateOrderStatusDao.updateTrackingReference(anyLong(), anyString(), anyString())).thenReturn(daoResult);
        Integer actual = updateOrderStatusEndpoint.updateOrderStatus("accent", 1001L, "4", "123456789");

        verify(updateOrderStatusDao, times(1)).updateTrackingReference(anyLong(), anyString(), anyString());
        verify(updateOrderStatusRestClient, never()).reportDeliveryNumber(anyInt());
        assertEquals(new Integer(0), actual);
    }

    @Test
    public void test_updateOrderStatus_sp_resultset_is_null()
    {
        when(updateOrderStatusDao.updateTrackingReference(anyLong(), anyString(), anyString())).thenReturn(null);
        Integer actual = updateOrderStatusEndpoint.updateOrderStatus("accent", 1001L, "4", "123456789");

        verify(updateOrderStatusDao, times(1)).updateTrackingReference(anyLong(), anyString(), anyString());
        verify(updateOrderStatusRestClient, never()).reportDeliveryNumber(anyInt());
        assertEquals(new Integer(-99), actual);
    }

    @Test
    public void test_updateOrderStatus_sp_resultset_is_empty()
    {
        Map<String, Object> daoResult = new HashMap<>();

        when(updateOrderStatusDao.updateTrackingReference(anyLong(), anyString(), anyString())).thenReturn(daoResult);
        Integer actual = updateOrderStatusEndpoint.updateOrderStatus("accent", 1001L, "4", "123456789");

        verify(updateOrderStatusDao, times(1)).updateTrackingReference(anyLong(), anyString(), anyString());
        verify(updateOrderStatusRestClient, never()).reportDeliveryNumber(anyInt());
        assertEquals(new Integer(-99), actual);
    }
}
