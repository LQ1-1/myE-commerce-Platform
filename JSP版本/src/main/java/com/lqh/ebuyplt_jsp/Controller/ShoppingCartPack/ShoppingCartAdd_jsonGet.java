package com.lqh.ebuyplt_jsp.Controller.ShoppingCartPack;

public class ShoppingCartAdd_jsonGet
{
    private String uID;
    private String pID;
    private int cAmount;

    public ShoppingCartAdd_jsonGet(){}

    public String getuID()
    {
        return uID;
    }
    public String getpID()
    {
        return pID;
    }
    public int getcAmount()
    {
        return cAmount;
    }
    public void setuID(String uID)
    {
        this.uID = uID;
    }
    public void setpID(String pID)
    {
        this.pID = pID;
    }
    public void setcAmount(int cAmount)
    {
        this.cAmount = cAmount;
    }
}
