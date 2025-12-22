package com.lqh.ebuyplt_1001p.Controller.OrderPack2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRecord_jsonGet
{
    @JsonProperty("uID")
    private String uID;

    public OrderRecord_jsonGet(){}

    public String getuID()
    {
        return uID;
    }
    public void setuID(String uID)
    {
        this.uID = uID;
    }
}
