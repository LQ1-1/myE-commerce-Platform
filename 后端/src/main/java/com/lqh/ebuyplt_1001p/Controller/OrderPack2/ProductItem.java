package com.lqh.ebuyplt_1001p.Controller.OrderPack2;

import com.fasterxml.jackson.annotation.JsonProperty;

//商品信息(用户发来)
public class ProductItem
{
    @JsonProperty("pID")
    private String pID;         //订购的商品的编号

    @JsonProperty("pAmount")
    private int pAmount;        //订购的数量

    @JsonProperty("oPrice")
    private double oPrice;

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
}
