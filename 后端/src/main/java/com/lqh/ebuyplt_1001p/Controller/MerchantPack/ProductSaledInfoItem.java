package com.lqh.ebuyplt_1001p.Controller.MerchantPack;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lqh.ebuyplt_1001p.Controller.OrderPack2.DeliveryInfo;
import com.lqh.ebuyplt_1001p.Controller.OrderPack2.ProductItem;

public class ProductSaledInfoItem
{
    //该订购的商品属于那个订单
    @JsonProperty("oOrderID")
    private String oOrderID;

    //订购数量，订购价格，派送状态
    @JsonProperty("OrderProductInfoTableItem")
    public ProductItem OrderProductInfoTableItem;

    //订购者的账号,订购者的收货信息
    @JsonProperty("DeliveryInfoItem")
    public DeliveryInfo DeliveryInfoItem;//这里的uID就是订购者的账号

    //订购的时间
    @JsonProperty("oDate")
    private String oDate;

    @JsonProperty("oStatus")
    private String oStatus;

    public String getoDate() {
        return oDate;
    }
    public void setoDate(String oDate)
    {
        this.oDate = oDate;
    }
    public ProductItem getOrderProductInfoTableItem()
    {
        return OrderProductInfoTableItem;
    }
    public void setOrderProductInfoTableItem(ProductItem other)
    {
        OrderProductInfoTableItem.setoPrice(other.getoPrice());
        OrderProductInfoTableItem.setpID(other.getpID());
        OrderProductInfoTableItem.setpAmount(other.getpAmount());
        OrderProductInfoTableItem.setoProductDeliveryStatus(other.getoProductDeliveryStatus());
    }
    public DeliveryInfo getDeliveryInfoItem()
    {
        return DeliveryInfoItem;
    }
    public void setDeliveryInfoItem(DeliveryInfo other)
    {
        DeliveryInfoItem.setoDeliveryNote(other.getoDeliveryNote());
        DeliveryInfoItem.setuDeliveryAddress(other.getuDeliveryAddress());
        DeliveryInfoItem.setuContactPersonPhone(other.getuContactPersonPhone());
        DeliveryInfoItem.setoPostalCode(other.getoPostalCode());
        DeliveryInfoItem.setuID(other.getuID());
        DeliveryInfoItem.setuContactPersonGender(other.getuContactPersonGender());
        DeliveryInfoItem.setuContactPersonName(other.getuContactPersonName());
        DeliveryInfoItem.setuContactPersonEmail(other.getuContactPersonEmail());
    }
    public String getoOrderID()
    {
        return oOrderID;
    }
    public void setoOrderID(String oOrderID)
    {
        this.oOrderID = oOrderID;
    }
    public String getoStatus()
    {
        return oStatus;
    }
    public void setoStatus(String oStatus)
    {
        this.oStatus = oStatus;
    }
}
