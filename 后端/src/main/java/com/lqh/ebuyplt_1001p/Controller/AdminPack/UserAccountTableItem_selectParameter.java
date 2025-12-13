package com.lqh.ebuyplt_1001p.Controller.AdminPack;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class UserAccountTableItem_selectParameter extends UserAccountTableItem
{
    @JsonProperty("SearchInput")
    private String SearchInput;


    @JsonProperty("DateL")
    private String DateF;
    @JsonProperty("DateR")
    private String DateR;
    public ArrayList<String> uAccountTypes;
    public ArrayList<String> uAccountStatuses;

    public UserAccountTableItem_selectParameter()
    {
        super();
        uAccountTypes=new ArrayList<>();
        uAccountStatuses=new ArrayList<>();
    }

    public ArrayList<String> getuAccountTypes() {
        return uAccountTypes;
    }

    public void setuAccountTypes(ArrayList<String> uAccountTypes) {
        for(String str:uAccountTypes)
        {
            this.uAccountTypes.add(str);
        }
    }

    public ArrayList<String> getuAccountStatuess() {
        return uAccountStatuses;
    }

    public void setuAccountStatus(ArrayList<String> uAccountStatuses) {
        for(String str:uAccountStatuses)
        {
            this.uAccountStatuses.add(str);
        }
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
}
