package com.lqh.ebuyplt_jsp.Controller.AdminPack;

import jakarta.json.bind.annotation.JsonbProperty;

public class MerchantsProductTableItem
{
    @JsonbProperty("uID")
    private String uID;
    @JsonbProperty("pID")
    private String pID;

    public MerchantsProductTableItem(){}

    public String  getuID()
    {
        return uID;
    }
    public void setuID(String uID)
    {
        this.uID = uID;
    }
    public String getpID()
    {
        return pID;
    }
    public void setpID(String pID)
    {
        this.pID = pID;
    }
}
