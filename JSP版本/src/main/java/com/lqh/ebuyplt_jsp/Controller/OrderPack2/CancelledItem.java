package com.lqh.ebuyplt_jsp.Controller.OrderPack2;

import jakarta.json.bind.annotation.JsonbProperty;

public class CancelledItem
{
    @JsonbProperty("oOrderID")
    private String oOrderID;
    public CancelledItem(){}
    public String  getoOrderID()
    {
        return oOrderID;
    }
    public void setoOrderID(String oOrderID)
    {
        this.oOrderID = oOrderID;
    }
}
