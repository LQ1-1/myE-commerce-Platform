package com.lqh.ebuyplt_jsp.Controller.OrderPack2;

import jakarta.json.bind.annotation.JsonbProperty;

import java.util.ArrayList;

public class OrderRecordItem
{
    @JsonbProperty("oOrderID")
    private String oOrderID;
    @JsonbProperty("pProducts")
    public ArrayList<ProductItem>pProducts;
    @JsonbProperty("oDate")
    private String oDate;
    @JsonbProperty("oStatus")
    private String oStatus;
    @JsonbProperty("DeliveryInfo")
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
