package com.lqh.ebuyplt_1001p.Controller.AdminPack;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductClicksInfoTableItem
{
    @JsonProperty("pID")
    private String pID;
    @JsonProperty("pClicksAmount")
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
