package com.lqh.ebuyplt_1001p.Controller.UIControllerTools;

import java.util.ArrayList;

public class ProductClick_jsonSend
{
    private String pID;
    private String pName;
    private String pType;
    private double pDiscount;
    private double pPrice;
    private String pProducer;
    private String pReleaseDate;
    private String pInfo;
    private ArrayList<String>pIcon_paths;                                                                               //该商品的图片路径

    public ProductClick_jsonSend()
    {
        this.pIcon_paths=new ArrayList<String>();
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
    public ArrayList<String>getpIcon_paths()
    {
        return pIcon_paths;
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
    public void setpIcon_paths(ArrayList<String> IpIcon_paths)
    {
        for(String i:IpIcon_paths)
        {
            this.pIcon_paths.add(i);
        }
    }
}
