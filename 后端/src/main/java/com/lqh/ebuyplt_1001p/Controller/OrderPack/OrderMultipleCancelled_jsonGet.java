package com.lqh.ebuyplt_1001p.Controller.OrderPack;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderMultipleCancelled_jsonGet
{
    public ArrayList<String>oOrderIDs;                      //前端只需要传递要取消的订单编号即可,用户只可以看到他自己的订单编号

    public OrderMultipleCancelled_jsonGet()
    {
        oOrderIDs=new ArrayList<String>();
    }
}
