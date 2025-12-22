package com.lqh.ebuyplt_jsp.Controller.MerchantPack;

import jakarta.json.bind.annotation.JsonbProperty;
public class ProductOnSale_jsonGet
{
    @JsonbProperty("uID")
    private String uID;

    @JsonbProperty("pName")
    private String pName;

    @JsonbProperty("pType")
    private String pType;

    @JsonbProperty("pDiscount")
    private double pDiscount;

    @JsonbProperty("pPrice")
    private double pPrice;

    @JsonbProperty("pProducer")
    private String pProducer;

    @JsonbProperty("pReleaseDate")
    private String pReleaseDate;

    @JsonbProperty("pInfo")
    private String pInfo;

    @JsonbProperty("pInventory")
    private int pInventory;

    @JsonbProperty("pStatus")
    private String pStatus;

    public ProductOnSale_jsonGet(){}

    public String getuID()
    {
        return uID;
    }
    public void setuID(String uID)
    {
        this.uID = uID;
    }
    public String getpName()
    {
        return pName;
    }
    public String getpType()
    {
        return pType;
    }
    public double getpDiscount()
    {
        return pDiscount;
    }
    public double getpPrice()
    {
        return pPrice;
    }
    public String getpProducer()
    {
        return pProducer;
    }
    public String getpReleaseDate()
    {
        return pReleaseDate;
    }
    public String getpInfo()
    {
        return pInfo;
    }
    public  int getpInventory()
    {
        return pInventory;
    }
    public String getpStatus()
    {
        return pStatus;
    }
    public void setpName(String pName)
    {
        this.pName = pName;
    }
    public void setpType(String pType)
    {
        this.pType = pType;
    }
    public void setpDiscount(double pDiscount)
    {
        this.pDiscount = pDiscount;
    }
    public void setpPrice(double pPrice)
    {
        this.pPrice = pPrice;
    }
    public void setpProducer(String pProducer)
    {
        this.pProducer = pProducer;
    }
    public void setpReleaseDate(String pReleaseDate)
    {
        this.pReleaseDate = pReleaseDate;
    }
    public void setpInfo(String pInfo)
    {
        this.pInfo = pInfo;
    }
    public void setpInventory(int pInventory)
    {
        this.pInventory = pInventory;
    }
    public void setpStatus(String pStatus)
    {
        this.pStatus = pStatus;
    }
}
