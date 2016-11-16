package com.viskan.uniquecustomer.venueretailgroup.web.extensions.controller;

import com.viskan.uniquecustomer.venueretailgroup.web.extensions.dao.UpdateOrderStatusDao;
import com.viskan.uniquecustomer.venueretailgroup.web.extensions.restclient.UpdateOrderStatusRestClient;

import static com.viskan.uniquecustomer.venueretailgroup.web.extensions.dao.UpdateOrderStatusDao.DELIVERY_NUMBER;
import static com.viskan.uniquecustomer.venueretailgroup.web.extensions.dao.UpdateOrderStatusDao.RETURN_VALUE;
import static com.viskan.uniquecustomer.venueretailgroup.web.extensions.dao.UpdateOrderStatusDao.SPRC_UPDATE_TRACKING_REFERENCE;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Endpoint controller handling the actual logic of the endpoint implementation
 */
@Component
public class UpdateOrderStatusController
{
    @Autowired
    UpdateOrderStatusDao updateOrderStatusDao;

    @Autowired
    UpdateOrderStatusRestClient updateOrderStatusRestClient;

    private final static Logger logger = LoggerFactory.getLogger(UpdateOrderStatusController.class);

    private static final String ACCENT = "accent";

    private static final int RESPONSE_CODE_OK = 0;
    private static final int RESPONSE_CODE_ORDER_NOT_FOUND = -1;
    private static final int RESPONSE_CODE_UNEXPECTED_PROBLEM = -99;
    private static final int VISKAN_RESPONSE_CODE_ORDER_NOT_DELIVERED = -66;

    /**
     * Updates tracking reference number for the delivery if the delivery was not cancelled, in which case a mail is sent to customer service. Unless
     * the delivery was cancelled, it will hereafter be reported.
     * 
     * @param domainName The web site in which the order was created. Expected values are at this time only “Accent” (not case sensitive).
     * @param orderNumber The order number in numeric format.
     * @param status A string containing the current status of the parcel. Sent = 4, Can not be delivered = 10, Delivered from delivery warehouse to
     *            customer = 11, Delivered from delivery warehouse to store = 12, Received in store = 13, Delivered in store = 14
     * @param trackingNumber The ID of the shipping, to be used for track and trace. It is optional, but an empty value will overwrite a pre-existing
     *            value.
     * @return A response code indicating if the message was correctly received. Ok = 0, OrderNotFound = -1, StatusInvalid = -2, UnexpectedProblem =
     *         -99
     */
    public int updateOrderStatus(String domainName, Long orderNumber, String status, String trackingNumber)
    {
        if (!domainName.toLowerCase().equals(ACCENT))
        {
            return RESPONSE_CODE_ORDER_NOT_FOUND;
        }

        Map<String, Object> resultSet = updateOrderStatusDao.updateTrackingReference(orderNumber, status, trackingNumber);

        if (resultSet == null || resultSet.isEmpty())
        {
            logger.debug(String.format("Resultset from %s was null or empty - resultSet=%s", SPRC_UPDATE_TRACKING_REFERENCE, resultSet));
            return RESPONSE_CODE_UNEXPECTED_PROBLEM;
        }

        int responseCode = (int) resultSet.get(RETURN_VALUE);

        if (responseCode != RESPONSE_CODE_OK)
        {
            logger.debug(String.format("Could not process delivery. Response code from %s: %s", SPRC_UPDATE_TRACKING_REFERENCE, responseCode));
            return responseCode == VISKAN_RESPONSE_CODE_ORDER_NOT_DELIVERED ? RESPONSE_CODE_OK : responseCode;
        }

        int deliveryNumberToReport = (int) resultSet.get(DELIVERY_NUMBER);

        HttpStatus httpStatus = updateOrderStatusRestClient.reportDeliveryNumber(deliveryNumberToReport);
        if (httpStatus == null || httpStatus != HttpStatus.OK)
        {
            logger.debug(String.format("Encountered an error while reporting delivery, httpStatus=%s. Delivery could not be reported - deliveryNumber=%s", httpStatus, deliveryNumberToReport));
            return RESPONSE_CODE_UNEXPECTED_PROBLEM;
        }
        return RESPONSE_CODE_OK;
    }
}
