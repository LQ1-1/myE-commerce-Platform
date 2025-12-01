package com.lqh.ebuyplt_1001p.Controller;


import com.lqh.ebuyplt_1001p.Controller.ResultPack.ApiResult;
import org.springframework.web.bind.annotation.*;

import com.lqh.ebuyplt_1001p.Controller.MerchantPack.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.*;

@RestController
public class MerchantController
{
    private static String url="jdbc:kingbase8://localhost:54321/db_ebuyplt2";
    private static String user="system";
    private static String password="qh20050908";


    @CrossOrigin(origins = "*")
    @RequestMapping("/api/ProductOnSale")
    public ApiResult<ProductOnSale_jsonSend> ProductOnSale(@RequestBody ProductOnSale_jsonGet productOnSale)   //这里应该返回订单号回去的
    {
        return ApiResult.success(ProductOnSaleResult(productOnSale))
    }
    private ProductOnSale_jsonSend ProductOnSaleResult(ProductOnSale_jsonGet productOnSale)
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(url,user,password);

            String sql1="SELECT NewProductOnSaleFunction(?,?,?," +
                    "?,?,?," +
                    "?,?,?);";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,productOnSale.getpName());
            prepare.setString(2,productOnSale.getpType());
            prepare.setDouble(3,productOnSale.getpDiscount());
            prepare.setDouble(4,productOnSale.getpPrice());
            prepare.setString(5,productOnSale.getpProducer());
            prepare.setString(6,productOnSale.getpReleaseDate());
            prepare.setString(7,productOnSale.getpInfo());
            prepare.setInt(8,productOnSale.getpInventory());
            prepare.setString(9,productOnSale.getpStatus());

            ResultSet rs=prepare.executeQuery();
            if(rs.next())
            {
                return rs.getBoolean(1);
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
        return false;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/api/ProductImagesUpload")
    public String ProductImagesUpload(@ModelAttribute ProductImagesUpload form)
    {

    }
    private boolean ProductImagesUploadResult(ProductImagesUpload form)
    {
        // 获取封面
        MultipartFile cover = form.getcover();

        // 获取展示图
        MultipartFile[] gallery = form.getgallery();

        String projectRoot=System.getProperty("user.dir");
        String assetsRoot=projectRoot+ File.separator+"assets"+File.separator;
    }

}
