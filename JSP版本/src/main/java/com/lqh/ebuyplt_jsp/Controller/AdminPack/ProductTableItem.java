package com.lqh.ebuyplt_jsp.Controller.AdminPack;

import jakarta.json.bind.annotation.JsonbProperty;

public class ProductTableItem
{
    @JsonbProperty("pID")
    private String pID;
    @JsonbProperty("pName")
    private String pName;
    @JsonbProperty("pType")
    private String pType;
    @JsonbProperty("pDiscount")
    private double pDiscount;
    @JsonbProperty("pPrice")
    private double pPrice;
    @JsonbProperty("pProducer")
    private String pProducer;
    @JsonbProperty("pReleaseDate")
    private String pReleaseDate;
    @JsonbProperty("pInfo")
    private String pInfo;
    @JsonbProperty("pInventory")
    private int pInventory;
    @JsonbProperty("pStatus")
    private String pStatus;
    @JsonbProperty("pImagePath")
    private String pImagePath;
    public  ProductTableItem() {}

    public String getpID(){return pID;}
    public String getpName(){return pName;}
    public String getpType(){return pType;}
    public double getpDiscount(){return pDiscount;}
    public double getpPrice(){return pPrice;}
    public String getpProducer(){return pProducer;}
    public String getpReleaseDate(){return pReleaseDate;}
    public String getpInfo(){return pInfo;}
    public int getpInventory(){return pInventory;}
    public String getpStatus(){return pStatus;}

    public void setpID(String pID){this.pID=pID;}
    public void setpName(String pName){this.pName=pName;}
    public  void setpType(String pType){this.pType=pType;}
    public void setpDiscount(double pDiscount){this.pDiscount=pDiscount;}
    public void setpPrice(double pPrice){this.pPrice=pPrice;}
    public void setpProducer(String pProducer){this.pProducer=pProducer;}
    public void setpReleaseDate(String pReleaseDate){this.pReleaseDate=pReleaseDate;}
    public void setpInfo(String pInfo){this.pInfo=pInfo;}
    public void setpInventory(int pInventory){this.pInventory=pInventory;}
    public void setpStatus(String pStatus){this.pStatus=pStatus;}

    public String getpImagePath() {
        return pImagePath;
    }

    public void setpImagePath(String pImagePath) {
        this.pImagePath = pImagePath;
    }
}

