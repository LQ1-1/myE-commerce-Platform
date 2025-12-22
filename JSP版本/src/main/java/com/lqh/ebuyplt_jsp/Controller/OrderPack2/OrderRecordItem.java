package com.lqh.ebuyplt_1001p.Controller.OrderPack2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class OrderRecordItem
{
    @JsonProperty("oOrderID")
    private String oOrderID;
    @JsonProperty("pProducts")
    public ArrayList<ProductItem>pProducts;
    @JsonProperty("oDate")
    private String oDate;
    @JsonProperty("oStatus")
    private String oStatus;
    @JsonProperty("DeliveryInfo")
    public DeliveryInfo DeliveryInfo;

    public OrderRecordItem()
    {
        this.pProducts = new ArrayList<>();
        this.DeliveryInfo=new DeliveryInfo();
    }
    public String getoOrderID()
    {
        return oOrderID;
    }
    public void setoOrderID(String oOrderID)
    {
        this.oOrderID = oOrderID;
    }
    public ArrayList<ProductItem>getpProducts()
    {
        return pProducts;
    }
    public void setpProducts (ArrayList<ProductItem>pProducts)
    {
        for(ProductItem productItem : pProducts)
        {
            this.pProducts.add(productItem);
        }
    }
    public String getoDate()
    {
        return oDate;
    }
    public void setoDate(String oDate)
    {
        this.oDate = oDate;
    }
    public String getoStatus()
    {
        return oStatus;
    }
    public void setoStatus(String oStatus)
    {
        this.oStatus = oStatus;
    }
    public  DeliveryInfo getDeliveryInfo()
    {
        return DeliveryInfo;
    }
    public  void setDeliveryInfo(DeliveryInfo deliveryInfo)
    {
        this.DeliveryInfo.setoDeliveryNote(deliveryInfo.getoDeliveryNote());
        this.DeliveryInfo.setoPostalCode(deliveryInfo.getoPostalCode());
        this.DeliveryInfo.setuDeliveryAddress(deliveryInfo.getuDeliveryAddress());
//        this.DeliveryInfo.setuID(deliveryInfo.getuID());
        this.DeliveryInfo.setuContactPersonEmail(deliveryInfo.getuContactPersonEmail());
        this.DeliveryInfo.setuContactPersonName(deliveryInfo.getuContactPersonName());
        this.DeliveryInfo.setuContactPersonPhone(deliveryInfo.getuContactPersonPhone());
        this.DeliveryInfo.setuContactPersonGender(deliveryInfo.getuContactPersonGender());
    }
}
