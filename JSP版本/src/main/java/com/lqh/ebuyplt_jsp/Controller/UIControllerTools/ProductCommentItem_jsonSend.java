package com.lqh.ebuyplt_1001p.Controller.UIControllerTools;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductCommentItem_jsonSend extends ProductCommentItem
{
    @JsonProperty("Commenter")
    private String Commenter;
    @JsonProperty("Recipient")
    private String Recipient;
    @JsonProperty("cLikes")
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
