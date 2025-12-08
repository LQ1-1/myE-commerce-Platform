package com.lqh.ebuyplt_1001p.Controller.AdminPack;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductImagesTableItem
{
    @JsonProperty("pID")
    private String pID;
    @JsonProperty("pType")
    private String pType;
    @JsonProperty("pImagePath")
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
    public String getpType()
    {
        return pType;
    }
    public void setpType(String pType)
    {
        this.pType = pType;
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
