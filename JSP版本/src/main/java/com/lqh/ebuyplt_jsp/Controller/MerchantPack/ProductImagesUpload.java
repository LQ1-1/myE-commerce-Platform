package com.lqh.ebuyplt_jsp.Controller.MerchantPack;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.servlet.http.Part;
import lombok.Data;

@Data
public class ProductImagesUpload
{
    @JsonbProperty("uID")
    private String uID;
    @JsonbProperty("pID")
    private String pID;

    public Part cover;//缩略图
    public  Part[] gallery;//全部的展示图

    public ProductImagesUpload(){}

    public String getuID()
    {
        return uID;
    }
    public void setuID(String uID)
    {
        this.uID = uID;
    }
    public String getpID()
    {
        return pID;
    }
    public void setpID(String pID)
    {
        this.pID = pID;
    }
    public Part getcover()
    {
        return cover;
    }
    public void setcover(Part cover)
    {
        this.cover = cover;
    }
    public Part[] getgallery()
    {
        return gallery;
    }
    public void setgallery(Part[] gallery)
    {
        this.gallery = gallery;
    }
}
