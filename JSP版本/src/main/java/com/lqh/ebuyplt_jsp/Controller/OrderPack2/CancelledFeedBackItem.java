package com.lqh.ebuyplt_1001p.Controller.OrderPack2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CancelledFeedBackItem
{
    @JsonProperty("oOrderID")
    private String oOrderID;

    @JsonProperty("FeedBack")
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
