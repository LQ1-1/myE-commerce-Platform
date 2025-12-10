package com.lqh.ebuyplt_1001p.Controller.AdminPack;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderProductInfoTableItem
{
    @JsonProperty("oOrderID")
    private String oOrderID;
    @JsonProperty("pID")
    private String pID;
    @JsonProperty("oPrice")
    private double oPrice;
    @JsonProperty("oAmount")
    private int oAmount;

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
