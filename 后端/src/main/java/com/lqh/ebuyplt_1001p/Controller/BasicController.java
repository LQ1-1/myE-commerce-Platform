package com.lqh.ebuyplt_1001p.Controller;

import com.lqh.ebuyplt_1001p.Controller.JSONparameter.UserLogin;
import com.lqh.ebuyplt_1001p.Controller.JSONparameter.UserRegistration;
import com.lqh.ebuyplt_1001p.Controller.BasicControllerTools.*;
import com.lqh.ebuyplt_1001p.Controller.ResultPack.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


import java.sql.*;
import java.sql.SQLException;

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

    @RequestMapping("/api/Welcome")
    public String Hello()
    {
        return "Welcome to EBuyPlt";
    }

    //处理登录函数1
    //手动请求参数
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
                return "No such Account";
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return "Failed";
    }


    //处理函数2
    //参数是json形式,通过请求体传递json格式
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
                return "No such Account";
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return "Failed";
    }

    @RequestMapping("/api/Registration")
    public String Registration(@RequestBody UserRegistration userregistration)
    {
        StringBuffer res=new StringBuffer();

        String uID=userregistration.getuID();
        String uNickName=userregistration.getuNickName();
        String uPassword=userregistration.getuPassword();
        String uPhone=userregistration.getuPhone();
        String uEmail=userregistration.getuEmail();
        String uGender=userregistration.getuGender();
        String uRegisterDate=userregistration.getuRegisterDate();
        String uAccountType=userregistration.getuAccountType();
        String uAccountStatus=userregistration.getuAccountStatus();

        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql="INSERT INTO UserAccountTable(" +
                    "uID,uNickName,uPassword," +
                    "uPhone,uEmail,uGender," +
                    "uRegisterDate,uAccountType,uAccountStatus)VALUES(" +
                    "?,?,?," +
                    "?,?,?," +
                    "?,?,?);";
            PreparedStatement prepare=con.prepareStatement(sql);
            prepare.setString(1,uID);                                       //用户账号
            prepare.setString(2,uNickName);                                 //用户昵称
            prepare.setString(3,uPassword);                                 //用户密码
            prepare.setString(4,uPhone);                                    //用户电话
            prepare.setString(5,uEmail);                                    //用户邮件
            prepare.setString(6,uGender);                                   //用户性别
            prepare.setString(7,uRegisterDate);                             //用户注册日期
            prepare.setString(8,uAccountType);                              //用户类别
            prepare.setString(9,uAccountStatus);                            //用户状态

            int rows=prepare.executeUpdate();
            if(rows>0)                                                                    //rows > 0 表示至少插入了1条记录，插入成功
            {
                prepare.close();
                con.close();
                res.append("RegistrationSuccess");
                return res.toString();
            }
            else                                                                          //插入失败
            {
                prepare.close();
                con.close();
                System.out.println("Failed to insert this record");
                res.append("Registration Failed.");
            }
        }
        catch(ClassNotFoundException e)
        {
            System.out.print("驱动类加载失败 : ");
            e.printStackTrace();

//            res.append("驱动类加载失败;");
        }
        catch(SQLException e)
        {
            System.out.println("Database operation failed.");
            System.out.println("ERROINFO : "+e.getMessage());
            System.out.println("SQLSTATE : "+e.getSQLState());
            System.out.println("DATABASESTATE : "+e.getErrorCode());
            e.printStackTrace();

//            res.append("Registration Failed.");
//            res.append("ERROINFO : "+e.getMessage());
//            res.append("SQLSTATE : "+e.getSQLState());
//            res.append("DATABASESTATE : "+e.getErrorCode());
            if(e.getErrorCode()==23505)
            {
                res.append("AccountAlreadyExist");
            }
            else if(e.getErrorCode()==22001)
            {
                res.append("ValuetooLargeForColumn");
            }
        }
        return res.toString();
    }


}

/*

.yml 是 YAML 格式文件的扩展名，YAML 的全称是 “YAML Ain't Markup Language”（YAML 不是标记语言），
它是一种轻量级的数据序列化格式，
主要用于配置文件（比如项目配置、环境变量、工具配置等），
因其语法简洁、可读性强而被广泛使用。

 */
