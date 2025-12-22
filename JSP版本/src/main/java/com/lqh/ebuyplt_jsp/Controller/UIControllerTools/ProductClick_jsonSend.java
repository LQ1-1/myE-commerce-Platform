package com.lqh.ebuyplt_jsp.Controller.UIControllerTools;

import java.util.ArrayList;

public class ProductClick_jsonSend              //图片返回全部的展示图
{
    private String pID;
    private String pName;
    private String pType;
    private double pDiscount;
    private double pPrice;
    private String pProducer;
    private String pReleaseDate;
    private String pInfo;
    private int pInventory;
    private String pStatus;
    private ArrayList<String>pImagePaths;                                                                               //该商品的图片路径

    public ProductClick_jsonSend()
    {
        this.pImagePaths=new ArrayList<String>();
    }

    public String getpID()
    {
        return pID;
    }
    public String  getpName()
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
    public int getpInventory()
    {
        return pInventory;
    }
    public String getpStatus()
    {
        return pStatus;
    }
    public ArrayList<String>getpImagePaths()
    {
        return pImagePaths;
    }

    public void setpID(String pID)
    {
        this.pID=pID;
    }
    public void setpName(String pName)
    {
        this.pName=pName;
    }
    public void setpType(String pType)
    {
        this.pType=pType;
    }
    public void setpDiscount(double pDiscount)
    {
        this.pDiscount=pDiscount;
    }
    public void setpPrice(double pPrice)
    {
        this.pPrice=pPrice;
    }
    public void setpProducer(String pProducer)
    {
        this.pProducer=pProducer;
    }
    public void setpReleaseDate(String pReleaseDate)
    {
        this.pReleaseDate=pReleaseDate;
    }
    public void setpInfo(String pInfo)
    {
        this.pInfo=pInfo;
    }
    public void setpInventory(int pInventory)
    {
        this.pInventory=pInventory;
    }
    public void  setpStatus(String pStatus)
    {
        this.pStatus=pStatus;
    }
    public void setpImagePaths(ArrayList<String> IpIcon_paths)
    {
        for(String i:IpIcon_paths)
        {
            this.pImagePaths.add(i);
        }
    }
}
