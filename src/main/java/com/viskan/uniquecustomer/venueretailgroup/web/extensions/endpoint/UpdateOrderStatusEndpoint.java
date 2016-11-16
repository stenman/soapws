package com.viskan.uniquecustomer.venueretailgroup.web.extensions.endpoint;

import com.viskan.uniquecustomer.venueretailgroup.web.extensions.controller.UpdateOrderStatusController;

import org.springframework.beans.factory.annotation.Autowired;

import pipeline_ws.CCWSFacktaPortType;

/**
 * SOAP WS Endpoint implementation for updating order status
 */
public class UpdateOrderStatusEndpoint implements CCWSFacktaPortType
{
    @Autowired
    UpdateOrderStatusController updateOrderStatusController;

    /**
     * @see UpdateOrderStatusController#updateOrderStatus(String, Long, String, String)
     */
    @Override
    public Integer updateOrderStatus(String domainName, Long orderNumber, String status, String trackingNumber)
    {
        return updateOrderStatusController.updateOrderStatus(domainName, orderNumber, status, trackingNumber);
    }

}
