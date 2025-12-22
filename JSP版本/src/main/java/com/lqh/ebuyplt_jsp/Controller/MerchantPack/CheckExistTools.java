package com.lqh.ebuyplt_1001p.Controller.MerchantPack;

public class CheckExistTools
{
    private String pImgType;
    private String pImagePath;

    public CheckExistTools(){}
    public CheckExistTools(String pImgType,String pImagePath)
    {
        this.pImgType=pImgType;
        this.pImagePath=pImagePath;
    }

    public String getpImgType()
    {
        return pImgType;
    }
    public void setpImgType(String pImgType)
    {
        this.pImgType = pImgType;
    }
    public String getpImagePath()
    {
        return pImagePath;
    }
    public void setpImagePath(String pImagePath)
    {
        this.pImagePath = pImagePath;
    }
}
