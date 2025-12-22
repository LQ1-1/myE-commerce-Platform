package com.lqh.ebuyplt_jsp.Controller.OrderPack2;

import jakarta.json.bind.annotation.JsonbProperty;

//商品信息(服务器返回给用户)
public class ProductItem_jsonSend
{
    @JsonbProperty("pID")
    private String pID;         //订购的商品的编号

    @JsonbProperty("pAmount")
    private int pAmount;        //订购的数量

    @JsonbProperty("pFeedback")
    private String pFeedback;   //该货物的反馈

    public String getpID()
    {
        return pID;
    }
    public void setpID(String pID)
    {
        this.pID = pID;
    }
    public int getpAmount()
    {
        return pAmount;
    }
    public void setpAmount(int pAmount)
    {
        this.pAmount = pAmount;
    }
    public String getpFeedback()
    {
        return pFeedback;
    }
    public void setpFeedback(String pFeedback)
    {
        this.pFeedback = pFeedback;
    }
}
