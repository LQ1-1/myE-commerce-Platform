package com.lqh.ebuyplt_1001p.Controller.MerchantPack;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class ProductSaledInfo_jsonSend
{
    @JsonProperty("SaledItemList")
    public ArrayList<ProductSaledInfoItem> SaledItemList;
    public ProductSaledInfo_jsonSend()
    {
        this.SaledItemList = new ArrayList<>();
    }

    public ArrayList<ProductSaledInfoItem> getSaledItemList()
    {
        return this.SaledItemList;
    }
    public void setSaledItemList(ArrayList<ProductSaledInfoItem> other)
    {
        if(this.SaledItemList==null)
        {
            this.SaledItemList = new ArrayList<>();
        }
        for(ProductSaledInfoItem item:other)
        {
            this.SaledItemList.add(item);
        }
    }
}
