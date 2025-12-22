package com.lqh.ebuyplt_jsp.Controller.AdminPack;

import jakarta.json.bind.annotation.JsonbProperty;

public class ProductClicksInfoTableItem
{
    @JsonbProperty("pID")
    private String pID;
    @JsonbProperty("pClicksAmount")
    private long pClicksAmount;

    public ProductClicksInfoTableItem(){}

    public String getpID()
    {
        return pID;
    }
    public void setpID(String pID)
    {
        this.pID = pID;
    }
    public long getpClicksAmount()
    {
        return pClicksAmount;
    }
    public void setpClicksAmount(long pClicksAmount)
    {
        this.pClicksAmount = pClicksAmount;
    }
}
