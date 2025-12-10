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
                    "OrderGeneralInfoTable.oOrderID, OrderGeneralInfoTable.oOrderID, " +
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
    //-----------------------------------筛选接口-----------------------------------//

    //-----------------------------------更新接口-----------------------------------//

    @CrossOrigin(origins="*")
    @RequestMapping("/api/AdminUserAccountTableUpdate")
    public String  AdminUserAccountTableUpdate(@RequestBody UserAccountTableItem newInfo)     //更新UserAccountTable的接口
    {
        if(AdminUserAccountTableUpdateResult(newInfo)) {   return AdminStatus.Success;    }
        else {   return AdminStatus.Fail;    }
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
    public String UserDeliveryInfoTableUpdate(@RequestBody UserDeliveryInfoTableItem newInfo)
    {
        if(AdminUserDeliveryInfoTableUpdateResult(newInfo)) {   return AdminStatus.Success; }
        else {   return AdminStatus.Fail;    }
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




    //-----------------------------------更新接口-----------------------------------//

}
