package com.lqh.ebuyplt_1001p.Controller.MerchantPack;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductOnSale_jsonGet
{
    @JsonProperty("pName")
    private String pName;

    @JsonProperty("pType")
    private String pType;

    @JsonProperty("pDiscount")
    private double pDiscount;

    @JsonProperty("pPrice")
    private double pPrice;

    @JsonProperty("pProducer")
    private String pProducer;

    @JsonProperty("pReleaseDate")
    private String pReleaseDate;

    @JsonProperty("pInfo")
    private String pInfo;

    @JsonProperty("pInventory")
    private int pInventory;

    @JsonProperty("pStatus")
    private String pStatus;

    public ProductOnSale_jsonGet(){}

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
