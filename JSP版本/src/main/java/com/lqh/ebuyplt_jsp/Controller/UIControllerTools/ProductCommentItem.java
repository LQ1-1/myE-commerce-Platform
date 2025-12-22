package com.lqh.ebuyplt_jsp.Controller.UIControllerTools;

import jakarta.json.bind.annotation.JsonbProperty;


public class ProductCommentItem
{
    @JsonbProperty("cID")
    private String cID;
    @JsonbProperty("uID")
    private String uID;
    @JsonbProperty("pID")
    private String pID;
    @JsonbProperty("rReplyID")
    private String rReplyID;
    @JsonbProperty("cContent")
    private String cContent;
    @JsonbProperty("cDate")
    private String cDate;

    public ProductCommentItem(){}

    public String getcID() {
        return cID;
    }

    public void setcID(String cID) {
        this.cID = cID;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getrReplyID() {
        return rReplyID;
    }

    public void setrReplyID(String rReplyID) {
        this.rReplyID = rReplyID;
    }

    public String getcContent() {
        return cContent;
    }

    public void setcContent(String cContent) {
        this.cContent = cContent;
    }

    public String getcDate() {
        return cDate;
    }

    public void setcDate(String cDate) {
        this.cDate = cDate;
    }
}
