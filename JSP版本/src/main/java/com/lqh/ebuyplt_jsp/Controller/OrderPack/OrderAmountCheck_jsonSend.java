package com.lqh.ebuyplt_1001p.Controller.OrderPack;

import java.util.ArrayList;

public class OrderAmountCheck_jsonSend
{
    public ArrayList<String> pIDs;                                                                                      //各个商品的商品编号
    public ArrayList<Integer> pAmounts;                                                                                 //各个商品的订购数量
    public ArrayList<Double>pPrices;                                                                                    //各个商品的购买单价
    public ArrayList<String>pFeedback;

    public String orderStatus;                                                                                          //如果订购数量超过库存量的话，这里就是ERROR,否则就是Accept

    public OrderAmountCheck_jsonSend()
    {
        this.pIDs=new ArrayList<>();
        this.pAmounts=new ArrayList<>();
        this.pPrices=new ArrayList<>();
        this.pFeedback=new ArrayList<>();
    }

    public ArrayList<String> getpIDs()
    {
        return pIDs;
    }
    public ArrayList<Integer> getpAmounts()
    {
        return pAmounts;
    }
    public ArrayList<Double> getpPrices()
    {
        return pPrices;
    }
    public ArrayList<String>getpFeedback()
    {
        return pFeedback;
    }
    public String getorderStatus()
    {
        return orderStatus;
    }

    public void setpIDs(ArrayList<String> IpIDs)
    {
        for(String i:IpIDs)
        {
            this.pIDs.add(i);
        }
    }
    public void setpAmounts(ArrayList<Integer> ipAmounts)
    {
        for(int i:ipAmounts)
        {
            this.pAmounts.add(i);
        }
    }
    public void setpPrices(ArrayList<Double> ipPrices)
    {
        for(double i:ipPrices)
        {
            this.pPrices.add(i);
        }
    }
    public void setpFeedback(ArrayList<String> IpFeedback)
    {
        for(String i:IpFeedback)
        {
            this.pFeedback.add(i);
        }
    }
    public void setorderStatus(String orderStatus)
    {
        this.orderStatus=orderStatus;
    }
}
