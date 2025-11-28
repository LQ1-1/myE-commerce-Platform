package com.lqh.ebuyplt_1001p.Controller.OrderPack2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class ExistDeliveryRecords
{
    @JsonProperty("DeliveryInfos")
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
