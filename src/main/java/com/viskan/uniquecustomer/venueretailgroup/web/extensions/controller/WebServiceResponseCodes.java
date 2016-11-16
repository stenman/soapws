package com.viskan.uniquecustomer.venueretailgroup.web.extensions.controller;

public enum WebServiceResponseCodes
{
    RESPONSE_CODE_OK(0, "OK"),
    RESPONSE_CODE_ORDER_NOT_FOUND(-1, "Order not found. Invalid Domain Name or Ordernumber does not exist."),
    RESPONSE_CODE_STATUS_INVALID(-2, "Invalid status value."),
    RESPONSE_CODE_UNEXPECTED_PROBLEM(-99, "An unexpected problem occurred while updating the order in ETM."),
    VISKAN_INTERNAL_RESPONSE_CODE_ORDER_NOT_DELIVERED(-66, "Order could not be delivered - mail was sent to customer service. Status OK will be sent to client.");
    
    private String name;
    private int id;

    WebServiceResponseCodes(int id, String name)
    {
        this.name = name;
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }

    public static WebServiceResponseCodes getById(int id)
    {
        for (WebServiceResponseCodes erp : values())
        {
            if (erp.id == id)
            {
                return erp;
            }
        }
        return null;
    }
}
