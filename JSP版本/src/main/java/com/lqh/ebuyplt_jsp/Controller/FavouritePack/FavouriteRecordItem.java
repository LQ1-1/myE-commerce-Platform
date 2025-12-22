package com.lqh.ebuyplt_jsp.Controller.FavouritePack;

public class FavouriteRecordItem
{
    private String pID;
    private String pName;
    private String pType;
    private double pPrice;
    private String pImagesPath;

    public FavouriteRecordItem(){}
    public String getpID()
    {
        return pID;
    }
    public void setpID(String pID)
    {
        this.pID = pID;
    }
    public String  getpName()
    {
        return pName;
    }
    public void setpName(String pName)
    {
        this.pName = pName;
    }
    public String getpType()
    {
        return pType;
    }
    public void setpType(String pType)
    {
        this.pType = pType;
    }
    public double getpPrice()
    {
        return pPrice;
    }
    public void setpPrice(double pPrice)
    {
        this.pPrice = pPrice;
    }
    public String getpImagesPath()
    {
        return pImagesPath;
    }
    public void setpImagesPath(String pImagesPath)
    {
        this.pImagesPath = pImagesPath;
    }
}
