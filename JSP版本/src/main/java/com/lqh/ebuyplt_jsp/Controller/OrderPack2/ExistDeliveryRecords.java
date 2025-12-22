package com.lqh.ebuyplt_jsp.Controller.OrderPack2;

import jakarta.json.bind.annotation.JsonbProperty;

import java.util.ArrayList;

public class ExistDeliveryRecords
{
    @JsonbProperty("DeliveryInfos")
    public ArrayList<DeliveryInfo> DeliveryInfos;

    public ExistDeliveryRecords()
    {
        DeliveryInfos=new ArrayList<>();
    }

    public void setDeliveryInfos(ArrayList<DeliveryInfo> ideliveryInfos)
    {
        for(int i=0;i<ideliveryInfos.size();i++)
        {
            this.DeliveryInfos.add(ideliveryInfos.get(i));
        }
    }
    public ArrayList<DeliveryInfo> getDeliveryInfos()
    {
        return this.DeliveryInfos;
    }
}
