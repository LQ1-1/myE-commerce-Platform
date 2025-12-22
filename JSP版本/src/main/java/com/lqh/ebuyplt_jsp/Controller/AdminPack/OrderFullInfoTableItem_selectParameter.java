package com.lqh.ebuyplt_jsp.Controller.AdminPack;

import jakarta.json.bind.annotation.JsonbProperty;

import java.util.ArrayList;

public class OrderFullInfoTableItem_selectParameter extends OrderFullInfoTableItem
{
    @JsonbProperty("DateF")
    private String DateF;
    @JsonbProperty("DateR")
    private String DateR;
    @JsonbProperty("SearchInput")
    private String SearchInput;
    ArrayList<String>oStatuses;



    public OrderFullInfoTableItem_selectParameter()
    {
        super();
        oStatuses = new ArrayList<>();
    }

    public String getDateF() {
        return DateF;
    }

    public void setDateF(String dateF) {
        DateF = dateF;
    }

    public String getDateR() {
        return DateR;
    }

    public void setDateR(String dateR) {
        DateR = dateR;
    }

    public String getSearchInput() {
        return SearchInput;
    }

    public void setSearchInput(String searchInput) {
        SearchInput = searchInput;
    }

    public ArrayList<String> getoStatuses() {
        return oStatuses;
    }

    public void setoStatuses(ArrayList<String> oStatuses) {
        for(String oStatus:oStatuses)
        {
            this.oStatuses.add(oStatus);
        }
    }
}
