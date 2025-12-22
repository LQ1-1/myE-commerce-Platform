package com.lqh.ebuyplt_jsp.Controller.OrderPack2;

import jakarta.json.bind.annotation.JsonbProperty;

import java.util.ArrayList;

public class OrderCancelled_jsonGet
{
    @JsonbProperty("uID")
    private String uID;

    @JsonbProperty("oOrderIDs")
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
