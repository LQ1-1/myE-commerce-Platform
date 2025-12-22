package com.lqh.ebuyplt_jsp.Controller.OrderPack2;

import jakarta.json.bind.annotation.JsonbProperty;

import java.util.ArrayList;

//订单信息(用户发送)
public class OrderItem
{
    @JsonbProperty("uID")
    private String uID;                                 //下单的用户

    @JsonbProperty("pProducts")
    public ArrayList<ProductItem>pProducts;            //订购的商品

    @JsonbProperty("oDeliveryInfo")
    private DeliveryInfo oDeliveryInfo;                 //收货的信息

    public OrderItem()
    {
        pProducts=new ArrayList<>();
    }

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
        for(ProductItem productItem:ipProducts)
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
