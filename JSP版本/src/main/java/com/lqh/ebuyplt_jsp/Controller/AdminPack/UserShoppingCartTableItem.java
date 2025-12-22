package com.lqh.ebuyplt_1001p.Controller.AdminPack;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserShoppingCartTableItem
{
    @JsonProperty("uID")
    private String uID;
    @JsonProperty("pID")
    private String pID;
    @JsonProperty("cAmount")
    private int cAmount;

    public UserShoppingCartTableItem(){}

    public String getuID(){return uID;}
    public void  setuID(String uID){this.uID = uID;}
    public String getpID(){return pID;}
    public void setpID(String pID){this.pID = pID;}
    public int getcAmount(){return cAmount;}
    public void setcAmount(int cAmount){this.cAmount = cAmount;}
}
