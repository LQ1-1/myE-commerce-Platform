package com.lqh.ebuyplt_1001p.Controller.OrderPack2;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;

public class OrderRecord_jsonSend
{
    @JsonProperty("OrderRecordList")
    public ArrayList<OrderRecordItem>OrderRecordList;
    public OrderRecord_jsonSend()
    {
        OrderRecordList=new  ArrayList<>();
    }

    public ArrayList<OrderRecordItem> getOrderRecordList()
    {
        return OrderRecordList;
    }
    public void setOrderRecordList(ArrayList<OrderRecordItem> orderRecordList)
    {
        for(OrderRecordItem item:orderRecordList)
        {
            this.OrderRecordList.add(item);
        }
    }

}
