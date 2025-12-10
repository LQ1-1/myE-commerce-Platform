package com.lqh.ebuyplt_1001p.Controller.OrderPack2;

import com.fasterxml.jackson.annotation.JsonProperty;

//收货信息接收表
public class DeliveryInfo
{
    @JsonProperty("uID")
    private String uID;

    @JsonProperty("uDeliveryAddress")
    private String uDeliveryAddress;

    @JsonProperty("uContactPersonName")
    private String uContactPersonName;              //也是收货人的名字

    @JsonProperty("uContactPersonPhone")
    private String uContactPersonPhone;             //收货人电话

    @JsonProperty("uContactPersonGender")
    private String uContactPersonGender;            //收货人性别

    @JsonProperty("uContactPersonEmail")
    private String uContactPersonEmail;             //收货人邮箱         --NEW

    @JsonProperty("oPostalCode")
    private String oPostalCode;                     //收货人邮编         --NEW

    @JsonProperty("oDeliveryNote")
    private String oDeliveryNote;                   //送货邮编           --NEW

    @JsonProperty("uDIndex")
    private int uDIndex;                            //标记这是第几个的送货记录 --NEW 2025-12-10

    public int getuDIndex() {
        return uDIndex;
    }

    public void setuDIndex(int uDIndex) {
        this.uDIndex = uDIndex;
    }

    public DeliveryInfo(){}

    public String getuID()
    {
        return uID;
    }
    public void setuID(String uID)
    {
        this.uID = uID;
    }
    public String getuDeliveryAddress()
    {
        return uDeliveryAddress;
    }
    public void setuDeliveryAddress(String uDeliveryAddress)
    {
        this.uDeliveryAddress = uDeliveryAddress;
    }
    public String getuContactPersonName()
    {
        return uContactPersonName;
    }
    public  void setuContactPersonName(String uContactPersonName)
    {
        this.uContactPersonName = uContactPersonName;
    }
    public String getuContactPersonPhone()
    {
        return uContactPersonPhone;
    }
    public void setuContactPersonPhone(String uContactPersonPhone)
    {
        this.uContactPersonPhone = uContactPersonPhone;
    }
    public String getuContactPersonGender()
    {
        return uContactPersonGender;
    }
    public void setuContactPersonGender(String uContactPersonGender)
    {
        this.uContactPersonGender = uContactPersonGender;
    }
    public String getuContactPersonEmail()
    {
        return uContactPersonEmail;
    }
    public void setuContactPersonEmail(String uContactPersonEmail)
    {
        this.uContactPersonEmail = uContactPersonEmail;
    }
    public String getoPostalCode()
    {
        return oPostalCode;
    }
    public void setoPostalCode(String oPostalCode)
    {
        this.oPostalCode = oPostalCode;
    }
    public String getoDeliveryNote()
    {
        return oDeliveryNote;
    }
    public void setoDeliveryNote(String oDeliveryNote)
    {
        this.oDeliveryNote = oDeliveryNote;
    }
}
