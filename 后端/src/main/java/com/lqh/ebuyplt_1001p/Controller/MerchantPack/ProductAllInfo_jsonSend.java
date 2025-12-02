package com.lqh.ebuyplt_1001p.Controller.MerchantPack;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class ProductAllInfo_jsonSend
{
    @JsonProperty("AllInfo")
    public ArrayList<ProductIAllnfoItem> AllInfo;

    public ProductAllInfo_jsonSend()
    {
        this.AllInfo=new ArrayList<>();
    }

    public  ArrayList<ProductIAllnfoItem> getAllInfo()
    {
        return AllInfo;
    }
    public void setAllInfo(ArrayList<ProductIAllnfoItem> AllInfo)
    {
        if(this.AllInfo==null)
        {
            this.AllInfo=new ArrayList<>();
        }
        for(ProductIAllnfoItem item:AllInfo)
        {
            this.AllInfo.add(item);
        }
    }
}
