package com.lqh.ebuyplt_jsp.Controller.JSONparameter;

public class UserLogin
{
    private String uID;
    private String uPassword;
    public UserLogin(){}

    public String getuID()
    {
        return uID;
    }
    public String getuPassword()
    {
        return uPassword;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }
}
