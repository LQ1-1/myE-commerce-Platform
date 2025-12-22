package com.lqh.ebuyplt_jsp.Controller.mapper;

import com.lqh.ebuyplt_jsp.Controller.UIControllerTools.*;

import java.util.ArrayList;

public interface ProductCommentMapper
{
    public ArrayList<ProductCommentItem_jsonSend> getCommentofSpecificProduct(ProductCommentItem para);
    public void sendComment(ProductCommentItem para);
    public void giveLiketoComment(ProductCommentItem para);
}
