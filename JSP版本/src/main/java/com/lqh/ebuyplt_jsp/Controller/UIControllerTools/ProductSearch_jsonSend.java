package com.lqh.ebuyplt_jsp.Controller.UIControllerTools;

public class ProductSearch_jsonSend                 //图片只返回一个
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
    private String pImagePath;                                                                                          //商品图标路径

    public ProductSearch_jsonSend(){}

    //get函数
    public String getpID() {
        return pID;
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
    public int getpInventory()
    {
        return pInventory;
    }
    public String getpStatus()
    {
        return pStatus;
    }
    public String getpImagePath() {return pImagePath;}

    //set函数
    public void setpID(String pID)
    {
        this.pID = pID;
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
    public void setpImagePath(String pIcon_path) {this.pImagePath = pIcon_path;}
}
