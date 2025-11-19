package com.lqh.ebuyplt_1001p.Controller;

import com.lqh.ebuyplt_1001p.Controller.OrderCheckPack.OrderCheckStatus;
import com.lqh.ebuyplt_1001p.Controller.OrderCheckPack.OrderStatus;
import com.lqh.ebuyplt_1001p.Controller.OrderPack.Order_jsonGet;
import com.lqh.ebuyplt_1001p.Controller.OrderPack.Order_jsonSend;
import com.lqh.ebuyplt_1001p.Controller.OrderPack.*;
import com.lqh.ebuyplt_1001p.Controller.ResultPack.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


import java.sql.*;
import java.sql.SQLException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@RestController
public class OrderController
{
    private static String url="jdbc:kingbase8://localhost:54321/db_ebuyplt2";
    private static String user="system";
    private static String password="qh20050908";


    //如果该用户没有任何收货信息记录的话发起表单让用户填写收货信息
    //如果用户有填写收货信息记录让用户选择一个收货信息记录
    //提交订单的时候分成两个状态；点击购买之后发起订单，但是此时的状态是Unpaid，当点击付款之后状态才是才是Paid,如果用户选择取消订单，订单的状态转为Cancelled
    @RequestMapping("/api/OrderConfirm_DeliveryCheck")
    public String OrderConfirm_DeliveryCheck(@RequestBody Order_jsonGet orderCondition)                                 //查询用户是否有派送信息，有让用户选择，没有就让用户填写
    {
        return OrderConfirm_DeliveryCheckResult(orderCondition);
    }                                                                                                                   //购物流程将是通过加入购物车，然后在购物车里下单
    private String OrderConfirm_DeliveryCheckResult(Order_jsonGet orderCondition)
    {
        StringBuilder res=new StringBuilder();

        String uID=orderCondition.getuID();                             //下单的账户

        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="SELECT * FROM UserDeliveryInfoTable WHERE uID=?;";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1, uID);

            ResultSet rs=prepare.executeQuery();
            if(rs.next())                                              //该用户有收货信息记录
            {
                res.append("DeliveryInfoExist");
            }
            else
            {
                res.append("DeliveryInfoNotExist");
            }

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return res.toString();
    }

    @RequestMapping("/api/OrderConfirm_AmountCheck")
    public ApiResult<OrderAmountCheck_jsonSend> OrderConfirm_AmountCheck(@RequestBody  OrderAmountCheck_jsonGet orderCondition)             //检查订购数量与库存数量是否有问题，有问题就返回；没有问题就正式生成订单
    {
        OrderAmountCheck_jsonSend res=OrderConfirm_AmountCheckResult(orderCondition);
        return ApiResult.success(res);
    }
    private OrderAmountCheck_jsonSend OrderConfirm_AmountCheckResult(OrderAmountCheck_jsonGet orderCondition)
    {
        OrderAmountCheck_jsonSend res=new  OrderAmountCheck_jsonSend();
        res.setpIDs(orderCondition.pIDs);                                                                               //存储pID信息
        res.setpAmounts(orderCondition.pAmounts);                                                                       //存储商品数量
        res.setpPrices(orderCondition.pPrices);                                                                         //存储商品价格
        ArrayList<String> iFeedBacks=new ArrayList<String>(orderCondition.pIDs.size());                                 //定义数量检查的反馈数组
        for(int i=0;i<orderCondition.pIDs.size();i++){iFeedBacks.add(null);}                                            //往iFeedBacks填充数据块
        res.setorderStatus(OrderCheckStatus.Accept);                                                                                   //存储订单的检查状态

        orderCondition.InipIDsToIndex();                                                                                //调用Map的初始化函数

        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1;
            StringBuilder sb1=new StringBuilder("SELECT * FROM ProductTable WHERE ");

            for(int i=0;i<orderCondition.pIDs.size();i++)
            {
                if(i==orderCondition.pIDs.size()-1)
                {
                    sb1.append("pID="+orderCondition.pIDs.get(i));
                }
                else
                {
                    sb1.append("pID="+orderCondition.pIDs.get(i)+" OR");
                }
            }
            sql1=sb1.toString();
            ResultSet rs=con.createStatement().executeQuery(sql1);

            while(rs.next())
            {
                String pID=rs.getString("pID");                                                              //获取该记录的商品编号
                int pAmount=rs.getInt("pAmount");                                                            //获取该记录的商品库存

                if(pAmount >= orderCondition.pAmounts.get(orderCondition.pIDsToIndex.get(pID)))                         //库存数量大于订购数量,正常
                {
                    iFeedBacks.set(orderCondition.pIDsToIndex.get(pID), OrderCheckStatus.Accept);
                }
                else                                                                                                    //库存数量小于订购数量，异常
                {
                    iFeedBacks.set(orderCondition.pIDsToIndex.get(pID),OrderCheckStatus.Error);
                    res.setorderStatus(OrderCheckStatus.Error);
                }
            }
            res.setpFeedback(iFeedBacks);                                                                               //存储商品数量检查的反馈数组
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }

    @RequestMapping("/api/OrderConfirm_OrderIDGenerate")
    public String OrderConfirm_OrderIDGenerate(@RequestBody OrderWrapper rapper)                                        //订单生成的API,生成订单时默认是Unpaid状态
    {
        boolean GenerateResult=OrderConfirm_OrderIDGenerate(rapper.OrderInfo,rapper.DeliveryInfo);
        if(GenerateResult==true)
        {
         return OrderCheckStatus.Accept;
        }
        else
        {
            return OrderCheckStatus.Error;
        }
    }
    private boolean OrderConfirm_OrderIDGenerate(OrderAmountCheck_jsonGet orderCondition,OrderDeliveryList orderDeliveryInfo)
    {
        boolean res=true;
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String oOrderDate = currentDateTime.format(formatter);                                                      //下单的时间(8位)
            String oOrderDateTime = currentDateTime.format(formatter1);    //精确到hhMMss的时间                                             //下单时间(14位)

            String sql1="SELECT GetCurrentNumber(?);";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,oOrderDate);
            ResultSet rs=prepare.executeQuery();
            long sequence=-1;
            if(rs.next())
            {
                sequence=rs.getInt(1);
            }
            StringBuilder OrderID=new StringBuilder();                          //最终的订单编号
            OrderID.append(oOrderDate);                                         //前8位，下单的日期YYYYmmDD
            OrderID.append(GenerateOrderID_9digit(sequence));                   //后9位，该日的序列号，互斥访问该项数据

            String oOrdererID=orderCondition.uID;                               //下单者的账号

            //true表示插入成功，false表示插入失败
            boolean OrderGeneralInfoResult=InsertOrderGeneralInfoTable(OrderID.toString(),oOrdererID);      //往OrderGeneralInfoTable里面插入该订单信息
            boolean OrderBasicInfoResult=InsertOrderBasicInfoTable(OrderID.toString(),oOrderDateTime);      //往OrderBasicInfoTable里面插入该订单信息
            boolean OrdererInfoResult=InsertOrdererInfoTable(OrderID.toString(),orderDeliveryInfo.getuContactPersonName(),orderDeliveryInfo.getuContactPersonGender(),orderDeliveryInfo.getuContactPersonEmail());//往OrdererInfoTable里面插入信息
            boolean OrderDeliveryInfoResult=InsertOrderDeliveryInfo(OrderID.toString(),orderDeliveryInfo.getuDeliveryAddress(),orderDeliveryInfo.getoPostalCode(),orderDeliveryInfo.getuContactPersonPhone(),orderDeliveryInfo.getoDeliveryNote());
            boolean OrderProductInfoTableResult=InsertOrderProductInfoTable(OrderID.toString(),orderCondition.pIDs,orderCondition.pPrices,orderCondition.pAmounts);

            if(OrderGeneralInfoResult==false || OrderBasicInfoResult==false || OrdererInfoResult==false || OrderDeliveryInfoResult==false || OrderProductInfoTableResult==false)
            {
                res=false;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
    private String GenerateOrderID_9digit(long iSequence)
    {
        StringBuilder res=new StringBuilder(String.valueOf(iSequence));
        while(res.length()<9)
        {
            res.append("0");
        }
        return res.toString();
    }
    private boolean InsertOrderGeneralInfoTable(String OrderID,String OrdererID)
    {
        boolean res=false;
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="INSERT INTO OrderGeneralInfo (OrderID,OrdererID) VALUES (?,?);";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1, OrderID);
            prepare.setString(2, OrdererID);
            int row=prepare.executeUpdate();
            if(row>0)
            {
                res=true;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
    private boolean InsertOrderBasicInfoTable(String OrderID,String oDate)
    {
        boolean res=false;
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="INSERT INTO OrderBasicInfo (OrderID,oDate,oStatus) VALUES (?,?,?);";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1, OrderID);
            prepare.setString(2, oDate);OrderStatus OrStatus=new OrderStatus();
            prepare.setString(3, OrStatus.EnumToString.get(OrderStatus.OrderStatusEnum.Unpaid));
            int row=prepare.executeUpdate();
            if(row>0)
            {
                res=true;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
    private boolean InsertOrdererInfoTable(String OrderID,String oReceiverName,String oReceieverGender,String oReceieverEmail)
    {
        boolean res=false;
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="INSERT INTO OrdererInfoTable(oOrderID,oReceiverName,oReceieverGender,oReceieverEmail)VALUES (?,?,?,?);";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1, OrderID);
            prepare.setString(2, oReceiverName);
            prepare.setString(3, oReceieverGender);
            prepare.setString(4, oReceieverEmail);
            int row=prepare.executeUpdate();
            if(row>0)
            {
                res=true;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
    private boolean InsertOrderDeliveryInfo(String OrderID,String oDeliveryAddress,String oPostalCode,String oContactPhone,String oDeliveryNote)
    {
        boolean res=false;
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="INSERT INTO OrderDeliveryInfo()VALUES(?,?,?,?,?);";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1, OrderID);
            prepare.setString(2, oDeliveryAddress);
            prepare.setString(3, oPostalCode);
            prepare.setString(4, oContactPhone);
            prepare.setString(5, oDeliveryNote);

            int row=prepare.executeUpdate();
            if(row>0)
            {
                res=true;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
    private boolean InsertOrderProductInfoTable(String OrderID,ArrayList<String>pIDs,ArrayList<Double>oPrices,ArrayList<Integer>oAmounts)
    {
        boolean res=false;
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            for(int i=0;i<pIDs.size();i++)
            {
                String sql1="INSERT INTO OrderProductInfoTable(oOrderID,pID,oPrice,oAmount)VALUES(?,?,?,?);";
                PreparedStatement prepare=con.prepareStatement(sql1);
                prepare.setString(1,OrderID);
                prepare.setString(2,pIDs.get(i));
                prepare.setString(3,String.valueOf(oPrices.get(i)));
                prepare.setString(4,String.valueOf(oAmounts.get(i)));
                int row=prepare.executeUpdate();
                if(row>0)
                {
                    res=true;
                }
                else
                {
                    res=false;
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }

    @RequestMapping("/api/OrderConfirm_OrderPaid")                  //订单已付款的API
    public String OrderConfirm_OrderPaid(@RequestBody OrderPaid_jsonGet paidCondition)
    {
        boolean res=OrderConfirm_OrderPaidResult(paidCondition);
        if(res==true)
        {
            return OrderCheckStatus.Accept;
        }
        else
        {
            return OrderCheckStatus.Error;
        }
    }
    private boolean OrderConfirm_OrderPaidResult(OrderPaid_jsonGet paidCondition)
    {
        boolean res=false;

        OrderStatus OrStatus=new OrderStatus();
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="UPDATE OrderBasicInfoTable SET oStatus=? WHERE oOrderID=?;";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,OrStatus.EnumToString.get(OrderStatus.OrderStatusEnum.Paid));
            prepare.setString(2,paidCondition.getoOrderID());
            int row=prepare.executeUpdate();
            if(row>0)
            {
                res=true;
            }
            else
            {
                res=false;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }

    @RequestMapping("/api/OrderConfirm_OrderCancelled")
    public String OrderConfirm_OrderCancelled(@RequestBody OrderCancelled_jsonGet CancelledCondition)   //取消单个订单的API，当在下单之后的某一段时间内可以取消订单(1hour),超过之后就无法取消
    {
        boolean res=OrderConfirm_OrderCancelledResult(CancelledCondition);
        if(res==true)           //订单在1小时之内可以取消
        {
            OrderStatus orderStatus=new OrderStatus();
            boolean updateres=OrderBasicInfoTableUpdate(CancelledCondition.getOrderId(),orderStatus.EnumToString.get(OrderStatus.OrderStatusEnum.Cancelled));
            if(updateres==true)
            {
                return OrderCheckStatus.Accept;                    //取消成功
            }
            else
            {
                return OrderCheckStatus.ErrorFromServer;          //取消失败,服务器的问题
            }
        }
        else
        {
            return OrderCheckStatus.Error;                        //取消失败,下单时间超过1h
        }
    }
    private boolean OrderConfirm_OrderCancelledResult(OrderCancelled_jsonGet CancelledCondition)
    {
        boolean res=false;
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="SELECT oDate FROM OrderBasicInfoTable WHERE oOrderID=?;";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,CancelledCondition.getOrderId());
            ResultSet rs=prepare.executeQuery();
            if(rs.next())
            {
                String oDate=rs.getString("oDate");

                LocalDateTime currentDateTime = LocalDateTime.now();//当前的时间
                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                LocalDateTime orderDateTime = LocalDateTime.parse(oDate,formatter1);//下单的时间

                long hoursDiffer=ChronoUnit.HOURS.between(orderDateTime,currentDateTime);
                if(Math.abs(hoursDiffer)==0)          //时间差在1h之内会被取整为0,该订单可以取消
                {
                    res=true;
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
    public boolean OrderBasicInfoTableUpdate(String oOrderID,String oStatus)
    {
        boolean res=false;
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="UPDATE OrderBasicInfoTable SET oStatus=? WHERE oOrderID=?";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,oStatus);
            prepare.setString(2,oOrderID);
            int row=prepare.executeUpdate();
            if(row>0)
            {
                res=true;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }

    @RequestMapping("/api/OrderConfirm_OrderMultipleCancelled")
    public ApiResult<OrderMultipleCancelled_jsonSend>OrderConfirm_OrderMultipleCancelled(@RequestBody OrderMultipleCancelled_jsonGet CancelledCondition)        //多个订单取消的API
    {

    }
    public OrderMultipleCancelled_jsonSend OrderConfirm_OrderMultipleCancelledResult(OrderMultipleCancelled_jsonGet CancelledCondition)
    {
        OrderMultipleCancelled_jsonSend res=new OrderMultipleCancelled_jsonSend();
        res.setoOrderIDs(CancelledCondition.oOrderIDs);                                 //往res里面填入数据
        res.IniMapping();                                                               //完成oOrderID名到反馈缩影的映射

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            StringBuilder sql1=new StringBuilder("SELECT oOrderID,oDate FROM OrderBasicInfoTable WHERE ");
            for(int i=0;i<res.oOrderIDs.size();i++)
            {
                if(i==res.oOrderIDs.size()-1)
                {
                    sql1.append("oOrderID="+res.oOrderIDs.get(i)+" AND ");
                }
                else
                {
                    sql1.append("oOrderID="+res.oOrderIDs.get(i)+" ;");
                }
            }
            ResultSet rs=con.prepareStatement(sql1.toString()).executeQuery();
            while(rs.next())
            {
                String oID=rs.getString("oOrderID");
                String oDate=rs.getString("oDate");

                LocalDateTime orderDateTime = LocalDateTime.parse(oDate,formatter);

                long hoursDiff=ChronoUnit.HOURS.between(orderDateTime,currentDateTime);
                if(Math.abs(hoursDiff)==0)          //差距在一个小时之内，该订单可以取消
                {

                }

            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }



}
