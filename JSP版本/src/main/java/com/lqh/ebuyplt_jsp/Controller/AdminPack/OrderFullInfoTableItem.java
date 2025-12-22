package com.lqh.ebuyplt_1001p.Controller.AdminPack;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderFullInfoTableItem
{
    //OrderGeneralInfoTable
    @JsonProperty("oOrderID")
    private String oOrderID;
    @JsonProperty("oOrdererID")
    private String oOrdererID;
    //OrderBasicInfoTable
    @JsonProperty("oDate")
    private String oDate;
    @JsonProperty("oStatus")
    private String oStatus;

    //OrdererInfoTable
    @JsonProperty("oReceiverName")
    private String oReceiverName;
    @JsonProperty("oReceieverGender")
    private String oReceieverGender;
    @JsonProperty("oReceieverEmail")
    private String oReceieverEmail;

    //OrderDeliveryInfo
    @JsonProperty("oDeliveryAddress")
    private String oDeliveryAddress;
    @JsonProperty("oPostalCode")
    private String oPostalCode;
    @JsonProperty("oContactPhone")
    private String oContactPhone;
    @JsonProperty("oDeliveryNote")
    private String oDeliveryNote;

    public String getoDeliveryNote() {
        return oDeliveryNote;
    }

    public void setoDeliveryNote(String oDeliveryNote) {
        this.oDeliveryNote = oDeliveryNote;
    }

    public String getoContactPhone() {
        return oContactPhone;
    }

    public void setoContactPhone(String oContactPhone) {
        this.oContactPhone = oContactPhone;
    }

    public String getoPostalCode() {
        return oPostalCode;
    }

    public void setoPostalCode(String oPostalCode) {
        this.oPostalCode = oPostalCode;
    }

    public String getoDeliveryAddress() {
        return oDeliveryAddress;
    }

    public void setoDeliveryAddress(String oDeliveryAddress) {
        this.oDeliveryAddress = oDeliveryAddress;
    }

    public String getoReceieverEmail() {
        return oReceieverEmail;
    }

    public void setoReceieverEmail(String oReceieverEmail) {
        this.oReceieverEmail = oReceieverEmail;
    }

    public String getoReceieverGender() {
        return oReceieverGender;
    }

    public void setoReceieverGender(String oReceieverGender) {
        this.oReceieverGender = oReceieverGender;
    }

    public String getoReceiverName() {
        return oReceiverName;
    }

    public void setoReceiverName(String oReceiverName) {
        this.oReceiverName = oReceiverName;
    }

    public String getoStatus() {
        return oStatus;
    }

    public void setoStatus(String oStatus) {
        this.oStatus = oStatus;
    }

    public String getoDate() {
        return oDate;
    }

    public void setoDate(String oDate) {
        this.oDate = oDate;
    }

    public String getoOrdererID() {
        return oOrdererID;
    }

    public void setoOrdererID(String oOrdererID) {
        this.oOrdererID = oOrdererID;
    }

    public String getoOrderID() {
        return oOrderID;
    }

    public void setoOrderID(String oOrderID) {
        this.oOrderID = oOrderID;
    }
}
