package com.lqh.ebuyplt_1001p.Controller.MerchantPack;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductImageItem
{
    @JsonProperty("pImgType")
    private String pImgType;
    @JsonProperty("pImagePath")
    private String pImagePath;

    public ProductImageItem(){}

    public String getpImgType()
    {
        return pImgType;
    }
    public void setpImgType(String pImgType)
    {
        this.pImgType = pImgType;
    }
    public String getpImagePath()
    {
        return pImagePath;
    }
    public void setpImagePath(String pImagePath)
    {
        this.pImagePath = pImagePath;
    }
}
