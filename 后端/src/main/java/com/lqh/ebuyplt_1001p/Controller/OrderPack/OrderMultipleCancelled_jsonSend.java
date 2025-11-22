package com.lqh.ebuyplt_1001p.Controller.OrderPack;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderMultipleCancelled_jsonSend
{
    public ArrayList<String> oOrderIDs;
    public ArrayList<String>fCancelledFeedBack;
    public String CancelledStatus;

    public OrderMultipleCancelled_jsonSend()
    {
        oOrderIDs=new ArrayList<String>();
        fCancelledFeedBack=new ArrayList<String>();

        IDsToIntegerMapping=new HashMap<String,Integer>();
    }

    public HashMap<String,Integer>IDsToIntegerMapping;                  //oOrderID和所处的索引
    public void IniMapping()                                            //初始化IDsToIntegerMapping
    {
        for(int i=0;i<oOrderIDs.size();i++)
        {
            IDsToIntegerMapping.put(oOrderIDs.get(i),i);
        }
    }

    public void setoOrderIDs(ArrayList<String> oOrderIDsI)      //初始化oOrderIDs数组
    {
        for(String ID:oOrderIDsI)
        {
            this.oOrderIDs.add(ID);
        }
    }

}
