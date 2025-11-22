package com.lqh.ebuyplt_1001p.Controller.OrderPack;

public class OrderStatusUpdate_jsonGet
{
    private String oOrderID;
    private String NewState;

    public OrderStatusUpdate_jsonGet(){}

    public String getoOrderID()
    {
        return oOrderID;
    }
    public String getNewState()
    {
        return NewState;
    }

    public void setoOrderID(String oOrderID)
    {
        this.oOrderID = oOrderID;
    }
    public void setNewState(String NewState)
    {
        this.NewState = NewState;
    }
}
