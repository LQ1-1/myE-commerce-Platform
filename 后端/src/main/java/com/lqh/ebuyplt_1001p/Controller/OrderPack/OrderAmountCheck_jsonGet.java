package com.lqh.ebuyplt_1001p.Controller.OrderPack;

import java.util.*;

public class OrderAmountCheck_jsonGet
{
    public String uID;                                                                                                  //下单的用户的账号
    public ArrayList<String> pIDs;                                                                                      //各个商品的商品编号
    public ArrayList<Integer> pAmounts;                                                                                 //各个商品的订购数量
    public ArrayList<Double>pPrices;                                                                                    //各个商品的购买单价


    public HashMap<String,Integer>pIDsToIndex;                                                                          //pID名与索引名对应
    public void InipIDsToIndex()
    {
        this.pIDsToIndex=new HashMap<String,Integer>();
        for(int i=0;i<pIDs.size();i++)
        {
            pIDsToIndex.put(pIDs.get(i),i);
        }
    }

    public OrderAmountCheck_jsonGet()
    {
        this.pIDs=new ArrayList<String>();
        this.pAmounts=new ArrayList<Integer>();
        this.pPrices=new ArrayList<Double>();
    }

}
