package com.lqh.ebuyplt_1001p.Controller.MerchantPack;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class ProductInfoUpdate_jsonGet
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

    private String oldThumbnailPicURL;//新的等于旧的就是没有改变
    private String newThumbnailPicURL;//如果用户删除目前的缩略图换上新的缩略图的话，这里就是空的，新的图片放在pThumbnail

    //如果用户上传新的缩略图就是这个
    public ProductImageItem pThumbnail;

    public ArrayList<String>oldShowcaseImagesURL;//用来确定哪些是要被删除的图片
    public ArrayList<String>newShowcaseImagesURL;//旧有新没有则表明该图片被移除，新的图片直接放在pShowcaseImageList里面

    //如果用户有上传新的展示图就放在这里
    public ArrayList<ProductImageItem> pShowcaseImageList;

    public ProductInfoUpdate_jsonGet()
    {
        this.pShowcaseImageList = new ArrayList<>();
        this.oldShowcaseImagesURL = new ArrayList<>();
        this.newShowcaseImagesURL = new ArrayList<>();

        this.pThumbnail=new ProductImageItem();
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
    public ProductImageItem getpThumbnail()
    {
        return pThumbnail;
    }
    public void setpThumbnail(ProductImageItem ipThumbnail)
    {
        if(this.pThumbnail == null)
        {
            this.pThumbnail = new ProductImageItem();
        }
        this.pThumbnail.setpImgType(ipThumbnail.getpImgType());
        this.pThumbnail.setpImagePath(ipThumbnail.getpImagePath());
    }
    public ArrayList<ProductImageItem>getpShowcaseImageList()
    {
        return pShowcaseImageList;
    }
    public void setpShowcaseImageList(ArrayList<ProductImageItem> ipShowcaseImageList)
    {
        if(this.pShowcaseImageList == null)
        {
            this.pShowcaseImageList = new ArrayList<>();
        }
        for(ProductImageItem item : ipShowcaseImageList)
        {
            this.pShowcaseImageList.add(item);
        }
    }
    public String getOldThumbnailPicURL()
    {
        return oldThumbnailPicURL;
    }
    public void setOldThumbnailPicURL(String oldThumbnailPicURL)
    {
        this.oldThumbnailPicURL = oldThumbnailPicURL;
    }
    public String getNewThumbnailPicURL()
    {
        return newThumbnailPicURL;
    }
    public void setNewThumbnailPicURL(String newThumbnailPicURL)
    {
        this.newThumbnailPicURL = newThumbnailPicURL;
    }
    public ArrayList<String> getoldShowcaseImagesURL()
    {
        return this.oldShowcaseImagesURL;
    }
    public void setOldShowcaseImagesURL(ArrayList<String> oldShowcaseImagesURL)
    {
        for(String s : oldShowcaseImagesURL)
        {
            this.oldShowcaseImagesURL.add(s);
        }
    }
    public ArrayList<String> getNewShowcaseImagesURL()
    {
        return this.newShowcaseImagesURL;
    }
    public void setNewShowcaseImagesURL(ArrayList<String> newShowcaseImagesURL)
    {
        for(String s : newShowcaseImagesURL)
        {
            this.newShowcaseImagesURL.add(s);
        }
    }
}
