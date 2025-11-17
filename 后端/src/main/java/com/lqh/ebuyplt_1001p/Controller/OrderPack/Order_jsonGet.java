package com.lqh.ebuyplt_1001p.Controller.OrderPack;

public class Order_jsonGet
{
    private String pID;                                                                                                 //下单的商品
    private String uID;                                                                                                 //下单的账号
    private int oAmount;                                                                                                 //下单的数量
    private double oPrices;                                                                                              //下单时的单价

    public Order_jsonGet(){}

    public String getpID()
    {
        return pID;
    }
    public String  getuID()
    {
        return uID;
    }
    public int getoAmount()
    {
        return oAmount;
    }
    public double getoPrices()
    {
        return oPrices;
    }

    public void setpID(String pID)
    {
        this.pID = pID;
    }
    public void setuID(String uID)
    {
        this.uID = uID;
    }
    public void setoAmount(int oAmount)
    {
        this.oAmount = oAmount;
    }
    public void setoPrices(double oPrices)
    {
        this.oPrices = oPrices;
    }
}
