package com.lqh.ebuyplt_jsp.Controller.MerchantPack;

import jakarta.json.bind.annotation.JsonbProperty;

import java.util.ArrayList;

public class ProductIAllnfoItem
{
    @JsonbProperty("pID")
    private String pID;

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

    @JsonbProperty("pThumbnail")
    public String pThumbnail;//返回资源路径

    @JsonbProperty("pShowcaseImageList")
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
