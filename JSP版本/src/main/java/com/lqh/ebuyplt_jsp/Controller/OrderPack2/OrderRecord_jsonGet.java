package com.lqh.ebuyplt_jsp.Controller.OrderPack2;

import jakarta.json.bind.annotation.JsonbProperty;

public class OrderRecord_jsonGet
{
    @JsonbProperty("uID")
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
