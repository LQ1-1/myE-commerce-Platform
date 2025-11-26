package com.lqh.ebuyplt_1001p.Controller.UIControllerTools;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class ProductSearch_jsonGet
{
    //用户输入的关键词，根据描述模糊查询包括（商品名，商品商品类型，生产商，商品描述信息）
    @JsonProperty("SearchDesciption")
    private String SearchDesciption;

    //筛选条件，没有勾选或者填写的话默认为空
    @JsonProperty("pID")
    private String pID;                                     //直接通过商品ID来查找

    @JsonProperty("pType")
    private String pType;                                   //筛选商品类别

    @JsonProperty("pPrice_f")
    private double pPrice_f;                                //筛选价格区间(前),只有这个，默认筛选条件是价格大于等于这个数值
    @JsonProperty("pPrice_r")
    private double pPrice_r;                                //筛选价格区间(后)

    @JsonProperty("pProducer")
    private String pProducer;                               //筛选生产商

    //日期筛选格式YYYY-MM-DD
    @JsonProperty("pReleaseDate_f")
    private String pReleaseDate_f;                          //筛选发售时间区间(前)
    @JsonProperty("pReleaseDate_r")
    private String pReleaseDate_r;                          //筛选发售时间区间(后)

    @JsonProperty("pInfo")
    private String pInfo;                                   //通过描述信息筛选

    public ProductSearch_jsonGet(){}
    public String getSearchDesciption()
    {
        return SearchDesciption;
    }

    public String getpID() {
        return pID;
    }
    public String getpType()
    {
        return pType;
    }
    public double getpPrice_f()
    {
        return pPrice_f;
    }
    public double getpPrice_r()
    {
        return pPrice_r;
    }
    public String getpProducer()
    {
        return pProducer;
    }
    public String getpReleaseDate_f()
    {
        return pReleaseDate_f;
    }
    public String getpReleaseDate_r()
    {
        return pReleaseDate_r;
    }
    public String getpInfo()
    {
        return pInfo;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }
    public void setpType(String pType)
    {
        this.pType = pType;
    }
    public void setSearchDesciption(String searchDesciption) {
        SearchDesciption = searchDesciption;
    }
    public void setpPrice_f(double pPrice_f)
    {
        this.pPrice_f = pPrice_f;
    }
    public void setpPrice_r(double pPrice_r)
    {
        this.pPrice_r = pPrice_r;
    }
    public void setpProducer(String pProducer)
    {
        this.pProducer = pProducer;
    }
    public void setpInfo(String pInfo)
    {
        this.pInfo = pInfo;
    }
    public void setpReleaseDate_f(String pReleaseDate_f)
    {
        this.pReleaseDate_f = pReleaseDate_f;
    }
    public void setpReleaseDate_r(String pReleaseDate_r)
    {
        this.pReleaseDate_r = pReleaseDate_r;
    }

    public boolean FilterOpen()
    {
        if(pID.length()!=0 || pType.length()!=0 || pPrice_f+pPrice_r>0 || pProducer.length()!=0 || pReleaseDate_f.length()!=0 || pReleaseDate_r.length()!=0 || pInfo.length()!=0)
        {
            return true;
        }
        return false;
    }
}
