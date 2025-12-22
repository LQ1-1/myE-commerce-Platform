package com.lqh.ebuyplt_jsp.Controller.OrderPack2;

import jakarta.json.bind.annotation.JsonbProperty;
import java.util.*;

public class OrderRecord_jsonSend
{
    @JsonbProperty("OrderRecordList")
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
