package com.lqh.ebuyplt_jsp.Controller.OrderPack2;

import jakarta.json.bind.annotation.JsonbProperty;

import java.util.ArrayList;

public class OrderCancelled_jsonSend
{
    @JsonbProperty("uID")
    private String uID;

    @JsonbProperty("oOrderIDs")
    public ArrayList<CancelledFeedBackItem> oOrderIDs;

    public OrderCancelled_jsonSend()
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
    public ArrayList<CancelledFeedBackItem> getoOrderIDs()
    {
        return oOrderIDs;
    }
    public void setoOrderIDs(ArrayList<CancelledFeedBackItem> oOrderIDs)
    {
        for(CancelledFeedBackItem item : oOrderIDs)
        {
            this.oOrderIDs.add(item);
        }
    }
}
