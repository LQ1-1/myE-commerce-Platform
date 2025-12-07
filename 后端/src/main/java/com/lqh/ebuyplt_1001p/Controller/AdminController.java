package com.lqh.ebuyplt_1001p.Controller;


import com.lqh.ebuyplt_1001p.Controller.AdminPack.UserAccountTableItem;
import com.lqh.ebuyplt_1001p.Controller.AdminPack.UserDeliveryInfoTableItem;
import com.lqh.ebuyplt_1001p.Controller.ResultPack.ApiResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;

@RestController
public class AdminController        //管理员控制器
{
    //数据库的信息
    private static String url="jdbc:kingbase8://localhost:54321/db_ebuyplt2";
    private static String user="system";
    private static String password="qh20050908";

    @CrossOrigin(origins="*")
    @RequestMapping("/api/AdminUserAccountTable")
    public ApiResult<ArrayList<UserAccountTableItem>> AdminUserAccountTable()
    {
        return ApiResult.success(AdminUserAccountTableResult());
    }
    private ArrayList<UserAccountTableItem> AdminUserAccountTableResult()
    {
        ArrayList<UserAccountTableItem>res=new ArrayList<>();
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(url,user,password);

            String sql1="SELECT * FROM UserAccountTable;";
            PreparedStatement prepare=con.prepareStatement(sql1);
            ResultSet rs=prepare.executeQuery();
            while(rs.next())
            {
                UserAccountTableItem item=new UserAccountTableItem();
                item.setuID(rs.getString("uID"));
                item.setuNickName(rs.getString("uNickName"));
                item.setuPassword(rs.getString("uPassword"));
                item.setuPhone(rs.getString("uPhone"));
                item.setuEmail(rs.getString("uEmail"));
                item.setuGender(rs.getString("uGender"));
                item.setuRegisterDate(rs.getString("uRegisterDate"));
                item.setuAccountType(rs.getString("uAccountType"));
                item.setuAccountStatus(rs.getString("uAccountStatus"));

                res.add(item);
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

    @CrossOrigin(origins="*")
    @RequestMapping("/api/AdminUserDeliveryInfoTable")
    public ApiResult<ArrayList<UserDeliveryInfoTableItem>> AdminUserDeliveryInfoTable()
    {
        return ApiResult.success();
    }
    private ArrayList<UserDeliveryInfoTableItem> AdminUserDeliveryInfoTableResult()
    {
        ArrayList<UserDeliveryInfoTableItem> res=new ArrayList<>();
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(url,user,password);


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
}
