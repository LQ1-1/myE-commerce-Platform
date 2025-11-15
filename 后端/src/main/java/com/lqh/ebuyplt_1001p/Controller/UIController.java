package com.lqh.ebuyplt_1001p.Controller;

import com.lqh.ebuyplt_1001p.Controller.UIControllerTools.ProductSearch_jsonSend;
import com.lqh.ebuyplt_1001p.Controller.UIControllerTools.ProductSearch_jsonGet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.*;
import java.sql.SQLException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

@RestController
public class UIController
{
    //数据库的信息
    private static String url="jdbc:kingbase8://localhost:54321/db_ebuyplt2";
    private static String user="system";
    private static String password="qh20050908";


    @RequestMapping("/api/ProductSearch")
    public ProductSearch_jsonSend ProductSearch(@RequestBody ProductSearch_jsonGet SearchCondition)
    {


    }

    private ArrayList<ProductSearch_jsonSend>  SearchResult(ProductSearch_jsonGet SearchCondition)                  //参数是描述信息
    {
        ArrayList<ProductSearch_jsonSend>res=new ArrayList<ProductSearch_jsonSend>();
        HashMap<String,ProductSearch_jsonSend>ResultList=new HashMap<String,ProductSearch_jsonSend>();              //以pID为主键，标记已经加入res的商品，避免重复加入

        String SearchDescribe=SearchCondition.getSeachDesciption();                             //用户输入大概商品描述
        String SearchpID=SearchCondition.getpID();                                              //筛选条件：商品编号
        String SearchpType=SearchCondition.getpType();                                          //筛选条件：商品类型

        double SearchpPrice_f=SearchCondition.getpPrice_f();                                    //筛选条件：商品价格
        double SearchpPrice_r=SearchCondition.getpPrice_r();                                    //筛选条件：商品价格

        String SearchpProducer=SearchCondition.getpProducer();                                  //筛选条件：商品生产商

        String SearchpReleaseDate_f=SearchCondition.getpReleaseDate_f();                        //筛选条件：商品上架日期
        String SearchpReleaseDate_r=SearchCondition.getpReleaseDate_r();                        //筛选条件：商品上架日期

        String SearchpInfo=SearchCondition.getpInfo();                                          //筛选条件：商品描述信息

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date date_f=null;
        Date date_r=null;
        try
        {
            date_f=dateFormat.parse(SearchpReleaseDate_f);
            date_r=dateFormat.parse(SearchpReleaseDate_r);
        }
        catch(Exception e)
        {
            System.out.println("日期转换错误 : "+e.getMessage());
            return res;
        }

        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="SELECT * FROM ProductTable WHERE " +
                    "LOWER(pID) LIKE LOWER(?) AND " +                                                                   //商品编号
                    "LOWER(pType) LIKE LOWER(?) AND " +                                                                 //商品名
                    "LOWER(pProducer) LIKE LOWER(?) AND " +                                                             //商品类型
                    "LOWER(pInfo) LIKE LOWER(?) AND " +                                                                 //商品描述
                    "pReleaseDate >= ? AND pReleaseDate <= ? AND " +
                    "pPrice >= ? AND pPrice <= ?";
            String sql2;

            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,"%"+SearchpID+"%");
            prepare.setString(2,"%"+SearchpType+"%");
            prepare.setString(3,"%"+SearchpProducer+"%");
            prepare.setString(4,"%"+SearchpInfo+"%");

            int dateCompareResult=date_f.compareTo(date_r);
            if(dateCompareResult>0)                              //左边日期大于右边日期
            {
                StringBuilder sb1=new StringBuilder(SearchpReleaseDate_r);
                sb1.append(" 00:00:00");
                StringBuilder sb2=new StringBuilder(SearchpReleaseDate_f);
                sb2.append(" 23:59:59");

                prepare.setString(5,sb1.toString());
                prepare.setString(6,sb2.toString());
            }
            else if(dateCompareResult==0)                       //两个日期相等
            {
                StringBuilder sb1=new StringBuilder(SearchpReleaseDate_f);
                sb1.append(" 00:00:00");
                StringBuilder sb2=new StringBuilder(SearchpReleaseDate_r);
                sb2.append(" 23:59:59");

                prepare.setString(5,sb1.toString());
                prepare.setString(6,sb2.toString());
            }
            else if(dateCompareResult<0)                        //左边日期小于右边日期
            {
                StringBuilder sb1=new StringBuilder(SearchpReleaseDate_f);
                sb1.append(" 00:00:00");
                StringBuilder sb2=new StringBuilder(SearchpReleaseDate_r);
                sb2.append(" 23:59:59");

                prepare.setString(5,sb1.toString());
                prepare.setString(6,sb2.toString());
            }


            //这个单独弄一块，这个部分查询需要在商品编号，商品名称，商品生产商，商品类型，商品描述信息里面查询，
            if(SearchCondition.getSeachDesciption()!=null)                                                          //有描述信息，根据描述信息模糊查询商品
            {

            }
        }
        catch(SQLException e)
        {

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return res;
    }

}
