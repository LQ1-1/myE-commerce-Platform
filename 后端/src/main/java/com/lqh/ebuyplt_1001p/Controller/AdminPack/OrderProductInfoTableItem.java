package com.lqh.ebuyplt_1001p.Controller.AdminPack;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderProductInfoTableItem
{
    @JsonProperty("oOrderID")
    private String oOrderID;
    @JsonProperty("pID")
    private String pID;
    @JsonProperty("oPrice")
    private String oPrice;
    @JsonProperty("oAmount")
    private String oAmount;

    public OrderProductInfoTableItem(){}

    public String getoAmount() {
        return oAmount;
    }

    public void setoAmount(String oAmount) {
        this.oAmount = oAmount;
    }

    public String getoPrice() {
        return oPrice;
    }

    public void setoPrice(String oPrice) {
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
