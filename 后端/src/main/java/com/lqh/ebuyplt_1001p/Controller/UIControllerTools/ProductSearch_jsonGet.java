package com.lqh.ebuyplt_1001p.Controller.UIControllerTools;

public class ProductSearch_jsonGet
{
    //用户输入的关键词，根据描述模糊查询包括（商品名，商品商品类型，生产商，商品描述信息）
    private String SeachDesciption;

    //筛选条件，没有勾选或者填写的话默认为空
    private String pID;                                     //直接通过商品ID来查找

    private String pType;                                   //筛选商品类别

    private double pPrice_f;                                //筛选价格区间(前),只有这个，默认筛选条件是价格大于等于这个数值
    private double pPrice_r;                                //筛选价格区间(后)

    private String pProducer;                               //筛选生产商

    //日期筛选格式YYYY-MM-DD
    private String pReleaseDate_f;                          //筛选发售时间区间(前)
    private String pReleaseDate_r;                          //筛选发售时间区间(后)

    private String pInfo;                                   //通过描述信息筛选

    public ProductSearch_jsonGet(){}
    public String getSeachDesciption()
    {
        return SeachDesciption;
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

    public boolean FilterOpen()
    {
        if(pID.length()!=0 || pType.length()!=0 || pPrice_f+pPrice_r>0 || pProducer.length()!=0 || pReleaseDate_f.length()!=0 || pReleaseDate_r.length()!=0 || pInfo.length()!=0)
        {
            return true;
        }
        return false;
    }
}
