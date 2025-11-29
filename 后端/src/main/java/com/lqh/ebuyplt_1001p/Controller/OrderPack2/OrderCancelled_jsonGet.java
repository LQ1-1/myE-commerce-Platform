package com.lqh.ebuyplt_1001p.Controller.OrderPack2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class OrderCancelled_jsonGet
{
    @JsonProperty("uID")
    private String uID;

    @JsonProperty("oOrderIDs")
    public ArrayList<CancelledItem>oOrderIDs;

    public OrderCancelled_jsonGet()
    {
        oOrderIDs=new  ArrayList<>();
    }

    public String getuID()
    {
        return uID;
    }
    public void setuID(String uID)
    {
        this.uID = uID;
    }
    public ArrayList<CancelledItem>getoOrderIDs()
    {
        return oOrderIDs;
    }
    public void setoOrderIDs(ArrayList<CancelledItem> oOrderIDs)
    {
        for(CancelledItem item:oOrderIDs)
        {
            this.oOrderIDs.add(item);
        }
    }
}
