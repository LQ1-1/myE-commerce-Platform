package com.lqh.ebuyplt_jsp.Controller.OrderPack2;

import jakarta.json.bind.annotation.JsonbProperty;

public class OrderSingularProductDeliveryStatusUpdate_jsonGet extends OrderStatusUpdate_jsonGet
{
    @JsonbProperty("pID")
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
