package com.lqh.ebuyplt_jsp.Controller.AdminPack;

import jakarta.json.bind.annotation.JsonbProperty;

public class UserAccountTableItem
{
    @JsonbProperty("uID")
    private String uID;
    @JsonbProperty("uNickName")
    private String uNickName;
    @JsonbProperty("uPassword")
    private String uPassword;
    @JsonbProperty("uPhone")
    private String uPhone;
    @JsonbProperty("uEmail")
    private String uEmail;
    @JsonbProperty("uGender")
    private String uGender;
    @JsonbProperty("uRegisterDate")
    private String uRegisterDate;
    @JsonbProperty("uAccountType")
    private String uAccountType;
    @JsonbProperty("uAccountStatus")
    private String uAccountStatus;


    public UserAccountTableItem(){}


    public String getuID()
    {
        return uID;
    }
    public String getuNickName()
    {
        return uNickName;
    }
    public String  getuPassword()
    {
        return uPassword;
    }
    public String getuPhone()
    {
        return uPhone;
    }
    public String getuEmail()
    {
        return uEmail;
    }
    public String getuGender()
    {
        return uGender;
    }
    public String getuRegisterDate()
    {
        return uRegisterDate;
    }
    public String getuAccountType()
    {
        return uAccountType;
    }
    public String getuAccountStatus()
    {
        return uAccountStatus;
    }

    public void  setuID(String uID)
    {
        this.uID = uID;
    }
    public void setuNickName(String uNickName)
    {
        this.uNickName = uNickName;
    }
    public void setuPassword(String uPassword)
    {
        this.uPassword = uPassword;
    }
    public void setuPhone(String uPhone)
    {
        this.uPhone = uPhone;
    }
    public void setuEmail(String uEmail)
    {
        this.uEmail = uEmail;
    }
    public void setuGender(String uGender)
    {
        this.uGender = uGender;
    }
    public void setuRegisterDate(String uRegisterDate)
    {
        this.uRegisterDate = uRegisterDate;
    }
    public void setuAccountType(String uAccountType)
    {
        this.uAccountType = uAccountType;
    }
    public void setuAccountStatus(String uAccountStatus)
    {
        this.uAccountStatus = uAccountStatus;
    }
}
