package com.lqh.ebuyplt_jsp.Controller.MerchantPack;

import jakarta.json.bind.annotation.JsonbProperty;

public class ProductOnSale_jsonSend
{
    @JsonbProperty("Status")
    private String Status;
    @JsonbProperty("pID")
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
