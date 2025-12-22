package com.lqh.ebuyplt_1001p.Controller.OrderPack2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CancelledItem
{
    @JsonProperty("oOrderID")
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
