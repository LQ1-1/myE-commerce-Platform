package com.lqh.ebuyplt_1001p.Controller.MerchantPack;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

public class ProductImageItem
{
    @JsonProperty("pImgType")
    private String pImgType;

    private MultipartFile pImagePath;

    public ProductImageItem(){}

    public String getpImgType()
    {
        return pImgType;
    }
    public void setpImgType(String pImgType)
    {
        this.pImgType = pImgType;
    }
    public MultipartFile getpImagePath()
    {
        return pImagePath;
    }
    public void setpImagePath(MultipartFile pImagePath)
    {
        this.pImagePath = pImagePath;
    }
}
