package com.lqh.ebuyplt_jsp.Controller.UIControllerTools;

import jakarta.json.bind.annotation.JsonbProperty;

//当用户点击商品选项，前端发送请求
public class ProductClick_jsonGet
{
    @JsonbProperty("pID")
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
