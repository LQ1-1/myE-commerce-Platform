package com.lqh.ebuyplt_1001p.Controller.OrderPack2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderStatusUpdate_jsonGet
{
    @JsonProperty("oOrderID")
    private String oOrderID;

    @JsonProperty("NewStatus")
    private String NewStatus;

    public OrderStatusUpdate_jsonGet(){}
    public String getoOrderID()
    {
        return oOrderID;
    }
    public void setoOrderID(String oOrderID)
    {
        this.oOrderID = oOrderID;
    }
    public String getNewStatus()
    {
        return NewStatus;
    }
    public void setNewStatus(String NewStatus)
    {
        this.NewStatus = NewStatus;
    }
}
