package com.lqh.ebuyplt_1001p.Controller.UIControllerTools;

import com.fasterxml.jackson.annotation.JsonProperty;

//当用户点击商品选项，前端发送请求
public class ProductClick_jsonGet
{
    @JsonProperty("pID")
    private String pID;
    public ProductClick_jsonGet(){}
    public String  getpID()
    {
        return pID;
    }
    public void setpID(String pID)
    {
        this.pID = pID;
    }
}
