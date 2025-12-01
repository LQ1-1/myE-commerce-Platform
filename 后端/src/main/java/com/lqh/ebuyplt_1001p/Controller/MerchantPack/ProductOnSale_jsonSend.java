package com.lqh.ebuyplt_1001p.Controller.MerchantPack;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductOnSale_jsonSend
{
    @JsonProperty("Status")
    private String Status;
    @JsonProperty("pID")
    private String pID;

    public ProductOnSale_jsonSend(){}

    public String getpID()
    {
        return pID;
    }
    public void setpID(String pID)
    {
        this.pID = pID;
    }
    public String getStatus()
    {
        return Status;
    }
    public void setStatus(String Status)
    {
        this.Status = Status;
    }
}
