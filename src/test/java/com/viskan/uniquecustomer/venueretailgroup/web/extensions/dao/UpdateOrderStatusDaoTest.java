package com.viskan.uniquecustomer.venueretailgroup.web.extensions.dao;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.core.Is;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit tests for {@code UpdateOrderStatusDao}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateOrderStatusDaoTest
{
    @Autowired
    private UpdateOrderStatusDao updateOrderStatusDao;

    @MockBean
    private SimpleJdbcCall simpleJdbcCall;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test_getUpdateOrderStatus_happy_flow()
    {
        Map<String, Object> expected = new HashMap<>();
        expected.put("RETURN_VALUE", "-66");
        expected.put("deliveryNumber", "1");
        when(simpleJdbcCall.execute(any(SqlParameterSource.class))).thenReturn(expected);

        Map<String, Object> actual = updateOrderStatusDao.updateTrackingReference(1001L, "4", "666666");

        verify(simpleJdbcCall, times(1)).execute(any(SqlParameterSource.class));
        assertThat(actual, Is.is(expected));
    }

    @Test
    public void test_getUpdateOrderStatus_exception_thrown() throws Exception
    {
        thrown.expect(Exception.class);

        when(simpleJdbcCall.execute(any(SqlParameterSource.class))).thenThrow(Exception.class);

        updateOrderStatusDao.updateTrackingReference(1001L, "4", "666666");

        fail("An exception should have been thrown");
    }
}
