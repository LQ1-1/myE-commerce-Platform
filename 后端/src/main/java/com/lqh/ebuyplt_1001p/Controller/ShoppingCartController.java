package com.lqh.ebuyplt_1001p.Controller;

import com.lqh.ebuyplt_1001p.Controller.ResultPack.ApiResult;
import com.lqh.ebuyplt_1001p.Controller.ShoppingCartPack.*;
import com.lqh.ebuyplt_1001p.Controller.FavouritePack.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.*;
import java.sql.SQLException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

@RestController
public class ShoppingCartController
{
    private static String url="jdbc:kingbase8://localhost:54321/db_ebuyplt2";
    private static String user="system";
    private static String password="qh20050908";

    //添加到购物车模块
    @CrossOrigin(origins="*")
    @RequestMapping("/api/ShoppingCartAdd")
    public String ShoppingCartAdd(@RequestBody ShoppingCartAdd_jsonGet shoppingCartAdd)
    {
        return ShoppingCartAddResult(shoppingCartAdd);
    }
    private String ShoppingCartAddResult(ShoppingCartAdd_jsonGet shoppingCartAdd)
    {
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="SELECT UserShoppingCartTableAdd(?,?,?);";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,shoppingCartAdd.getuID());
            prepare.setString(2,shoppingCartAdd.getpID());
            prepare.setInt(3,shoppingCartAdd.getcAmount());

            ResultSet rs=prepare.executeQuery();
            if(rs.next())
            {
                boolean flag=rs.getBoolean(1);
                if(flag)
                {
                    rs.close();
                    prepare.close();
                    return ShoppingCartAddResult.Success;
                }
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
        return ShoppingCartAddResult.Fail;
    }


    @CrossOrigin(origins="*")
    @RequestMapping("/api/ShoppingCartRemove")
    public void ShoppingCartRemove(@RequestBody ShoppingCartRemove_jsonGet shoppingCartRemove)//单个移除
    {
        boolean result=ShoppingCartRemoveResult(shoppingCartRemove);
        if(!result)
        {
            System.out.println("移除失败");
            System.out.println(shoppingCartRemove.getuID());
            System.out.println(shoppingCartRemove.getpID());
            System.out.println(shoppingCartRemove.getcAmount());
        }
    }
    private boolean ShoppingCartRemoveResult(ShoppingCartRemove_jsonGet shoppingCartRemove)
    {
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sq1="DELETE FROM UserShoppingCartTable WHERE uID=? AND pID=?;";
            PreparedStatement prepare=con.prepareStatement(sq1);
            prepare.setString(1,shoppingCartRemove.getuID());
            prepare.setString(2,shoppingCartRemove.getpID());
            int row=prepare.executeUpdate();
            if(row>0)
            {
                prepare.close();
                con.close();
                return  true;
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
        return false;
    }

    @CrossOrigin(origins="*")
    @RequestMapping("/api/ShoppingCartMultipleRemove")
    public void ShoppingCartMultipleRemove(@RequestBody ShoppingCartMultipleRemove_jsonGet shoppingCartMultipleRemove)
    {
        boolean result=ShoppingCartMultipleRemoveResult(shoppingCartMultipleRemove);
        if(!result)
        {
            System.out.println("移除失败");
            for(int i=0;i<shoppingCartMultipleRemove.RemoveList.size();i++)
            {
                System.out.println(shoppingCartMultipleRemove.RemoveList.get(i));
            }
        }
    }
    private boolean ShoppingCartMultipleRemoveResult(ShoppingCartMultipleRemove_jsonGet shoppingCartMultipleRemove)
    {
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            StringBuilder sql1=new StringBuilder("DELETE FROM UserShoppingCartTable WHERE (uID,pID) IN (");
            for(int i=0;i<shoppingCartMultipleRemove.RemoveList.size();i++)
            {
                if(i==shoppingCartMultipleRemove.RemoveList.size()-1)
                {
                    sql1.append("("+shoppingCartMultipleRemove.RemoveList.get(i).getpID()+","+shoppingCartMultipleRemove.RemoveList.get(i).getuID()+"));");
                }
                else
                {
                    sql1.append("("+shoppingCartMultipleRemove.RemoveList.get(i).getpID()+","+shoppingCartMultipleRemove.RemoveList.get(i).getuID()+"),");
                }
            }
            int row=con.prepareStatement(sql1.toString()).executeUpdate();
            if(row>0)
            {
                con.close();
                return true;
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
        return false;
    }

    @CrossOrigin(origins="*")
    @RequestMapping("/api/ShoppingCartRecords")
    public ApiResult<ArrayList<ShoppingCartItem>>ShoppingCartRecords(@RequestBody ShoppingCartRecords_jsonGet shoppingCartRecords_jsonGet)
    {
        ArrayList<ShoppingCartItem>shoppingCartItems=ShoppingCartRecordsResult(shoppingCartRecords_jsonGet);
        return ApiResult.success(shoppingCartItems);
    }
    private ArrayList<ShoppingCartItem> ShoppingCartRecordsResult(ShoppingCartRecords_jsonGet shoppingCartRecords_jsonGet)
    {
        ArrayList<ShoppingCartItem>res=new ArrayList<ShoppingCartItem>();
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="SELECT UserShoppingCartTable.pID,UserShoppingCartTable.cAmount,ProductTable.pName,ProductTable.pType," +
                    "ProductTable.pPrice,ProductImagesTable.pImagePath " +
                    "FROM UserShoppingCartTable,ProductTable,ProductImagesTable " +
                    "WHERE UserShoppingCartTable.pID=ProductTable.pID AND " +
                    "ProductImagesTable.pID=UserShoppingCartTable.pID AND " +
                    "ProductImagesTable.pImgType='缩略图' AND uID=?;";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,shoppingCartRecords_jsonGet.getuID());
            ResultSet rs=prepare.executeQuery();
            while(rs.next())
            {
                ShoppingCartItem item=new ShoppingCartItem();
                item.setpID(rs.getString("pID"));
                item.setpName(rs.getString("pName"));
                item.setpType(rs.getString("pType"));
                item.setpPrice(rs.getDouble("pPrice"));
                item.setcAmount(rs.getInt("cAmount"));
                item.setpImagesPath(rs.getString("pImagePath"));

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


    //添加收藏模块
    @CrossOrigin(origins="*")
    @RequestMapping("/api/FavouriteAdd")
    public String FavouriteAdd(@RequestBody FavouriteAdd_jsonGet favouriteAdd)
    {
        String result=FavouriteAddResult(favouriteAdd);
        return result;
    }
    private String FavouriteAddResult(FavouriteAdd_jsonGet favouriteAdd)
    {
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="INSERT INTO UserFavoritesTable (uID,pID) VALUES (?,?);";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,favouriteAdd.getuID());
            prepare.setString(2,favouriteAdd.getpID());

            int row=prepare.executeUpdate();
            if(row>0)
            {
                return FavouriteAddResult.Success;
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
        return FavouriteAddResult.Fail;
    }

    @CrossOrigin(origins="*")
    @RequestMapping("/api/FavouriteRemove")
    public void FavouriteRemove(@RequestBody FavouriteRemove_jsonGet favouriteRemove)//单个移除
    {
        boolean result=FavouriteRemoveResult(favouriteRemove);
        if(!result)
        {
            System.out.println("移除失败");
            System.out.println(favouriteRemove.getuID());
            System.out.println(favouriteRemove.getpID());
        }
    }
    private boolean FavouriteRemoveResult(FavouriteRemove_jsonGet favouriteRemove)
    {
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="DELETE FROM UserFavoritesTable WHERE uID=? AND pID=?;";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,favouriteRemove.getuID());
            prepare.setString(2,favouriteRemove.getpID());
            int row=prepare.executeUpdate();
            if(row>0)
            {
                prepare.close();
                con.close();
                return true;
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
        return false;
    }

    @CrossOrigin(origins="*")
    @RequestMapping("/api/FavouriteMultipleRemove")
    public void FavouriteMultipleRemove(@RequestBody FavouriteMultipleRemove_jsonGet favouriteMultipleRemove)
    {
        boolean result=FavouriteMultipleRemoveResult(favouriteMultipleRemove);
        if(!result)
        {
            System.out.println("移除失败");
            for(int i=0;i<favouriteMultipleRemove.RemoveList.size();i++)
            {
                System.out.println(favouriteMultipleRemove.RemoveList.get(i).getpID());
                System.out.println(favouriteMultipleRemove.RemoveList.get(i).getuID());
            }
        }
    }
    private boolean FavouriteMultipleRemoveResult(FavouriteMultipleRemove_jsonGet favouriteMultipleRemove)
    {
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            StringBuilder sql1=new StringBuilder("DELETE FROM UserFavoritesTable WHERE (uID,pID) IN (");
            for(int i=0;i<favouriteMultipleRemove.RemoveList.size();i++)
            {
                if(i==favouriteMultipleRemove.RemoveList.size()-1)
                {
                    sql1.append("("+favouriteMultipleRemove.RemoveList.get(i).getuID()+","+favouriteMultipleRemove.RemoveList.get(i).getpID()+"));");
                }
                else
                {
                    sql1.append("("+favouriteMultipleRemove.RemoveList.get(i).getuID()+","+favouriteMultipleRemove.RemoveList.get(i).getpID()+"),");
                }
            }
            int row=con.prepareStatement(sql1.toString()).executeUpdate();
            if(row>0)
            {
                con.close();
                return true;
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
        return false;
    }

    @CrossOrigin(origins="*")
    @RequestMapping("/api/FavouriteRecords")
    public ApiResult<ArrayList<FavouriteRecordItem>> FavouriteRecords(@RequestBody FavouriteRecords_jsonGet favouriteRecords_jsonGet)
    {
        ArrayList<FavouriteRecordItem>res=FavouriteRecordsResult(favouriteRecords_jsonGet);
        return ApiResult.success(res);
    }
    private ArrayList<FavouriteRecordItem>FavouriteRecordsResult(FavouriteRecords_jsonGet favouriteRecords_jsonGet)
    {
        ArrayList<FavouriteRecordItem>res=new ArrayList<>();
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="SELECT UserFavoritesTable.pID,ProductTable.pName,ProductTable.pType, "+
                    "ProductTable.pPrice,ProductImagesTable.pImagePath "+
                    "FROM UserFavoritesTable,ProductTable,ProductImagesTable "+
                    "WHERE UserFavoritesTable.pID=ProductTable.pID AND "+
                    "ProductImagesTable.pID=UserFavoritesTable.pID AND "+
                    "ProductImagesTable.pImgType='缩略图' AND uID=?;";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,favouriteRecords_jsonGet.getuID());
            ResultSet rs=prepare.executeQuery();
            while(rs.next())
            {
                FavouriteRecordItem item=new FavouriteRecordItem();
                item.setpID(rs.getString("pID"));
                item.setpName(rs.getString("pName"));
                item.setpType(rs.getString("pType"));
                item.setpPrice(rs.getDouble("pPrice"));
                item.setpImagesPath(rs.getString("pImagePath"));

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


}
