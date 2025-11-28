package com.lqh.ebuyplt_1001p.Controller.OrderPack2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

//订单信息(服务器返回给后端)
public class OrderItem_Feedback
{
    @JsonProperty("uID")
    private String uID;                                 //下单的用户

    @JsonProperty("pProducts")
    public ArrayList<ProductItem_jsonSend> pProducts;  //订购的商品

    @JsonProperty("oDeliveryInfo")
    private DeliveryInfo oDeliveryInfo;                 //收货的信息

    @JsonProperty("Status")                             //判断该订单是否可接收
    private String Status;

    @JsonProperty("oOrderID")
    private String oOrderID;

    public OrderItem_Feedback()
    {
        pProducts=new ArrayList<>();
    }

    public String getuID()
    {
        return uID;
    }
    public void setuID(String uID)
    {
        this.uID = uID;
    }

    public ArrayList<ProductItem_jsonSend> getpProducts()
    {
        return pProducts;
    }
    public void setpProducts(ArrayList<ProductItem_jsonSend> pProducts)
    {
        for(ProductItem_jsonSend i:pProducts)
        {
            this.pProducts.add(i);
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

    public String getStatus()
    {
        return Status;
    }
    public void setStatus(String Status)
    {
        this.Status = Status;
    }

    public String getoOrderID()
    {
        return oOrderID;
    }
    public void setoOrderID(String OrderID)
    {
        this.oOrderID = OrderID;
    }
}
