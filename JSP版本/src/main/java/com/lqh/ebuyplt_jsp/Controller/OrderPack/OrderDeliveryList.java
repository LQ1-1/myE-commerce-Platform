package com.lqh.ebuyplt_1001p.Controller.OrderPack;

public class OrderDeliveryList
{
    private String uID;
    private String uDeliveryAddress;
    private String uContactPersonName;              //也是收货人的名字
    private String uContactPersonPhone;             //收货人电话
    private String uContactPersonGender;            //收货人性别

    private String uContactPersonEmail;             //收货人邮箱         --NEW
    private String oPostalCode;                     //收货人邮编         --NEW
    private String oDeliveryNote;                   //送货邮编           --NEW

    public OrderDeliveryList() {}

    public String getuID() {
        return uID;
    }
    public String getuDeliveryAddress()
    {
        return uDeliveryAddress;
    }
    public String getuContactPersonName()
    {
        return uContactPersonName;
    }
    public String getuContactPersonPhone()
    {
        return uContactPersonPhone;
    }
    public String getuContactPersonGender()
    {
        return uContactPersonGender;
    }
    public String getuContactPersonEmail()
    {
        return uContactPersonEmail;
    }
    public String getoPostalCode()
    {
        return oPostalCode;
    }
    public String getoDeliveryNote()
    {
        return oDeliveryNote;
    }

    public void setuID(String uID)
    {
        this.uID = uID;
    }
    public void setuDeliveryAddress(String uDeliveryAddress)
    {
        this.uDeliveryAddress = uDeliveryAddress;
    }
    public void setuContactPersonName(String name)
    {
        this.uContactPersonName = name;
    }
    public void setuContactPersonPhone(String name)
    {
        this.uContactPersonPhone = name;
    }
    public void setuContactPersonGender(String gender)
    {
        this.uContactPersonGender = gender;
    }
    public void setuContactPersonEmail(String email)
    {
        this.uContactPersonEmail = email;
    }
    public void setoPostalCode(String code)
    {
        this.oPostalCode = code;
    }
    public void setoDeliveryNote(String note)
    {
        this.oDeliveryNote = note;
    }
}

//2025-11-18        UserDeliveryInfoTable表需要修改,需要添加