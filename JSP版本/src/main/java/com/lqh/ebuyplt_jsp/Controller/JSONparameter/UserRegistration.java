package com.lqh.ebuyplt_jsp.Controller.JSONparameter;

public class UserRegistration
{
    private String uID;
    private String uNickName;
    private String uPassword;
    private String uPhone;
    private String uEmail;
    private String uGender;
    private String uRegisterDate;
    private String uAccountType;
    private String uAccountStatus;

    public UserRegistration(){}

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

    public void setuID(String uID) {
        this.uID = uID;
    }

    public void setuNickName(String uNickName) {
        this.uNickName = uNickName;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public void setuGender(String uGender) {
        this.uGender = uGender;
    }

    public void setuRegisterDate(String uRegisterDate) {
        this.uRegisterDate = uRegisterDate;
    }

    public void setuAccountType(String uAccountType) {
        this.uAccountType = uAccountType;
    }

    public void setuAccountStatus(String uAccountStatus) {
        this.uAccountStatus = uAccountStatus;
    }
}
