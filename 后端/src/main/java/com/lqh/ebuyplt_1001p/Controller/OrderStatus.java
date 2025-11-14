package com.lqh.ebuyplt_1001p.Controller;

import java.util.*;

public class OrderStatus
{
    public String StatusErro="NoSuchStatus";

    public String Status1="Unpaid";
    public String Status2="Paid";
    public String Status3="Submitted";
    public String Status4="Shipped";
    public String Status5="OutForDelivery";
    public String Status6="PendingReceipt";
    public String Status7="Received";

    public enum OrderStatusEnum
    {
        NoSuchStatus,
        Unpaid,
        Paid,
        Submitted,
        Shipped,
        OutForDelivery,
        PendingReceipt,
        Received,
    };

    //字符串到枚举的映射
    HashMap<String,OrderStatusEnum> StringToEnum=new HashMap<String,OrderStatusEnum>(
            Map.ofEntries(
                    Map.entry(StatusErro,OrderStatusEnum.NoSuchStatus),
                    Map.entry(Status1,OrderStatusEnum.Unpaid),
                    Map.entry(Status2,OrderStatusEnum.Paid),
                    Map.entry(Status3,OrderStatusEnum.Submitted),
                    Map.entry(Status4,OrderStatusEnum.Shipped),
                    Map.entry(Status5,OrderStatusEnum.OutForDelivery),
                    Map.entry(Status6,OrderStatusEnum.PendingReceipt),
                    Map.entry(Status7,OrderStatusEnum.Received)
            )
    );
    //枚举到字符串的映射
    HashMap<OrderStatusEnum,String> EnumToString=new HashMap<OrderStatusEnum,String>(
            Map.ofEntries(
                    Map.entry(OrderStatusEnum.NoSuchStatus,StatusErro),
                    Map.entry(OrderStatusEnum.Unpaid,Status1),
                    Map.entry(OrderStatusEnum.Paid,Status2),
                    Map.entry(OrderStatusEnum.Submitted,Status3),
                    Map.entry(OrderStatusEnum.Shipped,Status4),
                    Map.entry(OrderStatusEnum.OutForDelivery,Status5),
                    Map.entry(OrderStatusEnum.PendingReceipt,Status6),
                    Map.entry(OrderStatusEnum.Received,Status7)
            )
    );

}

/*

    public String Status1="未支付";
    public String Status2="已支付";
    public String Status3="已提交";
    public String Status4="已发货";
    public String Status5="在派送";
    public String Status6="待签收";
    public String Status7="已签收";

 */
