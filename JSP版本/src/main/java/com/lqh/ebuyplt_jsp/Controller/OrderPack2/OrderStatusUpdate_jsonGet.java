package com.lqh.ebuyplt_jsp.Controller.OrderPack2;

import jakarta.json.bind.annotation.JsonbProperty;

public class OrderStatusUpdate_jsonGet
{
    @JsonbProperty("oOrderID")
    private String oOrderID;

    @JsonbProperty("NewStatus")
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
