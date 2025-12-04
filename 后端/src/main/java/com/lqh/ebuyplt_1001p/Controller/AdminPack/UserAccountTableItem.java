package com.lqh.ebuyplt_1001p.Controller.AdminPack;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAccountTableItem
{
    @JsonProperty("uID")
    private String uID;
    @JsonProperty("uNickName")
    private String uNickName;
    @JsonProperty("uPassword")
    private String uPassword;
    @JsonProperty("uPhone")
    private String uPhone;
    @JsonProperty("uEmail")
    private String uEmail;
    @JsonProperty("uGender")
    private String uGender;
    @JsonProperty("uRegisterDate")
    private String uRegisterDate;
    @JsonProperty("uAccountType")
    private String uAccountType;
    @JsonProperty("uAccountStatus")
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

}
