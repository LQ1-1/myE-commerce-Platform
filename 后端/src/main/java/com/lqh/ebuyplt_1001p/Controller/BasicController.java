package com.lqh.ebuyplt_1001p.Controller;

import com.lqh.ebuyplt_1001p.Controller.AdminPack.AdminStatus;
import com.lqh.ebuyplt_1001p.Controller.AdminPack.UserAccountTableItem;
import com.lqh.ebuyplt_1001p.Controller.AdminPack.UserDeliveryInfoTableItem;
import com.lqh.ebuyplt_1001p.Controller.JSONparameter.UserLogin;
import com.lqh.ebuyplt_1001p.Controller.JSONparameter.UserRegistration;
import com.lqh.ebuyplt_1001p.Controller.BasicControllerTools.*;
import com.lqh.ebuyplt_1001p.Controller.ResultPack.ApiResult;
import jakarta.servlet.Registration;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;


import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class BasicController
{
    //数据库的信息
    private static String url="jdbc:kingbase8://localhost:54321/db_ebuyplt2";
    private static String user="system";
    private static String password="qh20050908";

    //账号的状态与类型模板
    private static UserAccountStatus UAS=new UserAccountStatus();
    private static UserAccountType UAT=new UserAccountType();

    //    @CrossOrigin(origins="http://192.168.66.94:8082")
    @CrossOrigin(origins="*")
    @RequestMapping("/api/Welcome")
    public String Hello()
    {
        return "Welcome to EBuyPlt";
    }

    //处理登录函数1
    //手动请求参数
    //    @CrossOrigin(origins="http://192.168.66.94:8082")
    @CrossOrigin(origins="*")
    @RequestMapping("/api/Login_Manual")
    public String Login_Manual(HttpServletRequest request)
    {
        String UserAccount = request.getParameter("uID");
        String UserPassword = request.getParameter("uPassword");

//        System.out.println("获取信息:"+UserAccount);
//        System.out.println("获取信息:"+UserPassword);

        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql="select uPassword,uAccountType,uAccountStatus from UserAccountTable where uID=?";

            PreparedStatement prepare=con.prepareStatement(sql);
            prepare.setString(1,UserAccount);

            ResultSet rs=prepare.executeQuery();
            if(rs.next())
            {
                String getPasswordHash=rs.getString("uPassword");
                String getAccountType=rs.getString("uAccountType");
                String getAccountStatus=rs.getString("uAccountStatus");

//                System.out.println("查询信息:"+getPasswordHash);
//                System.out.println("查询信息:"+getAccountType);
//                System.out.println("查询信息:"+getAccountStatus);

                rs.close();
                prepare.close();
                con.close();

                switch(UAS.StringToEnum.get(getAccountStatus))
                {
                    case NoSuchStatus:                                                                                  //无此状态
                        return UAS.EnumToString.get(UserAccountStatus.UserAccountStatusEnum.NoSuchStatus);
                    case Normal:                                                                                        //正常状态
                        if(getPasswordHash.equals(UserPassword))                             //密码正确
                        {
                            switch(UAT.StringToEnum.get(getAccountType))
                            {
                                case NoSuchType:                                  //无此类型
                                    return UAT.EnumToString.get(UserAccountType.UserAccountTypeEnum.NoSuchType);
                                case RegularUser:                                 //普通用户
                                    return UAT.EnumToString.get(UserAccountType.UserAccountTypeEnum.RegularUser);
                                case Merchant:                                    //商家
                                    return UAT.EnumToString.get(UserAccountType.UserAccountTypeEnum.Merchant);
                                case Administrator:                               //管理员
                                    return UAT.EnumToString.get(UserAccountType.UserAccountTypeEnum.Administrator);
                            }
                        }
                        else                                                                //密码错误
                        {
                            return "Wrong Password";
                        }
                    case Banned:
                        return UAS.EnumToString.get(UserAccountStatus.UserAccountStatusEnum.Banned);
                    case Deleted:
                        return UAS.EnumToString.get(UserAccountStatus.UserAccountStatusEnum.Deleted);
                }
            }
            else
            {
                rs.close();
                prepare.close();
                con.close();
                return "No such Account";
            }
            rs.close();
            prepare.close();
            con.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return "Failed";
    }


    //处理函数2
    //参数是json形式,通过请求体传递json格式
    //    @CrossOrigin(origins="http://192.168.66.94:8082")
    @CrossOrigin(origins="*")
    @RequestMapping("/api/Login_RequestBody")
    public String Login_RequestBody(@RequestBody UserLogin userlogin)
    {
        String UserAccount = userlogin.getuID();
        String UserPassword = userlogin.getuPassword();

//        System.out.println("获取信息:"+UserAccount);
//        System.out.println("获取信息:"+UserPassword);

        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql="select uPassword,uAccountType,uAccountStatus from UserAccountTable where uID=?";

            PreparedStatement prepare=con.prepareStatement(sql);
            prepare.setString(1,UserAccount);

            ResultSet rs=prepare.executeQuery();
            if(rs.next())
            {
                String getPasswordHash=rs.getString("uPassword");
                String getAccountType=rs.getString("uAccountType");
                String getAccountStatus=rs.getString("uAccountStatus");

                System.out.println("查询信息:"+getPasswordHash);
                System.out.println("查询信息:"+getAccountType);
                System.out.println("查询信息:"+getAccountStatus);

                rs.close();
                prepare.close();
                con.close();

                switch(UAS.StringToEnum.get(getAccountStatus))
                {
                    case NoSuchStatus:                                                                                  //无此状态
                        return UAS.EnumToString.get(UserAccountStatus.UserAccountStatusEnum.NoSuchStatus);
                    case Normal:                                                                                        //正常状态
                        if(getPasswordHash.equals(UserPassword))                             //密码正确
                        {
                            switch(UAT.StringToEnum.get(getAccountType))
                            {
                                case NoSuchType:                                  //无此类型
                                    return UAT.EnumToString.get(UserAccountType.UserAccountTypeEnum.NoSuchType);
                                case RegularUser:                                 //普通用户
                                    return UAT.EnumToString.get(UserAccountType.UserAccountTypeEnum.RegularUser);
                                case Merchant:                                    //商家
                                    return UAT.EnumToString.get(UserAccountType.UserAccountTypeEnum.Merchant);
                                case Administrator:                               //管理员
                                    return UAT.EnumToString.get(UserAccountType.UserAccountTypeEnum.Administrator);
                            }
                        }
                        else                                                                //密码错误
                        {
                            return "Wrong Password";
                        }
                    case Banned:
                        return UAS.EnumToString.get(UserAccountStatus.UserAccountStatusEnum.Banned);
                    case Deleted:
                        return UAS.EnumToString.get(UserAccountStatus.UserAccountStatusEnum.Deleted);
                }
            }
            else
            {
                rs.close();
                prepare.close();
                con.close();
                return "No such Account";
            }
            rs.close();
            prepare.close();
            con.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return "Failed";
    }

//    @CrossOrigin(origins="http://192.168.66.94:8082")
    @CrossOrigin(origins="*")
    @RequestMapping("/api/Registration")
    public String Registration(@RequestBody UserRegistration userregistration)
    {
        String res=RegistrationResult(userregistration);
        return res;
    }
    private String RegistrationResult(UserRegistration userregistration)
    {
        //api连接测试//
        System.out.println(userregistration.getuID());
        System.out.println(userregistration.getuNickName());
        System.out.println(userregistration.getuPassword());
        System.out.println(userregistration.getuPhone());
        System.out.println(userregistration.getuEmail());
        System.out.println(userregistration.getuGender());
        System.out.println(userregistration.getuRegisterDate());
        System.out.println(userregistration.getuAccountType());
        System.out.println(userregistration.getuAccountStatus());

        //api连接测试//


        StringBuilder res=new StringBuilder("");
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="SELECT RegistrationResult(?,?,?," +
                    "?,?,?," +
                    "?,?,?);";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,userregistration.getuID());
            prepare.setString(2,userregistration.getuNickName());
            prepare.setString(3,userregistration.getuPassword());
            prepare.setString(4,userregistration.getuPhone());
            prepare.setString(5,userregistration.getuEmail());
            prepare.setString(6,userregistration.getuGender());
            prepare.setString(7,userregistration.getuRegisterDate());
            prepare.setString(8,userregistration.getuAccountType());
            prepare.setString(9,userregistration.getuAccountStatus());
            ResultSet rs=prepare.executeQuery();
            if(rs.next())
            {
                System.out.println(rs.getString(1));
                res.append(rs.getString(1));
            }
            rs.close();
            prepare.close();
            con.close();
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


    @CrossOrigin(origins="*")
    @RequestMapping("/api/GetUserAccountInfo")
    public ApiResult<UserAccountTableItem> GetUserAccountInfo(@RequestBody UserAccountTableItem para)   //获取用户信息的接口，请求的时候只用填写uID
    {
        return ApiResult.success(GetUserAccountInfoResult(para));
    }
    private UserAccountTableItem GetUserAccountInfoResult(UserAccountTableItem para)
    {
        UserAccountTableItem res=new UserAccountTableItem();
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="SELECT * FROM UserAccountTable WHERE uID=?;";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,para.getuID());
            ResultSet rs=prepare.executeQuery();
            if(rs.next())
            {
                res.setuID(rs.getString("uID"));
                res.setuNickName(rs.getString("uNickName"));
                res.setuPassword(rs.getString("uPassword"));
                res.setuPhone(rs.getString("uPhone"));
                res.setuEmail(rs.getString("uEmail"));
                res.setuGender(rs.getString("uGender"));
                res.setuRegisterDate(rs.getString("uRegisterDate"));
                res.setuAccountType(rs.getString("uAccountType"));
                res.setuAccountStatus(rs.getString("uAccountStatus"));
            }
            rs.close();
            prepare.close();
            con.close();                //使用完之后连接一定要记得关闭！！！
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
    @RequestMapping("/api/SetUserAccountInfo")
    public String  UserAccountTableUpdate(@RequestBody UserAccountTableItem newInfo)     //更新UserAccount信息的接口
    {
        if(UserAccountTableUpdateResult(newInfo)) {   return AdminStatus.Success;    }
        else {   return AdminStatus.Fail;    }
    }
    private boolean UserAccountTableUpdateResult(UserAccountTableItem newInfo)
    {
        boolean res=false;
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con= DriverManager.getConnection(url,user,password);

            String sql1="UPDATE UserAccountTable SET UserAccountTable.uNickName=?, UserAccountTable.uPassword=?, " +
                    "UserAccountTable.uPhone=?, UserAccountTable.uEmail=?, UserAccountTable.uGender=?, " +
                    "UserAccountTable.uAccountType=?, UserAccountTable.uAccountStatus=? WHERE UserAccountTable.uID=?;";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1, newInfo.getuNickName());
            prepare.setString(2, newInfo.getuPassword());
            prepare.setString(3, newInfo.getuPhone());
            prepare.setString(4, newInfo.getuEmail());
            prepare.setString(5, newInfo.getuGender());
            prepare.setString(6, newInfo.getuAccountType());
            prepare.setString(7, newInfo.getuAccountStatus());
            prepare.setString(8, newInfo.getuID());
            int row=prepare.executeUpdate();
            if(row>0)
            {
                res=true;
            }
            prepare.close();
            con.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }


    @CrossOrigin(origins="*")
    @RequestMapping("/api/GetUserDeliveryInfo")
    public ApiResult<ArrayList<UserDeliveryInfoTableItem>> GetUserDeliveryInfo(@RequestBody UserDeliveryInfoTableItem para)     //获取所有的该用户的收货记录，请求的时候只用填写uID,返回的数据中uDIndex不向用户显示
    {
        return ApiResult.success(GetUserDeliveryInfoResult(para));
    }
    private ArrayList<UserDeliveryInfoTableItem> GetUserDeliveryInfoResult(UserDeliveryInfoTableItem para)
    {
        ArrayList<UserDeliveryInfoTableItem> res=new ArrayList<>();
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="SELECT * FROM UserDeliveryInfoTable WHERE uID=?;";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,para.getuID());
            ResultSet rs=prepare.executeQuery();
            while(rs.next())
            {
                UserDeliveryInfoTableItem item=new UserDeliveryInfoTableItem();
                item.setuID(rs.getString("uID"));
                item.setuDIndex(rs.getInt("uDIndex"));
                item.setuDeliveryAddress(rs.getString("uDeliveryAddress"));
                item.setuContactPersonName(rs.getString("uContactPersonName"));
                item.setuContactPersonPhone(rs.getString("uContactPersonPhone"));
                item.setuContactPersonGender(rs.getString("uContactPersonGender"));
                item.setuContactPersonEmail(rs.getString("oReceieverEmail"));
                item.setoPostalCode(rs.getString("oPostalCode"));
                item.setoDeliveryNote(rs.getString("oDeliveryNote"));
                res.add(item);
            }
            rs.close();
            prepare.close();
            con.close();
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
    @RequestMapping("/api/SetUserDeliveryInfo")
    public String  SetUserDeliveryInfo(@RequestBody UserDeliveryInfoTableItem para)     //更新其中一个收货信息
    {
        if(SetUserDeliveryInfoResult(para)){    return AdminStatus.Success;    }
        else {   return AdminStatus.Fail;    }
    }
    private boolean SetUserDeliveryInfoResult(UserDeliveryInfoTableItem para)
    {
        boolean res=false;
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con= DriverManager.getConnection(url,user,password);

            String sql1="UPDATE UserDeliveryInfoTable SET UserDeliveryInfoTable.uDeliveryAddress=?, UserDeliveryInfoTable.uContactPersonName=?, " +
                    "UserDeliveryInfoTable.uContactPersonPhone=?, UserDeliveryInfoTable.uContactPersonGender=?, " +
                    "UserDeliveryInfoTable.oReceieverEmail=?, UserDeliveryInfoTable.oPostalCode=?, " +
                    "UserDeliveryInfoTable.oDeliveryNote=? " +
                    "WHERE UserDeliveryInfoTable.uID=? AND UserDeliveryInfoTable.uDIndex=?;";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,para.getuDeliveryAddress());
            prepare.setString(2,para.getuContactPersonName());
            prepare.setString(3,para.getuContactPersonPhone());
            prepare.setString(4,para.getuContactPersonGender());
            prepare.setString(5,para.getuContactPersonEmail());
            prepare.setString(6,para.getoPostalCode());
            prepare.setString(7,para.getoDeliveryNote());
            prepare.setString(8,para.getuID());
            prepare.setInt(9,para.getuDIndex());
            int row=prepare.executeUpdate();
            if(row>0)
            {
                res=true;
            }
            prepare.close();
            con.close();
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

    //添加新的收货信息则使用/api/OrderConfirm_NewDeliveryRecord这个接口
}

/*

.yml 是 YAML 格式文件的扩展名，YAML 的全称是 “YAML Ain't Markup Language”（YAML 不是标记语言），
它是一种轻量级的数据序列化格式，
主要用于配置文件（比如项目配置、环境变量、工具配置等），
因其语法简洁、可读性强而被广泛使用。

 */
