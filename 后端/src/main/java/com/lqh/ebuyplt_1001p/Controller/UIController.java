package com.lqh.ebuyplt_1001p.Controller;

import com.lqh.ebuyplt_1001p.Controller.UIControllerTools.ProductClick_jsonGet;
import com.lqh.ebuyplt_1001p.Controller.UIControllerTools.ProductClick_jsonSend;
import com.lqh.ebuyplt_1001p.Controller.UIControllerTools.ProductSearch_jsonSend;
import com.lqh.ebuyplt_1001p.Controller.UIControllerTools.ProductSearch_jsonGet;
import com.lqh.ebuyplt_1001p.Controller.ResultPack.ApiResult;
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

    @RequestMapping("/api/ProductRecommend")
    public ApiResult<ArrayList<ProductSearch_jsonSend>> ProductRecommend()
    {
        ArrayList<ProductSearch_jsonSend> res=ProductRecommendResult();
        return ApiResult.success(res);
    }
    private ArrayList<ProductSearch_jsonSend> ProductRecommendResult()
    {
        ArrayList<ProductSearch_jsonSend>res=new ArrayList<ProductSearch_jsonSend>();
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="SELECT * FROM ProductTable " +
                    "INNER JOIN ProductClicksInfoTable ON ProductTable.pID = ProductClicksInfoTable.pID " +
                    "INNER JOIN ProductImagesTable ON ProductImagesTable.pID=ProductClicksInfoTable.pID AND ProductImagesTable.pImgType='缩略图' " +
                    "ORDER BY ProductClicksInfoTable.pClicksAmount DESC " +
                    "LIMIT 50 ;";
            ResultSet rs=con.createStatement().executeQuery(sql1);
            while(rs.next())
            {
                ProductSearch_jsonSend item=new ProductSearch_jsonSend();
                item.setpID(rs.getString("pID"));
                item.setpName(rs.getString("pName"));
                item.setpType(rs.getString("pType"));
                item.setpDiscount(rs.getDouble("pDiscount"));
                item.setpPrice(rs.getDouble("pPrice"));
                item.setpProducer(rs.getString("pProducer"));
                item.setpReleaseDate(rs.getString("pReleaseDate"));
                item.setpInfo(rs.getString("pInfo"));
                item.setpIcon_path(rs.getString("pIcon_path"));
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


    @RequestMapping("/api/ProductSearch")
    public ApiResult<ArrayList<ProductSearch_jsonSend>> ProductSearch(@RequestBody ProductSearch_jsonGet SearchCondition)//返回商品搜索结果附带筛选条件
    {
        ArrayList<ProductSearch_jsonSend>ItemList=SearchResult(SearchCondition);
        return ApiResult.success(ItemList);
    }
    private ArrayList<ProductSearch_jsonSend>  SearchResult(ProductSearch_jsonGet SearchCondition)                      //参数是描述信息
    {
        ArrayList<ProductSearch_jsonSend>res=new ArrayList<ProductSearch_jsonSend>();
        HashMap<String,ProductSearch_jsonSend>ResultList=new HashMap<String,ProductSearch_jsonSend>();                  //以pID为主键，标记已经加入res的商品，避免重复加入

        String SearchDescribe=SearchCondition.getSeachDesciption();                                                     //用户输入大概商品描述
        String SearchpID=SearchCondition.getpID();                                                                      //筛选条件：商品编号
        String SearchpType=SearchCondition.getpType();                                                                  //筛选条件：商品类型

        double SearchpPrice_f=SearchCondition.getpPrice_f();                                                            //筛选条件：商品价格
        double SearchpPrice_r=SearchCondition.getpPrice_r();                                                            //筛选条件：商品价格

        String SearchpProducer=SearchCondition.getpProducer();                                                          //筛选条件：商品生产商

        String SearchpReleaseDate_f=SearchCondition.getpReleaseDate_f();                                                //筛选条件：商品上架日期
        String SearchpReleaseDate_r=SearchCondition.getpReleaseDate_r();                                                //筛选条件：商品上架日期

        String SearchpInfo=SearchCondition.getpInfo();                                                                  //筛选条件：商品描述信息

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date date_f=null;
        Date date_r=null;

        if(SearchpReleaseDate_f.length()==0)
        {
            SearchpReleaseDate_f=new StringBuilder("0000-01-01").toString();
        }
        if(SearchpReleaseDate_r.length()==0)
        {
            SearchpReleaseDate_r=new StringBuilder("9999-12-31").toString();
        }

        if(SearchpPrice_f==0.0)
        {
            //
        }
        if(SearchpPrice_r==0.0)
        {
            SearchpPrice_r=Double.MAX_VALUE-100.0;
        }

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

            String sql1="SELECT ProductTable.*,ProductImagesTable.pImgType,ProductImagesTable.pImagePath " +
                    "FROM ProductTable,ProductImagesTable WHERE " +
                    "LOWER(ProductTable.pID) LIKE LOWER(?) AND " +                                                                   //商品编号
                    "LOWER(ProductTable.pType) LIKE LOWER(?) AND " +                                                                 //商品名
                    "LOWER(ProductTable.pProducer) LIKE LOWER(?) AND " +                                                             //商品类型
                    "LOWER(ProductTable.pInfo) LIKE LOWER(?) AND " +                                                                 //商品描述
                    "ProductTable.pReleaseDate >= ? AND ProductTable.pReleaseDate <= ? AND " +
                    "ProductTable.pPrice >= ? AND ProductTable.pPrice <= ? AND "+
                    "ProductTable.pID=ProductImagesTable.pID AND ProductImagesTable.pImgType='缩略图';";

            String sql2="SELECT ProductTable.*,ProductImagesTable.pImgType,ProductImagesTable.pImagePath " +
                    "FROM ProductTable,ProductImagesTable WHERE " +
                    "LOWER(ProductTable.pID) LIKE LOWER(?) OR " +                                                                    //商品编号
                    "LOWER(ProductTable.pName) LIKE LOWER(?) OR " +                                                                  //商品名
                    "LOWER(ProductTable.pType) LIKE LOWER(?) OR " +                                                                  //商品类型
                    "LOWER(ProductTable.pProducer) LIKE LOWER(?) OR " +                                                              //商品生产商
                    "LOWER(ProductTable.pInfo) LIKE LOWER(?) AND " +
                    "ProductTable.pID=ProductImagesTable.pID AND ProductImagesTable.pImgType='缩略图';";

//            String sql1="SELECT * FROM ProductTable WHERE " +
//                    "LOWER(pID) LIKE LOWER(?) AND " +                                                                   //商品编号
//                    "LOWER(pType) LIKE LOWER(?) AND " +                                                                 //商品名
//                    "LOWER(pProducer) LIKE LOWER(?) AND " +                                                             //商品类型
//                    "LOWER(pInfo) LIKE LOWER(?) AND " +                                                                 //商品描述
//                    "pReleaseDate >= ? AND pReleaseDate <= ? AND " +
//                    "pPrice >= ? AND pPrice <= ? ;";
//            String sql2="SELECT * FROM ProductTable WHERE " +
//                    "LOWER(pID) LIKE LOWER(?) OR " +                                                                    //商品编号
//                    "LOWER(pName) LIKE LOWER(?) OR " +                                                                  //商品名
//                    "LOWER(pType) LIKE LOWER(?) OR " +                                                                  //商品类型
//                    "LOWER(pProducer) LIKE LOWER(?) OR " +                                                              //商品生产商
//                    "LOWER(pInfo) LIKE LOWER(?);";                                                                      //商品描述信息

            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,"%"+SearchpID+"%");                                                     //填入商品编号筛选条件
            prepare.setString(2,"%"+SearchpType+"%");                                                   //填入商品类型筛选条件
            prepare.setString(3,"%"+SearchpProducer+"%");                                               //填入商品生产商筛选条件
            prepare.setString(4,"%"+SearchpInfo+"%");                                                   //填入商品信息筛选条件

            int dateCompareResult=date_f.compareTo(date_r);
            if(dateCompareResult>0)                              //左边日期大于右边日期
            {
                StringBuilder sb1=new StringBuilder(SearchpReleaseDate_r);
                sb1.append(" 00:00:00");
                StringBuilder sb2=new StringBuilder(SearchpReleaseDate_f);
                sb2.append(" 23:59:59");

                prepare.setString(5,sb1.toString());                                                        //填入上架日期筛选区间f          5
                prepare.setString(6,sb2.toString());                                                        //填入上架日期筛选区间r          6
            }
            else if(dateCompareResult==0)                       //两个日期相等
            {
                StringBuilder sb1=new StringBuilder(SearchpReleaseDate_f);
                sb1.append(" 00:00:00");
                StringBuilder sb2=new StringBuilder(SearchpReleaseDate_r);
                sb2.append(" 23:59:59");

                prepare.setString(5,sb1.toString());                                                        //填入上架日期筛选区间f          5
                prepare.setString(6,sb2.toString());                                                        //填入上架日期筛选区间r          6
            }
            else if(dateCompareResult<0)                        //左边日期小于右边日期
            {
                StringBuilder sb1=new StringBuilder(SearchpReleaseDate_f);
                sb1.append(" 00:00:00");
                StringBuilder sb2=new StringBuilder(SearchpReleaseDate_r);
                sb2.append(" 23:59:59");

                prepare.setString(5,sb1.toString());                                                        //填入上架日期筛选区间f          5
                prepare.setString(6,sb2.toString());                                                        //填入上架日期筛选区间r          6
            }

            if(SearchpPrice_f>SearchpPrice_r)
            {
                double temp=SearchpPrice_f;
                SearchpPrice_f=SearchpPrice_r;
                SearchpPrice_r=temp;
            }
            prepare.setString(7,"%"+SearchpPrice_f+"%");                                                //填入价格筛选区间f             7
            prepare.setString(8,"%"+SearchpPrice_r+"%");                                                //填入价格筛选区间r             8


            if(SearchCondition.FilterOpen()==true)
            {
                ResultSet rs=con.createStatement().executeQuery(sql1);
                while(rs.next())
                {
                    ProductSearch_jsonSend item=new ProductSearch_jsonSend();
                    item.setpID(rs.getString("ProductTable.pID"));
                    item.setpName(rs.getString("ProductTable.pName"));
                    item.setpType(rs.getString("ProductTable.pType"));
                    item.setpProducer(rs.getString("ProductTable.pProducer"));
                    item.setpDiscount(rs.getDouble("ProductTable.pDiscount"));
                    item.setpPrice(rs.getDouble("ProductTable.pPrice"));
                    item.setpReleaseDate(rs.getString("ProductTable.pReleaseDate"));
                    item.setpInfo(rs.getString("ProductTable.pInfo"));

                    item.setpIcon_path(rs.getString("ProductImagesTable.pImgType"));

                    ResultList.put(item.getpID(),item);
                    res.add(item);
                }
            }

            //这个单独弄一块，这个部分查询需要在商品编号，商品名称，商品生产商，商品类型，商品描述信息里面查询，
            if(SearchDescribe.length()!=0)                                                                              //有描述信息，根据描述信息模糊查询商品
            {
                PreparedStatement prepare2=con.prepareStatement(sql2);
                prepare2.setString(1,"%"+SearchDescribe+"%");                                           //填入商品编号搜索_模糊查询
                prepare2.setString(2,"%"+SearchDescribe+"%");                                           //填入商品名搜索_模糊查询
                prepare2.setString(3,"%"+SearchDescribe+"%");                                           //填入商品类型搜索_模糊查询
                prepare2.setString(4,"%"+SearchDescribe+"%");                                           //填入商品生产商搜索_模糊查询
                prepare2.setString(5,"%"+SearchDescribe+"%");                                           //填入商品描述信息搜索_模糊查询

                ResultSet rs2=prepare2.executeQuery();
                while(rs2.next())
                {
                    ProductSearch_jsonSend item=new ProductSearch_jsonSend();
                    item.setpID(rs2.getString("ProductTable.pID"));
                    item.setpName(rs2.getString("ProductTable.pName"));
                    item.setpType(rs2.getString("ProductTable.pType"));
                    item.setpProducer(rs2.getString("ProductTable.pProducer"));
                    item.setpDiscount(rs2.getDouble("ProductTable.pDiscount"));
                    item.setpPrice(rs2.getDouble("ProductTable.pPrice"));
                    item.setpReleaseDate(rs2.getString("ProductTable.pReleaseDate"));
                    item.setpInfo(rs2.getString("ProductTable.pInfo"));

                    item.setpIcon_path(rs2.getString("ProductImagesTable.pImgType"));

                    if(ResultList.get(item.getpID())==null)                                                             //这条商品记录之前没有被添加
                    {
                        res.add(item);
                    }
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

    @RequestMapping("/api/ProductClick")
    public ApiResult<ProductClick_jsonSend> ProductClick(@RequestBody ProductClick_jsonGet ClickCondition)              //返回该点击的商品信息
    {
        ProductClick_jsonSend Item=ClickResult(ClickCondition);
        return ApiResult.success(Item);
    }
    private ProductClick_jsonSend ClickResult(ProductClick_jsonGet ClickCondition)
    {
        ProductClick_jsonSend result=new ProductClick_jsonSend();

        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="SELECT * FROM ProductTable WHERE pID=?;";                                                    //查询商品的基本信息
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,ClickCondition.getpID());
            ResultSet rs=prepare.executeQuery();
            if(rs.next())
            {
                result.setpID(rs.getString("pID"));
                result.setpName(rs.getString("pName"));
                result.setpType(rs.getString("pType"));
                result.setpProducer(rs.getString("pProducer"));
                result.setpDiscount(rs.getDouble("pDiscount"));
                result.setpPrice(rs.getDouble("pPrice"));
                result.setpReleaseDate(rs.getString("pReleaseDate"));
                result.setpInfo(rs.getString("pInfo"));
            }

            String sql2="SELECT * FROM ProductImagesTable WHERE pID=?;";                                                //查询该商品的图片信息
            PreparedStatement prepare2=con.prepareStatement(sql2);
            prepare2.setString(1,ClickCondition.getpID());
            ResultSet rs2=prepare2.executeQuery();
            ArrayList<String>temparr=new ArrayList<>();
            while(rs2.next())
            {
                String tempstr=rs2.getString("pImagePath");
                temparr.add(tempstr);
            }
            result.setpIcon_paths(temparr);

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

}
