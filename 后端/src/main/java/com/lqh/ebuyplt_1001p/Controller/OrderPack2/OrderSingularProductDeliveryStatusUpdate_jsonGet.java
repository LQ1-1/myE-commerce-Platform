package com.lqh.ebuyplt_1001p.Controller.OrderPack2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderSingularProductDeliveryStatusUpdate_jsonGet extends OrderStatusUpdate_jsonGet
{
    @JsonProperty("pID")
    private String pID;

    public  OrderSingularProductDeliveryStatusUpdate_jsonGet()
    {
        super();
    }

    public String  getpID()
    {
        return pID;
    }
    public void setpID(String pID)
    {
        this.pID = pID;
    }
}
