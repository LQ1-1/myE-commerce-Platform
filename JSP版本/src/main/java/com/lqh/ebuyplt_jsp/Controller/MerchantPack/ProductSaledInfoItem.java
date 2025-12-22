package com.lqh.ebuyplt_jsp.Controller.MerchantPack;

import jakarta.json.bind.annotation.JsonbProperty;
import com.lqh.ebuyplt_jsp.Controller.OrderPack2.DeliveryInfo;
import com.lqh.ebuyplt_jsp.Controller.OrderPack2.ProductItem;

public class ProductSaledInfoItem
{
    //该订购的商品属于那个订单
    @JsonbProperty("oOrderID")
    private String oOrderID;

    //订购数量，订购价格，派送状态
    @JsonbProperty("OrderProductInfoTableItem")
    public ProductItem OrderProductInfoTableItem;

    //订购者的账号,订购者的收货信息
    @JsonbProperty("DeliveryInfoItem")
    public DeliveryInfo DeliveryInfoItem;//这里的uID就是订购者的账号

    //订购的时间
    @JsonbProperty("oDate")
    private String oDate;

    @JsonbProperty("oStatus")
    private String oStatus;

    public ProductSaledInfoItem()
    {
        this.DeliveryInfoItem=new DeliveryInfo();
        this.OrderProductInfoTableItem=new ProductItem();
    }

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
