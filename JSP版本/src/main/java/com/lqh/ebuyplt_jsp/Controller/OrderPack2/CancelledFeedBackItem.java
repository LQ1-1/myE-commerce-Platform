package com.lqh.ebuyplt_jsp.Controller.OrderPack2;

import jakarta.json.bind.annotation.JsonbProperty;

public class CancelledFeedBackItem
{
    @JsonbProperty("oOrderID")
    private String oOrderID;

    @JsonbProperty("FeedBack")
    private String FeedBack;

    public CancelledFeedBackItem(){}

    public String  getoOrderID()
    {
        return oOrderID;
    }
    public void setoOrderID(String oOrderID)
    {
        this.oOrderID = oOrderID;
    }
    public String  getFeedBack()
    {
        return FeedBack;
    }
    public void setFeedBack(String FeedBack)
    {
        this.FeedBack = FeedBack;
    }
}
