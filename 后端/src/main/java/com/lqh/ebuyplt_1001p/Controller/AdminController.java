package com.lqh.ebuyplt_1001p.Controller;


import com.lqh.ebuyplt_1001p.Controller.AdminPack.UserAccountTableItem;
import com.lqh.ebuyplt_1001p.Controller.AdminPack.UserDeliveryInfoTableItem;
import com.lqh.ebuyplt_1001p.Controller.AdminPack.UserFavoritesTableItem;
import com.lqh.ebuyplt_1001p.Controller.AdminPack.*;
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
    @RequestMapping("/api/MerchantsProductTable")
    public ApiResult<ArrayList<MerchantsProductTableItem>> MerchantsProductTable()      //获取所有商户的所有上架记录
    {
        return ApiResult.success(MerchantsProductTableResult());
    }
    private ArrayList<MerchantsProductTableItem> MerchantsProductTableResult()
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
    @RequestMapping("/api/ProductTable")
    public ApiResult<ArrayList<ProductTableItem>> ProductTable()        //返回所有商品的记录
    {
        return ApiResult.success(ProductTableResult());
    }
    private ArrayList<ProductTableItem> ProductTableResult()
    {
        ArrayList<ProductTableItem> res=new ArrayList<>();
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con= DriverManager.getConnection(url,user,password);
            String sql1="SELECT * FROM ProductTable;";
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
    @RequestMapping("/api/ProductImagesTable")
    public ApiResult<ArrayList<ProductImagesTableItem>> ProductImagesTable()    //返回所有商品的所有图片记录信息
    {
        return ApiResult.success(ProductImagesTableResult());
    }
    private ArrayList<ProductImagesTableItem> ProductImagesTableResult()
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
                item.setpType(rs.getString("pType"));
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
    @RequestMapping("/api/ProductClicksInfoTable")
    public ApiResult<ArrayList<ProductClicksInfoTableItem>> ProductClicksInfoTable()    //返回所有商品的点击次数记录信息
    {
        return ApiResult.success(ProductClicksInfoTableResult());
    }
    private ArrayList<ProductClicksInfoTableItem> ProductClicksInfoTableResult()
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
    @RequestMapping("/api/OrderFullInfoTable")
    public ApiResult<ArrayList<OrderFullInfoTableItem>> OrderFullInfoTable()
    {
        return ApiResult.success(OrderFullInfoTableResult());
    }
    private ArrayList<OrderFullInfoTableItem> OrderFullInfoTableResult()
    {
        ArrayList<OrderFullInfoTableItem> res=new ArrayList<>();
        try
        {
            Class.forName("com.kingbase8.Driver");
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
