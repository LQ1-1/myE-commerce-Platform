package com.lqh.ebuyplt_1001p.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

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

    @RequestMapping("/Welcome")
    public String Hello()
    {
        return "Welcome to EBuyPlt";
    }

    //处理登录函数1
    @RequestMapping("/Login")
    public String Login(HttpServletRequest request)
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
            System.out.println(e.fillInStackTrace());
        }
        return "404";
    }


    //处理函数2
    @RequestMapping("/Login")
    public String Login_other(HttpServletRequest request)
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
            System.out.println(e.fillInStackTrace());
        }
        return "404";
    }





}

/*

.yml 是 YAML 格式文件的扩展名，YAML 的全称是 “YAML Ain't Markup Language”（YAML 不是标记语言），
它是一种轻量级的数据序列化格式，
主要用于配置文件（比如项目配置、环境变量、工具配置等），
因其语法简洁、可读性强而被广泛使用。

 */
