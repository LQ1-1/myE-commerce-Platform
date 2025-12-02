package com.lqh.ebuyplt_1001p.Controller.MerchantPack;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class ProductIAllnfoItem
{
    @JsonProperty("pID")
    private String pID;

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

    @JsonProperty("pThumbnail")
    public String pThumbnail;//返回资源路径

    @JsonProperty("pShowcaseImageList")
    public ArrayList<String> pShowcaseImageList;//返回资源路径

    public ProductIAllnfoItem()
    {
        this.pShowcaseImageList = new ArrayList<>();
    }

    public String getpID()
    {
        return pID;
    }
    public void setpID(String pID)
    {
        this.pID = pID;
    }
    public String getpName()
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
    public double getpDiscount()
    {
        return pDiscount;
    }
    public void setpDiscount(double pDiscount)
    {
        this.pDiscount = pDiscount;
    }
    public double getpPrice()
    {
        return pPrice;
    }
    public void setpPrice(double pPrice)
    {
        this.pPrice = pPrice;
    }
    public String getpProducer()
    {
        return pProducer;
    }
    public void setpProducer(String pProducer)
    {
        this.pProducer = pProducer;
    }
    public String getpReleaseDate()
    {
        return pReleaseDate;
    }
    public void setpReleaseDate(String pReleaseDate)
    {
        this.pReleaseDate = pReleaseDate;
    }
    public String getpInfo()
    {
        return pInfo;
    }
    public void setpInfo(String pInfo)
    {
        this.pInfo = pInfo;
    }
    public int getpInventory()
    {
        return pInventory;
    }
    public void setpInventory(int pInventory)
    {
        this.pInventory = pInventory;
    }
    public String getpStatus()
    {
        return pStatus;
    }
    public void setpStatus(String pStatus)
    {
        this.pStatus = pStatus;
    }
    public String getpThumbnail()
    {
        return pThumbnail;
    }
    public void setpThumbnail(String ipThumbnail)
    {
        this.pThumbnail = ipThumbnail;
    }
    public ArrayList<String>getpShowcaseImageList()
    {
        return pShowcaseImageList;
    }
    public void setpShowcaseImageList(ArrayList<String> ipShowcaseImageList)
    {
        if(this.pShowcaseImageList == null)
        {
            this.pShowcaseImageList = new ArrayList<>();
        }
        for(String item : ipShowcaseImageList)
        {
            this.pShowcaseImageList.add(item);
        }
    }
}
