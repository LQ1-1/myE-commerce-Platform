package com.lqh.ebuyplt_1001p.Controller;


import com.lqh.ebuyplt_1001p.Controller.AdminPack.UserAccountTableItem;
import com.lqh.ebuyplt_1001p.Controller.AdminPack.UserDeliveryInfoTableItem;
import com.lqh.ebuyplt_1001p.Controller.AdminPack.UserFavoritesTableItem;
import com.lqh.ebuyplt_1001p.Controller.AdminPack.*;
import com.lqh.ebuyplt_1001p.Controller.DBTools.DBUtil;
import com.lqh.ebuyplt_1001p.Controller.MerchantPack.ProductImageItem;
import com.lqh.ebuyplt_1001p.Controller.MerchantPack.ProductInfoUpdate_jsonGet;
import com.lqh.ebuyplt_1001p.Controller.ResultPack.ApiResult;
import com.lqh.ebuyplt_1001p.Controller.UIControllerTools.ProductSearch_jsonGet;
import com.lqh.ebuyplt_1001p.Controller.UIControllerTools.ProductSearch_jsonSend;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@RestController
public class AdminController        //管理员控制器
{
    //数据库的信息
    private static String url="jdbc:kingbase8://localhost:54321/db_ebuyplt2";
    private static String user="system";
    private static String password="qh20050908";

    //-----------------------------------基本表的信息----------------------------------//

    @CrossOrigin(origins="*")
    @RequestMapping("/api/AdminUserAccountTable")
    public ApiResult<ArrayList<UserAccountTableItem>> AdminUserAccountTable()       //获取所有的账户信息
    {
        return ApiResult.success(AdminUserAccountTableResult());
    }
    private ArrayList<UserAccountTableItem> AdminUserAccountTableResult()
    {
        ArrayList<UserAccountTableItem>res=new ArrayList<>();
        try
        {
            Class.forName("com.kingbase8.Driver");
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
    @RequestMapping("/api/AdminUserDeliveryInfoTable")
    public ApiResult<ArrayList<UserDeliveryInfoTableItem>> AdminUserDeliveryInfoTable()     //获取所有用户的所有收货信息
    {
        return ApiResult.success(AdminUserDeliveryInfoTableResult());
    }
    private ArrayList<UserDeliveryInfoTableItem> AdminUserDeliveryInfoTableResult()
    {
        ArrayList<UserDeliveryInfoTableItem> res=new ArrayList<>();
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con= DriverManager.getConnection(url,user,password);

            String sql1="SELECT * FROM UserDeliveryInfoTable;";
            ResultSet rs=con.createStatement().executeQuery(sql1);
            while(rs.next())
            {
                UserDeliveryInfoTableItem item=new UserDeliveryInfoTableItem();
                item.setuID(rs.getString("uID"));
                item.setoDeliveryNote(rs.getString("oDeliveryNote"));
                item.setoPostalCode(rs.getString("oPostalCode"));
                item.setuContactPersonEmail(rs.getString("oReceieverEmail"));
                item.setuDeliveryAddress(rs.getString("uDeliveryAddress"));
                item.setuContactPersonGender(rs.getString("uContactPersonGender"));
                item.setuContactPersonPhone(rs.getString("uContactPersonPhone"));
                item.setuContactPersonName(rs.getString("uContactPersonName"));
                item.setuDIndex(Integer.parseInt(rs.getString("uDIndex")));
                res.add(item);
            }
            rs.close();
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
    @RequestMapping("/api/AdminUserShoppingCartTable")
    public ApiResult<ArrayList<UserShoppingCartTableItem>> AdminUserShoppingCartTable()     //返回所有用户的所有购物车记录
    {
        return ApiResult.success(AdminUserShoppingCartTableResult());
    }
    private ArrayList<UserShoppingCartTableItem> AdminUserShoppingCartTableResult()
    {
        ArrayList<UserShoppingCartTableItem> res=new ArrayList<>();
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con= DriverManager.getConnection(url,user,password);
            String sql1="SELECT * FROM UserShoppingCartTable;";
            ResultSet rs=con.createStatement().executeQuery(sql1);
            while(rs.next())
            {
                UserShoppingCartTableItem item=new UserShoppingCartTableItem();
                item.setuID(rs.getString("uID"));
                item.setpID(rs.getString("pID"));
                item.setcAmount(rs.getInt("cAmount"));
                res.add(item);
            }
            rs.close();
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
    @RequestMapping("/api/AdminUserFavoritesTable")
    public ApiResult<ArrayList<UserFavoritesTableItem>>AdminUserFavoritesTable()        //获取所有用户的所有商品收藏信息
    {
        return ApiResult.success(AdminUserFavoritesTableResult());
    }
    private ArrayList<UserFavoritesTableItem> AdminUserFavoritesTableResult()
    {
        ArrayList<UserFavoritesTableItem>res=new ArrayList<>();
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con= DriverManager.getConnection(url,user,password);

            String sql1="SELECT * FROM UserFavoritesTable;";
            ResultSet rs=con.createStatement().executeQuery(sql1);
            while(rs.next())
            {
                UserFavoritesTableItem item=new UserFavoritesTableItem();
                item.setuID(rs.getString("uID"));
                item.setpID(rs.getString("pID"));
                res.add(item);
            }
            rs.close();
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
    @RequestMapping("/api/AdminMerchantsProductTable")
    public ApiResult<ArrayList<MerchantsProductTableItem>> AdminMerchantsProductTable()      //获取所有商户的所有上架记录
    {
        return ApiResult.success(AdminMerchantsProductTableResult());
    }
    private ArrayList<MerchantsProductTableItem> AdminMerchantsProductTableResult()
    {
        ArrayList<MerchantsProductTableItem> res=new ArrayList<>();
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con= DriverManager.getConnection(url,user,password);

            String sql1="SELECT * FROM MerchantsProductTable;";
            ResultSet rs=con.createStatement().executeQuery(sql1);
            while(rs.next())
            {
                MerchantsProductTableItem item=new MerchantsProductTableItem();
                item.setuID(rs.getString("uID"));
                item.setpID(rs.getString("pID"));
                res.add(item);
            }
            rs.close();
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
    @RequestMapping("/api/AdminProductTable")
    public ApiResult<ArrayList<ProductTableItem>> AdminProductTable()        //返回所有商品的记录
    {
        return ApiResult.success(AdminProductTableResult());
    }
    private ArrayList<ProductTableItem> AdminProductTableResult()
    {
        ArrayList<ProductTableItem> res=new ArrayList<>();
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con= DriverManager.getConnection(url,user,password);
            String sql1="SELECT ProductTable.pID, ProductTable.pName, ProductTable.pType," +
                    "ProductTable.pDiscount, ProductTable.pPrice, ProductTable.pProducer," +
                    "ProductTable.pReleaseDate, ProductTable.pInfo, ProductTable.pInventory, ProductTable.pStatus, " +
                    "ProductImagesTable.pImagePath FROM ProductTable " +
                    "INNER JOIN ProductImagesTable ON ProductImagesTable.pID=ProductTable.pID WHERE ProductImagesTable.pImgType='缩略图';";
            ResultSet rs=con.createStatement().executeQuery(sql1);
            while(rs.next())
            {
                ProductTableItem item=new ProductTableItem();
                item.setpID(rs.getString("pID"));
                item.setpName(rs.getString("pName"));
                item.setpType(rs.getString("pType"));
                item.setpDiscount(rs.getDouble("pDiscount"));
                item.setpPrice(rs.getDouble("pPrice"));
                item.setpProducer(rs.getString("pProducer"));
                item.setpReleaseDate(rs.getString("pReleaseDate"));
                item.setpInfo(rs.getString("pInfo"));
                item.setpInventory(rs.getInt("pInventory"));
                item.setpStatus(rs.getString("pStatus"));
                item.setpImagePath(rs.getString("pImagePath"));
                res.add(item);
            }
            rs.close();
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
    @RequestMapping("/api/AdminProductImagesTable")
    public ApiResult<ArrayList<ProductImagesTableItem>> AdminProductImagesTable()    //返回所有商品的所有图片记录信息
    {
        return ApiResult.success(AdminProductImagesTableResult());
    }
    private ArrayList<ProductImagesTableItem> AdminProductImagesTableResult()
    {
        ArrayList<ProductImagesTableItem> res=new ArrayList<>();
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="SELECT * FROM ProductImagesTable;";
            ResultSet rs=con.createStatement().executeQuery(sql1);
            while(rs.next())
            {
                ProductImagesTableItem item=new ProductImagesTableItem();
                item.setpID(rs.getString("pID"));
                item.setpImgType(rs.getString("pImgType"));
                item.setpImagePath(rs.getString("pImagePath"));
                res.add(item);
            }
            rs.close();
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
        Object obj;
        String str;
        return res;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/api/AdminProductClicksInfoTable")
    public ApiResult<ArrayList<ProductClicksInfoTableItem>> AdminProductClicksInfoTable()    //返回所有商品的点击次数记录信息
    {
        return ApiResult.success(AdminProductClicksInfoTableResult());
    }
    private ArrayList<ProductClicksInfoTableItem> AdminProductClicksInfoTableResult()
    {
        ArrayList<ProductClicksInfoTableItem> res=new ArrayList<>();
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con= DriverManager.getConnection(url,user,password);

            String sql1="SELECT * FROM ProductClicksInfoTable;";
            ResultSet rs=con.createStatement().executeQuery(sql1);
            while(rs.next())
            {
                ProductClicksInfoTableItem item=new ProductClicksInfoTableItem();
                item.setpID(rs.getString("pID"));
                item.setpClicksAmount(rs.getLong("pClicksAmount"));
                res.add(item);
            }
            rs.close();
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

    @CrossOrigin(origins = "*")
    @RequestMapping("/api/AdminOrderFullInfoTable")
    public ApiResult<ArrayList<OrderFullInfoTableItem>> AdminOrderFullInfoTable()        //返回所有的订单信息
    {
        return ApiResult.success(AdminOrderFullInfoTableResult());
    }
    private ArrayList<OrderFullInfoTableItem> AdminOrderFullInfoTableResult()
    {
        ArrayList<OrderFullInfoTableItem> res=new ArrayList<>();
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con= DriverManager.getConnection(url,user,password);

            String sql1="SELECT " +
                    "OrderGeneralInfoTable.oOrderID, OrderGeneralInfoTable.oOrdererID, " +
                    "OrderBasicInfoTable.oDate, OrderBasicInfoTable.oStatus, " +
                    "OrdererInfoTable.oReceiverName, OrdererInfoTable.oReceieverGender, OrdererInfoTable.oReceieverEmail, " +
                    "OrderDeliveryInfo.oDeliveryAddress, OrderDeliveryInfo.oPostalCode, OrderDeliveryInfo.oContactPhone, OrderDeliveryInfo.oDeliveryNote " +
                    "FROM OrderGeneralInfoTable " +
                    "INNER JOIN OrderBasicInfoTable ON OrderGeneralInfoTable.oOrderID=OrderBasicInfoTable.oOrderID " +
                    "INNER JOIN OrdererInfoTable ON OrdererInfoTable.oOrderID=OrderGeneralInfoTable.oOrderID " +
                    "INNER JOIN OrderDeliveryInfo ON OrderDeliveryInfo.oOrderID=OrderGeneralInfoTable.oOrderID ;";

            ResultSet rs=con.createStatement().executeQuery(sql1);
            while(rs.next())
            {
                OrderFullInfoTableItem item=new OrderFullInfoTableItem();
                item.setoOrderID(rs.getString("oOrderID"));
                item.setoOrdererID(rs.getString("oOrdererID"));
                item.setoDate(rs.getString("oDate"));
                item.setoStatus(rs.getString("oStatus"));
                item.setoReceiverName(rs.getString("oReceiverName"));
                item.setoReceieverGender(rs.getString("oReceieverGender"));
                item.setoReceieverEmail(rs.getString("oReceieverEmail"));
                item.setoDeliveryAddress(rs.getString("oDeliveryAddress"));
                item.setoPostalCode(rs.getString("oPostalCode"));
                item.setoContactPhone(rs.getString("oContactPhone"));
                item.setoDeliveryNote(rs.getString("oDeliveryNote"));
                res.add(item);
            }
            rs.close();
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
    @RequestMapping("/api/AdminOrderProductInfoTable")
    public ApiResult<ArrayList<OrderProductInfoTableItem>> AdminOrderProductInfoTable()      //返回所有订单的所有的订购的商品信息
    {
        return ApiResult.success(AdminOrderProductInfoTableResult());
    }
    private ArrayList<OrderProductInfoTableItem> AdminOrderProductInfoTableResult()
    {
        ArrayList<OrderProductInfoTableItem>res=new ArrayList<>();
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con= DriverManager.getConnection(url,user,password);

            String sql1="SELECT * FROM OrderProductInfoTable;";
            ResultSet rs=con.createStatement().executeQuery(sql1);
            while(rs.next())
            {
                OrderProductInfoTableItem item=new OrderProductInfoTableItem();
                item.setoOrderID(rs.getString("oOrderID"));
                item.setpID(rs.getString("pID"));
                item.setoPrice(rs.getDouble("oPrice"));
                item.setoAmount(rs.getInt("oAmount"));
                item.setoProductDeliveryStatus(rs.getString("oProductDeliveryStatus"));
                res.add(item);
            }
            rs.close();
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

    //-----------------------------------基本表的信息----------------------------------//


    //-----------------------------------筛选接口-----------------------------------//

    @CrossOrigin(origins="*")
    @RequestMapping("/api/AdminUserAccountTableSearch")
    public ApiResult<ArrayList<UserAccountTableItem>> AdminUserAccountTableSearch(@RequestBody UserAccountTableItem_selectParameter para)     //搜索用户
    {
        return ApiResult.success(AdminUserAccountTableSearchResult(para));
    }
    private ArrayList<UserAccountTableItem> AdminUserAccountTableSearchResult(UserAccountTableItem_selectParameter para)
    {
        HashMap<String,Boolean> MAP=new HashMap<>();

        String id;
        String nickname;
        String phone;
        String email;
        String gender;
        String searchinput;
        String datef;
        String dater;

        id=(para!=null && para.getuID()!=null)?para.getuID():"";
        nickname=(para!=null && para.getuNickName()!=null)?para.getuNickName():"";
        phone=(para!=null && para.getuPhone()!=null)?para.getuPhone():"";
        email=(para!=null && para.getuEmail()!=null)?para.getuEmail():"";
        gender=(para!=null && para.getuGender()!=null)?para.getuGender():"";
        searchinput=(para!=null && para.getSearchInput()!=null)?para.getSearchInput():"";
        datef=(para!=null && para.getDateF()!=null)?para.getDateF():"0001-01-01";
        dater=(para!=null && para.getDateR()!=null)?para.getDateR():"9999-12-31";

        {
            System.out.println("*******************AdminUserAccountTableSearchResult*********************");
            System.out.println(id);
            System.out.println(nickname);
            System.out.println(phone);
            System.out.println(email);
            System.out.println(gender);
            System.out.println(searchinput);
            System.out.println(datef);
            System.out.println(dater);
            System.out.println("*******************AdminUserAccountTableSearchResult*********************");
        }

        ArrayList<UserAccountTableItem>res=new ArrayList<>();
        try
        {
            Connection con=DBUtil.getConnection();
            //筛选表单
            String sql1="SELECT * FROM UserAccountTable WHERE " +
                    "LOWER(UserAccountTable.uID) LIKE LOWER(?) AND " +      //账号
                    "LOWER(UserAccountTable.uNickName) LIKE LOWER(?) AND " +       //NickName
                    "LOWER(UserAccountTable.uPhone) LIKE LOWER(?) AND " +       //电话
                    "LOWER(UserAccountTable.uEmail) LIKE LOWER(?) AND " +       //邮箱
                    "LOWER(UserAccountTable.uGender) LIKE LOWER(?) AND " +       //性别
                    "UserAccountTable.uAccountType = ANY(?) AND " +        //账号类型
                    "UserAccountTable.uAccountStatus = ANY(?) AND " +        //账号状态
                    "UserAccountTable.uRegisterDate >= ? AND UserAccountTable.uRegisterDate <= ? ;";     //注册日期

            //搜索框输入
            String sql2="SELECT * FROM UserAccountTable WHERE " +
                    "LOWER(UserAccountTable.uID) LIKE LOWER(?) OR " +
                    "LOWER(UserAccountTable.uNickName) LIKE LOWER(?) OR " +
                    "LOWER(UserAccountTable.uPhone) LIKE LOWER(?) OR " +
                    "LOWER(UserAccountTable.uEmail) LIKE LOWER(?) OR " +
                    "LOWER(UserAccountTable.uGender) LIKE LOWER(?) OR " +
                    "LOWER(UserAccountTable.uAccountType) LIKE LOWER(?) OR " +
                    "LOWER(UserAccountTable.uAccountStatus) LIKE LOWER(?) ;";

            PreparedStatement prepare1=con.prepareStatement(sql1);
            prepare1.setString(1,id);
            prepare1.setString(2,nickname);
            prepare1.setString(3,phone);
            prepare1.setString(4,email);
            prepare1.setString(5,gender);
            prepare1.setArray(6,con.createArrayOf("VARCHAR",para.getuAccountTypes().toArray(new String[0])));
            prepare1.setArray(7,con.createArrayOf("VARCHAR",para.getuAccountStatuess().toArray(new String[0])));
            prepare1.setString(8,datef);
            prepare1.setString(9,dater);
            ResultSet rs=prepare1.executeQuery();
            while(rs.next())
            {
                if(MAP.get(rs.getString("uID"))==null)
                {
                    UserAccountTableItem item=new UserAccountTableItem();
                    item.setuID(rs.getString("uID"));
                    item.setuNickName(rs.getString("uNickName"));
                    item.setuPassword(rs.getString("uPassword"));
                    item.setuEmail(rs.getString("uPhone"));
                    item.setuEmail(rs.getString("uEmail"));
                    item.setuGender(rs.getString("uGender"));
                    item.setuRegisterDate(rs.getString("uRegisterDate"));
                    item.setuAccountType(rs.getString("uAccountType"));
                    item.setuAccountStatus(rs.getString("uAccountStatus"));
                    res.add(item);
                    MAP.put(rs.getString("uID"),true);
                }
            }
            rs.close();
            prepare1.close();

            if(searchinput!=null&&searchinput.length()>0)
            {
                PreparedStatement prepare2=con.prepareStatement(sql2);
                prepare2.setString(1,searchinput);
                prepare2.setString(2,searchinput);
                prepare2.setString(3,searchinput);
                prepare2.setString(4,searchinput);
                prepare2.setString(5,searchinput);
                prepare2.setString(6,searchinput);
                prepare2.setString(7,searchinput);
                ResultSet rs2=prepare2.executeQuery();
                while(rs2.next())
                {
                    if(MAP.get(rs2.getString("uID"))==null)
                    {
                        UserAccountTableItem item=new UserAccountTableItem();
                        item.setuID(rs2.getString("uID"));
                        item.setuNickName(rs2.getString("uNickName"));
                        item.setuPassword(rs2.getString("uPassword"));
                        item.setuEmail(rs2.getString("uPhone"));
                        item.setuEmail(rs2.getString("uEmail"));
                        item.setuGender(rs2.getString("uGender"));
                        item.setuRegisterDate(rs2.getString("uRegisterDate"));
                        item.setuAccountType(rs2.getString("uAccountType"));
                        item.setuAccountStatus(rs2.getString("uAccountStatus"));
                        res.add(item);
                        MAP.put(rs2.getString("uID"),true);
                    }
                }
                rs2.close();
                prepare2.close();
            }
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
    @RequestMapping("/api/AdminProductTableSearch")
    public ApiResult<ArrayList<ProductTableItem>> AdminProductTableSearch(@RequestBody ProductSearch_jsonGet SearchCondition)     //搜索商品
    {
        {
            System.out.println("***************Product search***************");
            System.out.println(SearchCondition.getSearchDesciption());
            System.out.println(SearchCondition.getpID());
            System.out.println(SearchCondition.getpType());
            System.out.println(SearchCondition.getpPrice_f());
            System.out.println(SearchCondition.getpPrice_r());
            System.out.println(SearchCondition.getpReleaseDate_f());
            System.out.println(SearchCondition.getpReleaseDate_r());
            System.out.println(SearchCondition.getSearchDesciption());
            System.out.println(SearchCondition.getpInfo());
            System.out.println(SearchCondition.getpProducer());
            System.out.println("***************Product search***************");
        }


        ArrayList<ProductTableItem>ItemList=AdminProductTableSearchResult(SearchCondition);
        return ApiResult.success(ItemList);
    }
    private ArrayList<ProductTableItem>  AdminProductTableSearchResult(ProductSearch_jsonGet SearchCondition)                      //参数是描述信息
    {
        ArrayList<ProductTableItem>res=new ArrayList<ProductTableItem>();
        HashMap<String,ProductTableItem>ResultList=new HashMap<String,ProductTableItem>();                  //以pID为主键，标记已经加入res的商品，避免重复加入

        String SearchDescribe=new StringBuilder("").append(SearchCondition.getSearchDesciption()).toString();                                                     //用户输入大概商品描述
        String SearchpID=new StringBuilder("").append(SearchCondition.getpID()).toString();                                                                      //筛选条件：商品编号
        String SearchpType=new StringBuilder("").append(SearchCondition.getpType()).toString();                                                                  //筛选条件：商品类型

        double SearchpPrice_f=SearchCondition.getpPrice_f();                                                            //筛选条件：商品价格
        double SearchpPrice_r=SearchCondition.getpPrice_r();                                                            //筛选条件：商品价格

        String SearchpProducer=new StringBuilder("").append(SearchCondition.getpProducer()).toString();                                                          //筛选条件：商品生产商

        String SearchpReleaseDate_f=new StringBuilder("").append(SearchCondition.getpReleaseDate_f()).toString();                                                //筛选条件：商品上架日期
        String SearchpReleaseDate_r=new StringBuilder("").append(SearchCondition.getpReleaseDate_r()).toString();                                                //筛选条件：商品上架日期

        String SearchpInfo=new StringBuilder("").append(SearchCondition.getpInfo()).toString();                                                                  //筛选条件：商品描述信息

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date_f=null;
        Date date_r=null;

        if(SearchpReleaseDate_f.length()==0)
        {
            SearchpReleaseDate_f=new StringBuilder("0001-01-01").toString();
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

        {
            System.out.println("***************Product search***************");
            System.out.println(SearchDescribe);
            System.out.println(SearchpType);
            System.out.println(SearchpPrice_f);
            System.out.println(SearchpPrice_r);
            System.out.println(SearchpProducer);
            System.out.println(SearchpReleaseDate_f);
            System.out.println(SearchpReleaseDate_r);
            System.out.println(SearchpInfo);
            System.out.println("***************Product search***************");
        }

        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="SELECT ProductTable.*,ProductImagesTable.pImgType,ProductImagesTable.pImagePath " +
                    "FROM ProductTable INNER JOIN ProductImagesTable ON ProductTable.pID=ProductImagesTable.pID AND ProductImagesTable.pImgType='缩略图' " +
                    "WHERE " +
                    "LOWER(ProductTable.pID) LIKE LOWER(?) AND " +                                                                   //商品编号
                    "LOWER(ProductTable.pType) LIKE LOWER(?) AND " +                                                                 //商品名
                    "LOWER(ProductTable.pProducer) LIKE LOWER(?) AND " +                                                             //商品类型
                    "LOWER(ProductTable.pInfo) LIKE LOWER(?) AND " +                                                                 //商品描述
                    "ProductTable.pReleaseDate >= ? AND ProductTable.pReleaseDate <= ? AND " +
                    "ProductTable.pPrice >= ? AND ProductTable.pPrice <= ? ;";

            String sql2="SELECT ProductTable.*,ProductImagesTable.pImgType,ProductImagesTable.pImagePath " +
                    "FROM ProductTable INNER JOIN ProductImagesTable ON ProductTable.pID=ProductImagesTable.pID AND ProductImagesTable.pImgType='缩略图' " +
                    "WHERE " +
                    "LOWER(ProductTable.pID) LIKE LOWER(?) OR " +                                                                    //商品编号
                    "LOWER(ProductTable.pName) LIKE LOWER(?) OR " +                                                                  //商品名
                    "LOWER(ProductTable.pType) LIKE LOWER(?) OR " +                                                                  //商品类型
                    "LOWER(ProductTable.pProducer) LIKE LOWER(?) OR " +                                                              //商品生产商
                    "LOWER(ProductTable.pInfo) LIKE LOWER(?) ;";

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
            prepare.setDouble(7,SearchpPrice_f);                                                //填入价格筛选区间f             7
            prepare.setDouble(8,SearchpPrice_r);                                                //填入价格筛选区间r             8


            if(SearchCondition.FilterOpen()==true)
            {
                ResultSet rs=prepare.executeQuery();
                while(rs.next())
                {
                    ProductTableItem item=new ProductTableItem();
                    item.setpID(rs.getString("pID"));
                    item.setpName(rs.getString("pName"));
                    item.setpType(rs.getString("pType"));
                    item.setpProducer(rs.getString("pProducer"));
                    item.setpDiscount(rs.getDouble("pDiscount"));
                    item.setpPrice(rs.getDouble("pPrice"));
                    item.setpReleaseDate(rs.getString("pReleaseDate"));
                    item.setpInfo(rs.getString("pInfo"));
                    item.setpInventory(rs.getInt("pInventory"));
                    item.setpStatus(rs.getString("pStatus"));

                    item.setpImagePath(rs.getString("pImagePath"));

                    ResultList.put(item.getpID(),item);
                    res.add(item);
                }
                rs.close();
                prepare.close();
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

                    ProductTableItem item=new ProductTableItem();
                    item.setpID(rs2.getString("pID"));
                    item.setpName(rs2.getString("pName"));
                    item.setpType(rs2.getString("pType"));
                    item.setpProducer(rs2.getString("pProducer"));
                    item.setpDiscount(rs2.getDouble("pDiscount"));
                    item.setpPrice(rs2.getDouble("pPrice"));
                    item.setpReleaseDate(rs2.getString("pReleaseDate"));
                    item.setpInfo(rs2.getString("pInfo"));
                    item.setpInventory(rs2.getInt("pInventory"));
                    item.setpStatus(rs2.getString("pStatus"));

                    item.setpImagePath(rs2.getString("pImagePath"));

                    if(ResultList.get(item.getpID())==null)                                                             //这条商品记录之前没有被添加
                    {
                        res.add(item);
                    }
                }
                rs2.close();
                prepare.close();
            }
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
    @RequestMapping("/api/AdminOrderInfoSearch")
    public ApiResult<ArrayList<OrderFullInfoTableItem>> AdminOrderInfoSearch(@RequestBody OrderFullInfoTableItem_selectParameter para)
    {
        return ApiResult.success(AdminOrderInfoSearchResult(para));
    }
    private ArrayList<OrderFullInfoTableItem> AdminOrderInfoSearchResult(OrderFullInfoTableItem_selectParameter para)
    {
        HashMap<String,Boolean> MAP=new HashMap<>();
        ArrayList<OrderFullInfoTableItem> res=new ArrayList<>();

        String orderid;
        String orderer;
        String receiver;
        String gender;
        String email;
        String address;
        String code;
        String phone;
        String note;
        String searchinput;
        String datef;
        String dater;

        orderid=(para!=null && para.getoOrderID()!=null)?para.getoOrderID():"";
        orderer=(para!=null && para.getoOrdererID()!=null)?para.getoOrdererID():"";
        receiver=(para!=null && para.getoReceiverName()!=null)?para.getoReceiverName():"";
        gender=(para!=null && para.getoReceieverGender()!=null)?para.getoReceieverGender():"";
        email=(para!=null && para.getoReceieverEmail()!=null)?para.getoReceieverEmail():"";
        address=(para!=null && para.getoDeliveryAddress()!=null)?para.getoDeliveryAddress():"";
        code=(para!=null && para.getoPostalCode()!=null)?para.getoPostalCode():"";
        phone=(para!=null && para.getoContactPhone()!=null)?para.getoContactPhone():"";
        note=(para!=null && para.getoDeliveryNote()!=null)?para.getoDeliveryNote():"";
        searchinput=(para!=null && para.getSearchInput()!=null)?para.getSearchInput():"";
        datef=(para!=null && para.getDateF()!=null)?para.getDateF():"0001-01-01";
        dater=(para!=null && para.getDateR()!=null)?para.getDateR():"9999-12-31";

        {
            System.out.println("***********************AdminOrderInfoSearchResult***********************");
            System.out.println(orderid);
            System.out.println(orderer);
            System.out.println(receiver);
            System.out.println(gender);
            System.out.println(email);
            System.out.println(address);
            System.out.println(code);
            System.out.println(phone);
            System.out.println(note);
            System.out.println(searchinput);
            System.out.println(datef);
            System.out.println(dater);
            System.out.println("***********************AdminOrderInfoSearchResult***********************");
        }

        try
        {
            Connection con=DBUtil.getConnection();
            String sql1="SELECT " +
                    "OrderGeneralInfoTable.oOrderID, OrderGeneralInfoTable.oOrdererID, " +
                    "OrderBasicInfoTable.oDate, OrderBasicInfoTable.oStatus, " +
                    "OrdererInfoTable.oReceiverName, OrdererInfoTable.oReceieverGender, OrdererInfoTable.oReceieverEmail, " +
                    "OrderDeliveryInfo.oDeliveryAddress, OrderDeliveryInfo.oPostalCode, OrderDeliveryInfo.oContactPhone, OrderDeliveryInfo.oDeliveryNote " +
                    "FROM OrderGeneralInfoTable " +
                    "INNER JOIN OrderBasicInfoTable ON OrderGeneralInfoTable.oOrderID=OrderBasicInfoTable.oOrderID " +
                    "INNER JOIN OrdererInfoTable ON OrdererInfoTable.oOrderID=OrderGeneralInfoTable.oOrderID " +
                    "INNER JOIN OrderDeliveryInfo ON OrderDeliveryInfo.oOrderID=OrderGeneralInfoTable.oOrderID " +
                    "WHERE " +
                    "OrderGeneralInfoTable.oOrderID LIKE ? AND " +
                    "OrderGeneralInfoTable.oOrdererID LIKE ? AND " +
                    "OrdererInfoTable.oReceiverName LIKE ? AND " +
                    "OrdererInfoTable.oReceieverGender LIKE ? AND " +
                    "OrdererInfoTable.oReceieverEmail LIKE ? AND " +
                    "OrderDeliveryInfo.oDeliveryAddress LIKE ? AND " +
                    "OrderDeliveryInfo.oPostalCode LIKE ? AND " +
                    "OrderDeliveryInfo.oContactPhone LIKE ? AND " +
                    "OrderDeliveryInfo.oDeliveryNote LIKE ? AND " +
                    "OrderBasicInfoTable.oStatus = ANY(?) AND " +
                    "OrderBasicInfoTable.oDate >= ? AND OrderBasicInfoTable.oDate <= ? ;";      //12?

            String sql2="SELECT " +
                    "OrderGeneralInfoTable.oOrderID, OrderGeneralInfoTable.oOrdererID, " +
                    "OrderBasicInfoTable.oDate, OrderBasicInfoTable.oStatus, " +
                    "OrdererInfoTable.oReceiverName, OrdererInfoTable.oReceieverGender, OrdererInfoTable.oReceieverEmail, " +
                    "OrderDeliveryInfo.oDeliveryAddress, OrderDeliveryInfo.oPostalCode, OrderDeliveryInfo.oContactPhone, OrderDeliveryInfo.oDeliveryNote " +
                    "FROM OrderGeneralInfoTable " +
                    "INNER JOIN OrderBasicInfoTable ON OrderGeneralInfoTable.oOrderID=OrderBasicInfoTable.oOrderID " +
                    "INNER JOIN OrdererInfoTable ON OrdererInfoTable.oOrderID=OrderGeneralInfoTable.oOrderID " +
                    "INNER JOIN OrderDeliveryInfo ON OrderDeliveryInfo.oOrderID=OrderGeneralInfoTable.oOrderID " +
                    "WHERE " +
                    "OrderGeneralInfoTable.oOrderID LIKE ? OR " +
                    "OrderGeneralInfoTable.oOrdererID LIKE ? OR " +
                    "OrdererInfoTable.oReceiverName LIKE ? OR " +
                    "OrdererInfoTable.oReceieverGender LIKE ? OR " +
                    "OrdererInfoTable.oReceieverEmail LIKE ? OR " +
                    "OrderDeliveryInfo.oDeliveryAddress LIKE ? OR " +
                    "OrderDeliveryInfo.oPostalCode LIKE ? OR " +
                    "OrderDeliveryInfo.oContactPhone LIKE ? OR " +
                    "OrderDeliveryInfo.oDeliveryNote LIKE ? ;";     //9?

            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,orderid);
            prepare.setString(2,orderer);
            prepare.setString(3,receiver);
            prepare.setString(4,gender);
            prepare.setString(5,email);
            prepare.setString(6,address);
            prepare.setString(7,code);
            prepare.setString(8,phone);
            prepare.setString(9,note);
            prepare.setArray(10,con.createArrayOf("VARCHAR",para.getoStatuses().toArray(new String[0])));
            prepare.setString(11,datef);
            prepare.setString(12,dater);
            ResultSet rs=prepare.executeQuery();
            while(rs.next())
            {
                if(MAP.get(rs.getString("oOrderID"))==null)
                {
                    OrderFullInfoTableItem item=new OrderFullInfoTableItem();
                    item.setoOrderID(rs.getString("oOrderID"));
                    item.setoOrdererID(rs.getString("oOrdererID"));
                    item.setoDate(rs.getString("oDate"));
                    item.setoStatus(rs.getString("oStatus"));
                    item.setoReceiverName(rs.getString("oReceiverName"));
                    item.setoReceieverGender(rs.getString("oReceieverGender"));
                    item.setoReceieverEmail(rs.getString("oReceieverEmail"));
                    item.setoDeliveryAddress(rs.getString("oDeliveryAddress"));
                    item.setoPostalCode(rs.getString("oPostalCode"));
                    item.setoContactPhone(rs.getString("oContactPhone"));
                    item.setoDeliveryNote(rs.getString("oDeliveryNote"));
                    res.add(item);
                    MAP.put(rs.getString("oOrderID"),true);
                }
            }
            rs.close();
            prepare.close();

            if(searchinput!=null && searchinput.length()>0)
            {
                PreparedStatement prepare2=con.prepareStatement(sql2);
                prepare2.setString(1,searchinput);
                prepare2.setString(2,searchinput);
                prepare2.setString(3,searchinput);
                prepare2.setString(4,searchinput);
                prepare2.setString(5,searchinput);
                prepare2.setString(6,searchinput);
                prepare2.setString(7,searchinput);
                prepare2.setString(8,searchinput);
                prepare2.setString(9,searchinput);
                ResultSet rs2=prepare2.executeQuery();
                while(rs2.next())
                {
                    if(MAP.get(rs2.getString("oOrderID"))==null)
                    {
                        OrderFullInfoTableItem item=new OrderFullInfoTableItem();
                        item.setoOrderID(rs2.getString("oOrderID"));
                        item.setoOrdererID(rs2.getString("oOrdererID"));
                        item.setoDate(rs2.getString("oDate"));
                        item.setoStatus(rs2.getString("oStatus"));
                        item.setoReceiverName(rs2.getString("oReceiverName"));
                        item.setoReceieverGender(rs2.getString("oReceieverGender"));
                        item.setoReceieverEmail(rs2.getString("oReceieverEmail"));
                        item.setoDeliveryAddress(rs2.getString("oDeliveryAddress"));
                        item.setoPostalCode(rs2.getString("oPostalCode"));
                        item.setoContactPhone(rs2.getString("oContactPhone"));
                        item.setoDeliveryNote(rs2.getString("oDeliveryNote"));
                        res.add(item);
                        MAP.put(rs2.getString("oOrderID"),true);
                    }
                }
                rs2.close();
                prepare2.close();
            }
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

    //-----------------------------------筛选接口-----------------------------------//

    //-----------------------------------更新接口-----------------------------------//

    @CrossOrigin(origins="*")
    @RequestMapping("/api/AdminUserAccountTableUpdate")
    public String  AdminUserAccountTableUpdate(@RequestBody UserAccountTableItem newInfo)     //更新UserAccountTable的接口
    {
        return AdminUserAccountTableUpdateResult(newInfo)?AdminStatus.Success:AdminStatus.Fail;
    }
    private boolean AdminUserAccountTableUpdateResult(UserAccountTableItem newInfo)
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
    @RequestMapping("/api/AdminUserDeliveryInfoTableUpdate")
    public String UserDeliveryInfoTableUpdate(@RequestBody UserDeliveryInfoTableItem newInfo)   //更新收货信息的接口
    {
        return AdminUserDeliveryInfoTableUpdateResult(newInfo)?AdminStatus.Success:AdminStatus.Fail;
    }
    private boolean AdminUserDeliveryInfoTableUpdateResult(UserDeliveryInfoTableItem newInfo)
    {
        boolean res=false;
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con= DriverManager.getConnection(url,user,password);

            String sql1="UPDATE UserDeliveryInfoTable SET " +
                    "UserDeliveryInfoTable.uDeliveryAddress=?, UserDeliveryInfoTable.uContactPersonName=?, " +
                    "UserDeliveryInfoTable.uContactPersonPhone=?, UserDeliveryInfoTable.uContactPersonGender=?, " +
                    "UserDeliveryInfoTable.oReceieverEmail=?, UserDeliveryInfoTable.oPostalCode=?, " +
                    "UserDeliveryInfoTable.oDeliveryNote=? WHERE UserDeliveryInfoTable.uID=? AND UserDeliveryInfoTable.uDIndex=?;";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1, newInfo.getuDeliveryAddress());
            prepare.setString(2, newInfo.getuContactPersonName());
            prepare.setString(3, newInfo.getuContactPersonPhone());
            prepare.setString(4, newInfo.getuContactPersonGender());
            prepare.setString(5,newInfo.getuContactPersonEmail());
            prepare.setString(6,newInfo.getoPostalCode());
            prepare.setString(7,newInfo.getoDeliveryNote());
            prepare.setString(8, newInfo.getuID());
            prepare.setInt(9, newInfo.getuDIndex());
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

    @CrossOrigin(origins="*")
    @RequestMapping("/api/AdminProductTableUpdate")
    public String AdminProductTableUpdate(ProductInfoUpdate_jsonGet updateInfo)         //更商户界面的更新商品信息的接口是一样的
    {
        return ProductStatusUpdateResult(updateInfo)?AdminStatus.Success:AdminStatus.Fail;
    }
    private String saveFile(MultipartFile file, String saveDir, String pID) throws IOException    //返回加工过的文件名，函数里面将图片文件保存至磁盘里面
    {
        String originalFilename = file.getOriginalFilename();//文件名(带后缀)
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));//文件后缀名
        String newFileName =pID+"_"+ UUID.randomUUID()+suffix;//由商品编号和随机乱码组成新的文件名称

        //文件保存到硬盘
        File des=new File(saveDir+newFileName);
        file.transferTo(des);
        String webPath=newFileName;

        return webPath;
    }
    private boolean ProductStatusUpdateResult(ProductInfoUpdate_jsonGet updateInfo) //可能需要删除一些照片
    {
        {
            System.out.println("************ProductStatusUpdateResult*************");
            System.out.println("商户更新商品的信息");
            System.out.println(updateInfo.getpID());
            System.out.println(updateInfo.getpName());
            System.out.println(updateInfo.getpType());
            System.out.println(updateInfo.getpDiscount());
            System.out.println(updateInfo.getpPrice());
            System.out.println(updateInfo.getpInfo());
            System.out.println(updateInfo.getpInventory());
            System.out.println(updateInfo.getpStatus());
            System.out.println("************ProductStatusUpdateResult*************");
        }

        boolean res1=false,res2=false,res3=false,res4=false;

        //检查缩略图是否更新
        boolean ThumbnailUpdate=true;
        if(updateInfo.getNewThumbnailPicURL()==null)
        {
            ThumbnailUpdate=true;
        }
        else if(updateInfo.getNewThumbnailPicURL().equals(updateInfo.getOldThumbnailPicURL()))//缩略图未更新
        {
            ThumbnailUpdate=false;
        }

        //检查展示图是否有更新
        HashMap<String,Boolean> mapNew=new HashMap<String,Boolean>();
        for(String str:updateInfo.newShowcaseImagesURL)
        {
            mapNew.put(str,true);
        }
        ArrayList<String>NeedToDelete=new ArrayList<>();
        for(String str:updateInfo.oldShowcaseImagesURL)
        {
            if(mapNew.get(str)==null)
            {
                NeedToDelete.add(str);
            }
        }

        //更新ProductTable表上的信息
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            //更新ProductTable表上的信息
            String sql1="UPDATE ProductTable SET ProductTable.pName=?, ProductTable.pType=?, ProductTable.pDiscount=?, " +
                    "ProductTable.pPrice=?, ProductTable.pProducer=?, ProductTable.pReleaseDate=?, " +
                    "ProductTable.pInfo=?, ProductTable.pInventory=?, ProductTable.pStatus=? " +
                    "WHERE ProductTable.pID=? ;";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,updateInfo.getpName());
            prepare.setString(2,updateInfo.getpType());
            prepare.setDouble(3,updateInfo.getpDiscount());
            prepare.setDouble(4,updateInfo.getpPrice());
            prepare.setString(5,updateInfo.getpProducer());
            prepare.setString(6,updateInfo.getpReleaseDate());
            prepare.setString(7,updateInfo.getpInfo());
            prepare.setInt(8,updateInfo.getpInventory());
            prepare.setString(9,updateInfo.getpStatus());
            prepare.setString(10,updateInfo.getpID());
            int row=prepare.executeUpdate();
            if(row>0)
            {
                System.out.println("商品"+updateInfo.getpID()+"信息更新成功");
                res1=true;
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

        //更新ProductImagesTable表上的图片信息-缩略图
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            if(ThumbnailUpdate==true)//缩略图需要更新
            {
                //服务器电脑硬盘上删除旧的图片
                String projectRoot=System.getProperty("user.dir");
                String oldImagePath=projectRoot+updateInfo.getOldThumbnailPicURL();
                File file=new File(oldImagePath);
                if(file.exists())
                {
                    boolean delRes=file.delete();
                    if(delRes)
                    {
                        System.out.println("缩略图 : "+oldImagePath+"删除成功");
                    }
                    else
                    {
                        System.out.println("缩略图 : "+oldImagePath+"删除失败");
                    }
                }

                //服务器电脑硬盘上保存新的图片
                String newURL="/assets/images/"+saveFile(updateInfo.pThumbnail.getpImagePath(),projectRoot+"/assets/images/",updateInfo.getpID());

                //更新数据库里的缩略图路径信息
                String sql2="UPDATE ProductImagesTable SET ProductImagesTable.pImagePath=? WHERE ProductImagesTable.pID=? AND ProductImagesTable.pImgType='缩略图';";
                PreparedStatement prepare2=con.prepareStatement(sql2);
                prepare2.setString(1,newURL);
                prepare2.setString(2,updateInfo.getpID());
                int row=prepare2.executeUpdate();
                if(row>0)
                {
                    System.out.println(newURL+" 已在数据库里面设置好");
                    res2=true;
                }
                prepare2.close();
            }
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

        //服务器硬盘上移除用户放弃的图片，以及从数据库里移除该记录
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            for(String str:NeedToDelete)
            {
                //服务器硬盘上删除被用户放弃的展示图
                String projectRoot=System.getProperty("user.dir");
                String oldImagePath=projectRoot+str;
                File file=new File(oldImagePath);
                if(file.exists())
                {
                    boolean delRes=file.delete();
                    if(delRes)
                    {
                        System.out.println("展示图 : "+oldImagePath+"删除成功");
                    }
                    else
                    {
                        System.out.println("展示图 : "+oldImagePath+"删除失败");
                    }
                }

                //在数据库上面删除这条旧的记录
                String sql3="DELETE FROM ProductImagesTable WHERE pID=? AND pImgType='展示图' AND pImagePath=?;";
                PreparedStatement prepare3=con.prepareStatement(sql3);
                prepare3.setString(1,updateInfo.getpID());
                prepare3.setString(2,str);
                int row=prepare3.executeUpdate();
                if(row>0)
                {
                    System.out.println("旧展示图"+str+"在数据库里面删除成功");
                }
                prepare3.close();
            }
            con.close();
            res3=true;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //如果有新添加的图片就在服务器硬盘上保存，并在数据库里面添加上该展示图的记录
        if(updateInfo.pShowcaseImageList!=null && updateInfo.pShowcaseImageList.size()>0)
        {
            try
            {
                Class.forName("com.kingbase8.Driver");
                Connection con=DriverManager.getConnection(url,user,password);

                String projectRoot=System.getProperty("user.dir");
                String targetFolder=projectRoot+"/assets/images/";

                //往数据库里面添加新的记录
                String sql4="INSERT INTO ProductImagesTable (pID,pImgType,pImagePath)VALUES(?,?,?);";
                PreparedStatement prepare4=con.prepareStatement(sql4);

                for(ProductImageItem item:updateInfo.pShowcaseImageList)
                {
                    //服务器硬盘上保存新的展示图片
                    String newURL="/assets/images/"+saveFile(item.getpImagePath(), targetFolder,updateInfo.getpID());

                    prepare4.setString(1,updateInfo.getpID());
                    prepare4.setString(2,"展示图");
                    prepare4.setString(3,newURL);
                    prepare4.addBatch();//批量插入
                }
                int rows[]=prepare4.executeBatch();
                con.commit();//提交事务
                if(rows.length>0)
                {
                    System.out.println("成功插入 "+rows.length+"张展示图");
                    res4=true;
                }
                prepare4.close();
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
        }
        System.out.println(res1+" "+res2+" "+res3+" "+res4);
        if(res1==true)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @CrossOrigin(origins="*")
    @RequestMapping("/api/AdminOrderBasicInfoTableUpdate")
    public String AdminOrderBasicInfoTableUpdate(@RequestBody OrderFullInfoTableItem para)
    {
        return AdminOrderBasicInfoTableUpdateResult(para)?AdminStatus.Success:AdminStatus.Fail;
    }
    private boolean AdminOrderBasicInfoTableUpdateResult(OrderFullInfoTableItem para)
    {
        boolean res=false;
        try
        {
            Connection con=DBUtil.getConnection();
            String sql1="UPDATE OrderBasicInfoTable SET OrderBasicInfoTable.oStatus=? WHERE OrderBasicInfoTable.oOrderID=?;";
            PreparedStatement prepare1=con.prepareStatement(sql1);
            prepare1.setString(1,para.getoStatus());
            prepare1.setString(2,para.getoOrderID());
            int row=prepare1.executeUpdate();
            if(row>0)
            {
                res=true;
            }
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
    @RequestMapping("/api/AdminOrderProductInfoTableUpdate")
    public String AdminOrderProductInfoTableUpdate(@RequestBody OrderProductInfoTableItem para)
    {
        return AdminOrderProductInfoTableUpdateResult(para)?AdminStatus.Success:AdminStatus.Fail;
    }
    private boolean AdminOrderProductInfoTableUpdateResult(OrderProductInfoTableItem para)
    {
        boolean res=false;
        try
        {
            Connection con= DBUtil.getConnection();
            String sql1="UPDATE OrderProductInfoTable SET OrderProductInfoTable.oProductDeliveryStatus=? " +
                    "WHERE OrderProductInfoTable.oOrderID=? AND OrderProductInfoTable.pID=?;";
            PreparedStatement prepare1=con.prepareStatement(sql1);
            prepare1.setString(1,para.getoProductDeliveryStatus());
            prepare1.setString(2,para.getoOrderID());
            prepare1.setString(3,para.getpID());
            int row=prepare1.executeUpdate();
            if(row>0)
            {
                res=true;
            }
            prepare1.close();
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

    //-----------------------------------更新接口-----------------------------------//

    //-----------------------------返回具体的信息的接口----------------------------//

    @CrossOrigin(origins="*")
    @RequestMapping("/api/AdminGetUserDeliveryInfo")                                                                         //返回被点击的账号记录的所有收货信息
    public ApiResult<ArrayList<UserDeliveryInfoTableItem>> AdminGetUserDeliveryInfo(@RequestBody UserAccountTableItem para)     //参数只用填写账号信息
    {
        return ApiResult.success(AdminGetUserDeliveryInfoResult(para));
    }
    private ArrayList<UserDeliveryInfoTableItem> AdminGetUserDeliveryInfoResult(UserAccountTableItem para)
    {
        {
            System.out.println("*****************AdminGetUserDeliveryInfoResult********************");
            System.out.println(para.getuID());
            System.out.println("*****************AdminGetUserDeliveryInfoResult********************");
        }

        ArrayList<UserDeliveryInfoTableItem> res=new ArrayList<>();
        try
        {
            Connection con=DBUtil.getConnection();
            String sql1="SELECT * FROM UserDeliveryInfoTable WHERE uID=?;";
            PreparedStatement prepare1=con.prepareStatement(sql1);
            prepare1.setString(1,para.getuID());
            ResultSet rs=prepare1.executeQuery();
            while(rs.next())
            {
                UserDeliveryInfoTableItem item=new UserDeliveryInfoTableItem();
                item.setuID(rs.getString("uID"));
                item.setoDeliveryNote(rs.getString("oDeliveryNote"));
                item.setoPostalCode(rs.getString("oPostalCode"));
                item.setuContactPersonEmail(rs.getString("oReceieverEmail"));
                item.setuDeliveryAddress(rs.getString("uDeliveryAddress"));
                item.setuContactPersonGender(rs.getString("uContactPersonGender"));
                item.setuContactPersonPhone(rs.getString("uContactPersonPhone"));
                item.setuContactPersonName(rs.getString("uContactPersonName"));
                item.setuDIndex(Integer.parseInt(rs.getString("uDIndex")));
                res.add(item);
            }
            rs.close();
            prepare1.close();
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
    @RequestMapping("/api/AdminGetUserOrderInfo")                                                                        //返回被点击的账号记录的所有订购信息
    public ApiResult<ArrayList<OrderFullInfoTableItem>> AdminGetUserOrderInfo(@RequestBody UserAccountTableItem para)       //参数只用填写账号信息
    {
        return ApiResult.success(AdminGetUserOrderInfoResult(para));
    }
    private ArrayList<OrderFullInfoTableItem> AdminGetUserOrderInfoResult(UserAccountTableItem para)
    {
        {
            System.out.println("*******************************AdminGetUserOrderInfoResult*******************************");
            System.out.println(para.getuID());
            System.out.println("*******************************AdminGetUserOrderInfoResult*******************************");
        }

        ArrayList<OrderFullInfoTableItem> res=new ArrayList<>();
        try
        {
            Connection con= DBUtil.getConnection();
            String sql1="SELECT " +
                    "OrderGeneralInfoTable.oOrderID, OrderGeneralInfoTable.oOrdererID, " +
                    "OrderBasicInfoTable.oDate, OrderBasicInfoTable.oStatus, " +
                    "OrdererInfoTable.oReceiverName, OrdererInfoTable.oReceieverGender, OrdererInfoTable.oReceieverEmail, " +
                    "OrderDeliveryInfo.oDeliveryAddress, OrderDeliveryInfo.oPostalCode, OrderDeliveryInfo.oContactPhone, OrderDeliveryInfo.oDeliveryNote " +
                    "FROM OrderGeneralInfoTable " +
                    "INNER JOIN OrderBasicInfoTable ON OrderGeneralInfoTable.oOrderID=OrderBasicInfoTable.oOrderID " +
                    "INNER JOIN OrdererInfoTable ON OrdererInfoTable.oOrderID=OrderGeneralInfoTable.oOrderID " +
                    "INNER JOIN OrderDeliveryInfo ON OrderDeliveryInfo.oOrderID=OrderGeneralInfoTable.oOrderID " +
                    "WHERE OrderGeneralInfoTable.oOrdererID = ? ;";
            PreparedStatement prepare1=con.prepareStatement(sql1);
            prepare1.setString(1,para.getuID());
            ResultSet rs=prepare1.executeQuery();
            while(rs.next())
            {
                OrderFullInfoTableItem item=new OrderFullInfoTableItem();
                item.setoOrderID(rs.getString("oOrderID"));
                item.setoOrdererID(rs.getString("oOrdererID"));
                item.setoDate(rs.getString("oDate"));
                item.setoStatus(rs.getString("oStatus"));
                item.setoReceiverName(rs.getString("oReceiverName"));
                item.setoReceieverGender(rs.getString("oReceieverGender"));
                item.setoReceieverEmail(rs.getString("oReceieverEmail"));
                item.setoDeliveryAddress(rs.getString("oDeliveryAddress"));
                item.setoPostalCode(rs.getString("oPostalCode"));
                item.setoContactPhone(rs.getString("oContactPhone"));
                item.setoDeliveryNote(rs.getString("oDeliveryNote"));
                res.add(item);
            }
            rs.close();
            prepare1.close();
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
    @RequestMapping("/api/AdminGetOrderSpecificInfo")                                                                                //被点击的订单信息的订购的商品信息
    public ApiResult<ArrayList<OrderProductInfoTableItem>> AdminGetOrderSpecificInfo(@RequestBody OrderProductInfoTableItem para)       //参数只用填写订单号oOrderID
    {
        return ApiResult.success(AdminGetOrderSpecificInfoResult(para));
    }
    private ArrayList<OrderProductInfoTableItem> AdminGetOrderSpecificInfoResult(OrderProductInfoTableItem para)
    {
        {
            System.out.println("******************************AdminGetOrderSpecificInfoResult***********************************");
            System.out.println(para.getoOrderID());
            System.out.println("******************************AdminGetOrderSpecificInfoResult***********************************");
        }

        ArrayList<OrderProductInfoTableItem> res=new ArrayList<>();
        try
        {
            Connection con= DBUtil.getConnection();
            String sql1="SELECT * FROM OrderProductInfoTable WHERE oOrderID = ? ;";
            PreparedStatement prepare1=con.prepareStatement(sql1);
            prepare1.setString(1,para.getoOrderID());
            ResultSet rs=prepare1.executeQuery();
            while(rs.next())
            {
                OrderProductInfoTableItem item=new OrderProductInfoTableItem();
                item.setoOrderID(rs.getString("oOrderID"));
                item.setpID(rs.getString("pID"));
                item.setoPrice(rs.getDouble("oPrice"));
                item.setoAmount(rs.getInt("oAmount"));
                item.setoProductDeliveryStatus(rs.getString("oProductDeliveryStatus"));
                res.add(item);
            }
            rs.close();
            prepare1.close();
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
    @RequestMapping("/api/AdminGetSpecificProductInfo")
    public ApiResult<ArrayList<ProductTableItem>> AdminGetSpecificProductInfo(@RequestBody ProductTableItem para)       //参数只用填写商品的编号
    {
        return ApiResult.success(AdminGetSpecificProductInfoResult(para));
    }
    private ArrayList<ProductTableItem> AdminGetSpecificProductInfoResult(ProductTableItem para)
    {
        {
            System.out.println("****************************AdminGetSpecificProductInfoResult***********************************");
            System.out.println(para.getpID());
            System.out.println("****************************AdminGetSpecificProductInfoResult***********************************");
        }

        ArrayList<ProductTableItem> res=new ArrayList<>();
        try
        {
            Connection con= DBUtil.getConnection();
            String sql1="SELECT * FROM ProductTable WHERE pID = ? ;";
            PreparedStatement prepare1=con.prepareStatement(sql1);
            prepare1.setString(1,para.getpID());
            ResultSet rs=prepare1.executeQuery();
            if(rs.next())
            {
                ProductTableItem item=new ProductTableItem();
                item.setpID(rs.getString("pID"));
                item.setpName(rs.getString("pName"));
                item.setpType(rs.getString("pType"));
                item.setpDiscount(rs.getDouble("pDiscount"));
                item.setpPrice(rs.getDouble("pPrice"));
                item.setpProducer(rs.getString("pProducer"));
                item.setpReleaseDate(rs.getString("pReleaseDate"));
                item.setpInfo(rs.getString("pInfo"));
                item.setpInventory(rs.getInt("pInventory"));
                item.setpStatus(rs.getString("pStatus"));
                res.add(item);
            }
            rs.close();
            prepare1.close();
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

    //-----------------------------返回具体用户的信息的接口----------------------------//

}
