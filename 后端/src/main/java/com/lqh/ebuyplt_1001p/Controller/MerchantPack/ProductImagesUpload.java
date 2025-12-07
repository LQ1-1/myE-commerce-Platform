package com.lqh.ebuyplt_1001p.Controller.MerchantPack;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class ProductImagesUpload
{
    @JsonProperty("uID")
    private String uID;
    @JsonProperty("pID")
    private String pID;

    public MultipartFile cover;//缩略图
    public  MultipartFile[] gallery;//全部的展示图

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
    public MultipartFile getcover()
    {
        return cover;
    }
    public void setcover(MultipartFile cover)
    {
        this.cover = cover;
    }
    public MultipartFile[] getgallery()
    {
        return gallery;
    }
    public void setgallery(MultipartFile[] gallery)
    {
        this.gallery = gallery;
    }
}
