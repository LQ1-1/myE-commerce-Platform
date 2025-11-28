package com.lqh.ebuyplt_1001p.Controller.OrderPack2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class OrderCancelled_jsonSend
{
    @JsonProperty("uID")
    private String uID;

    @JsonProperty("oOrderIDs")
    public ArrayList<CancelledFeedBackItem> oOrderIDs;

    public OrderCancelled_jsonSend() {}

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
