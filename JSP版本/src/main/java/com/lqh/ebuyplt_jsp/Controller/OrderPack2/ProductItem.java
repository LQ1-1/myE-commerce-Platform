package com.lqh.ebuyplt_jsp.Controller.OrderPack2;

import jakarta.json.bind.annotation.JsonbProperty;

//商品信息(用户发来)
public class ProductItem
{
    @JsonbProperty("pID")
    private String pID;         //订购的商品的编号

    @JsonbProperty("pAmount")
    private int pAmount;        //订购的数量

    @JsonbProperty("oPrice")
    private double oPrice;

    @JsonbProperty("oProductDeliveryStatus")
    private String oProductDeliveryStatus;

    public String getpID()
    {
        return pID;
    }
    public void setpID(String pID)
    {
        this.pID = pID;
    }
    public int getpAmount()
    {
        return pAmount;
    }
    public void setpAmount(int pAmount)
    {
        this.pAmount = pAmount;
    }
    public double getoPrice()
    {
        return oPrice;
    }
    public void setoPrice(double oPrice)
    {
        this.oPrice = oPrice;
    }
    public String getoProductDeliveryStatus()
    {
        return oProductDeliveryStatus;
    }
    public void setoProductDeliveryStatus(String oProductDeliveryStatus)
    {
        this.oProductDeliveryStatus = oProductDeliveryStatus;
    }
}
