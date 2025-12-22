package com.lqh.ebuyplt_jsp.Controller.AdminPack;

import jakarta.json.bind.annotation.JsonbProperty;

public class UserShoppingCartTableItem
{
    @JsonbProperty("uID")
    private String uID;
    @JsonbProperty("pID")
    private String pID;
    @JsonbProperty("cAmount")
    private int cAmount;

    public UserShoppingCartTableItem(){}

    public String getuID(){return uID;}
    public void  setuID(String uID){this.uID = uID;}
    public String getpID(){return pID;}
    public void setpID(String pID){this.pID = pID;}
    public int getcAmount(){return cAmount;}
    public void setcAmount(int cAmount){this.cAmount = cAmount;}
}
