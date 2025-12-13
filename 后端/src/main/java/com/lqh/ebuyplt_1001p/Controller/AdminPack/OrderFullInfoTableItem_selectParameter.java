package com.lqh.ebuyplt_1001p.Controller.AdminPack;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class OrderFullInfoTableItem_selectParameter extends OrderFullInfoTableItem
{
    @JsonProperty("DateF")
    private String DateF;
    @JsonProperty("DateR")
    private String DateR;
    @JsonProperty("SearchInput")
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
