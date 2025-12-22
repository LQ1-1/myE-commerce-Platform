package com.lqh.ebuyplt_jsp.Controller.AdminPack;

import jakarta.json.bind.annotation.JsonbProperty;

public class ProductImagesTableItem
{
    @JsonbProperty("pID")
    private String pID;
    @JsonbProperty("pType")
    private String pImgType;
    @JsonbProperty("pImagePath")
    private String pImagePath;

    public ProductImagesTableItem(){}

    public String getpID()
    {
        return pID;
    }
    public void setpID(String pID)
    {
        this.pID = pID;
    }
    public String getpImgType()
    {
        return pImgType;
    }
    public void setpImgType(String pType)
    {
        this.pImgType = pType;
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
