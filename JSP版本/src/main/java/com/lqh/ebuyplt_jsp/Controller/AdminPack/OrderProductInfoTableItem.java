package com.lqh.ebuyplt_jsp.Controller.AdminPack;

import jakarta.json.bind.annotation.JsonbProperty;

public class OrderProductInfoTableItem
{
    @JsonbProperty("oOrderID")
    private String oOrderID;
    @JsonbProperty("pID")
    private String pID;
    @JsonbProperty("oPrice")
    private double oPrice;
    @JsonbProperty("oAmount")
    private int oAmount;
    @JsonbProperty("oProductDeliveryStatus")
    private String oProductDeliveryStatus;

    public String getoProductDeliveryStatus() {
        return oProductDeliveryStatus;
    }

    public void setoProductDeliveryStatus(String oProductDeliveryStatus) {
        this.oProductDeliveryStatus = oProductDeliveryStatus;
    }

    public OrderProductInfoTableItem(){}

    public int getoAmount() {
        return oAmount;
    }

    public void setoAmount(int oAmount) {
        this.oAmount = oAmount;
    }

    public double getoPrice() {
        return oPrice;
    }

    public void setoPrice(double oPrice) {
        this.oPrice = oPrice;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getoOrderID() {
        return oOrderID;
    }

    public void setoOrderID(String oOrderID) {
        this.oOrderID = oOrderID;
    }
}
