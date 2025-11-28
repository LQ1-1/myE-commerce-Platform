package com.lqh.ebuyplt_1001p.Controller.OrderPack2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

//订单信息(用户发送)
public class OrderItem
{
    @JsonProperty("uID")
    private String uID;                                 //下单的用户

    @JsonProperty("pProducts")
    public ArrayList<ProductItem>pProducts;            //订购的商品

    @JsonProperty("oDeliveryInfo")
    private DeliveryInfo oDeliveryInfo;                 //收货的信息

    public OrderItem(){}

    public String  getuID() {
        return uID;
    }
    public void setuID(String iuID)
    {
        this.uID = iuID;
    }

    public ArrayList<ProductItem> getpProducts()
    {
        return pProducts;
    }
    public void setpProducts (ArrayList<ProductItem> ipProducts)
    {
        for(ProductItem productItem:pProducts)
        {
            this.pProducts.add(productItem);
        }
    }

    public DeliveryInfo getoDeliveryInfo()
    {
        return oDeliveryInfo;
    }
    public void setoDeliveryInfo(DeliveryInfo oDeliveryInfo)
    {
        this.oDeliveryInfo = oDeliveryInfo;
    }
}
