package com.lqh.ebuyplt_1001p.Controller.UIControllerTools;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductCommentItem
{
    @JsonProperty("cID")
    private String cID;
    @JsonProperty("uID")
    private String uID;
    @JsonProperty("pID")
    private String pID;
    @JsonProperty("rReplyID")
    private String rReplyID;
    @JsonProperty("cContent")
    private String cContent;
    @JsonProperty("cDate")
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
