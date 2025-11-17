package com.lqh.ebuyplt_1001p.Controller;

import com.lqh.ebuyplt_1001p.Controller.OrderPack.Order_jsonGet;
import com.lqh.ebuyplt_1001p.Controller.OrderPack.Order_jsonSend;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


import java.sql.*;
import java.sql.SQLException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class OrderController
{
    private static String url="jdbc:kingbase8://localhost:54321/db_ebuyplt2";
    private static String user="system";
    private static String password="qh20050908";


    //如果该用户没有任何收货信息记录的话发起表单让用户填写收货信息
    //如果用户有填写收货信息记录让用户选择一个收货信息记录
    //提交订单的时候分成两个状态；点击购买之后发起订单，但是此时的状态是Unpaid，当点击付款之后状态才是才是Paid
    @RequestMapping("/api/OrderConfirm_DeliveryCheck")
    public String OrderConfirm_DeliveryCheck(@RequestBody Order_jsonGet orderCondition)
    {
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        String oOrderDate = currentDateTime.format(formatter);          //下单的时间
        return OrderConfirm_DeliveryCheckResult(orderCondition);
    }
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

}
