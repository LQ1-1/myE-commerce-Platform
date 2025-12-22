package com.lqh.ebuyplt_jsp.Controller.MerchantPack;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.servlet.http.Part;

public class ProductImageItem
{
    private String pImgType;

    private Part pImagePath;

    public ProductImageItem(){}

    public String getpImgType()
    {
        return pImgType;
    }
    public void setpImgType(String pImgType)
    {
        this.pImgType = pImgType;
    }
    public Part getpImagePath()
    {
        return pImagePath;
    }
    public void setpImagePath(Part pImagePath)
    {
        this.pImagePath = pImagePath;
    }
}
