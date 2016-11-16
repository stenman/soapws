package com.viskan.uniquecustomer.venueretailgroup.web.extensions.dao;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

/**
 * Handles calls to the database in order to perform operations on order details.
 */
@Repository
public class UpdateOrderStatusDao
{
    private static final String COMP_ID = "pComp_id";
    private static final String STAMP_INFO = "pStampInfo";
    private static final String INT_LANG_ID = "pIntlang_id";
    private static final String VISKAN_USER_ID = "pVisUser_id";
    private static final String ORDER_NUMBER = "pOrderNumber";
    private static final String TRACKING_NUMBER = "pTrackingNumber";
    private static final String STATUS = "pStatus";
    private static final String DELIVERYNUMBER = "pDeliveryNumber";

    public static final String SPRC_UPDATE_TRACKING_REFERENCE = "SPRC_UPDATE_TRACKING_REFERENCE";
    public static final String RETURN_VALUE = "RETURN_VALUE";
    public static final String DELIVERY_NUMBER = "deliveryNumber";

    private static final Logger logger = LoggerFactory.getLogger(UpdateOrderStatusDao.class);

    @Autowired
    private SimpleJdbcCall simpleJdbcCall;

    /**
     * A simple call to an SP that updates the tracking number (if exists) or sends a mail to customer service depending on incoming status. Returns a
     * value with the result code.
     * 
     * @param orderNumber The outorder-number of that parcel to update tracking number on.
     * @param status The external status of the order .
     * @param trackingNumber The tracking number. A URL making the parcel trackable.
     * @return
     */
    public Map<String, Object> updateTrackingReference(Long orderNumber, String status, String trackingNumber)
    {
        simpleJdbcCall.withProcedureName("SPRC_UPDATE_TRACKING_REFERENCE");
        simpleJdbcCall.withReturnValue();
        simpleJdbcCall.returningResultSet(DELIVERY_NUMBER, new SingleColumnRowMapper<>(Integer.class));
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue(COMP_ID, 11)
                .addValue(STAMP_INFO, "-1, UpdateOrderStatus, UpdateOrderStatus")
                .addValue(INT_LANG_ID, -1)
                .addValue(VISKAN_USER_ID, 1)
                .addValue(ORDER_NUMBER, orderNumber)
                .addValue(TRACKING_NUMBER, trackingNumber)
                .addValue(STATUS, status)
                .addValue(DELIVERYNUMBER, -1);

        logSpCall(in);

        Map<String, Object> resultSet = null;
        try
        {
            resultSet = simpleJdbcCall.execute(in);
        }
        catch (Exception e)
        {
            logger.error(String.format("An error was encountered when calling procedure %s. Stacktrace: %s", SPRC_UPDATE_TRACKING_REFERENCE, e));
            throw e;
        }

        logger.info(String.format("Call to %s yielded restultSet=%s", SPRC_UPDATE_TRACKING_REFERENCE, resultSet));

        return resultSet;
    }

    /**
     * Helper method to make the actual code more readable.
     * 
     * @param in In-parameters to log.
     */
    private void logSpCall(SqlParameterSource in)
    {
        logger.info(String.format("Calling %s with the following parameters: %s",
                SPRC_UPDATE_TRACKING_REFERENCE,
                COMP_ID + "= [" + in.getValue(COMP_ID) + "], " +
                    STAMP_INFO + "= [" + in.getValue(STAMP_INFO) + "], " +
                    INT_LANG_ID + "= [" + in.getValue(INT_LANG_ID) + "], " +
                    VISKAN_USER_ID + "= [" + in.getValue(VISKAN_USER_ID) + "], " +
                    ORDER_NUMBER + "= [" + in.getValue(ORDER_NUMBER) + "], " +
                    TRACKING_NUMBER + "= [" + in.getValue(TRACKING_NUMBER) + "], " +
                    STATUS + "= [" + in.getValue(STATUS) + "], " +
                    DELIVERYNUMBER + "= [" + in.getValue(DELIVERYNUMBER) + "]"));
    }
}
