package com.lqh.ebuyplt_jsp.Controller.UIControllerTools;

import jakarta.json.bind.annotation.JsonbProperty;


public class ProductCommentItem_jsonSend extends ProductCommentItem
{
    @JsonbProperty("Commenter")
    private String Commenter;
    @JsonbProperty("Recipient")
    private String Recipient;
    @JsonbProperty("cLikes")
    private int cLikes;

    public ProductCommentItem_jsonSend(){}

    public String getCommenter() {
        return Commenter;
    }

    public void setCommenter(String commenter) {
        Commenter = commenter;
    }

    public String getRecipient() {
        return Recipient;
    }

    public void setRecipient(String recipient) {
        Recipient = recipient;
    }

    public int getcLikes() {
        return cLikes;
    }

    public void setcLikes(int likes) {
        cLikes = likes;
    }
}
