package com.lqh.ebuyplt_1001p.Controller;


import com.lqh.ebuyplt_1001p.Controller.ResultPack.ApiResult;
import org.springframework.web.bind.annotation.*;

import com.lqh.ebuyplt_1001p.Controller.MerchantPack.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        return ApiResult.success(ProductOnSaleResult(productOnSale));
    }
    private ProductOnSale_jsonSend ProductOnSaleResult(ProductOnSale_jsonGet productOnSale)
    {
        ProductOnSale_jsonSend result=new ProductOnSale_jsonSend();
        result.setStatus(PutOnSaleStatus.OnSaleFail);

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
                String New_pID = rs.getString(1);

                result.setStatus(PutOnSaleStatus.OnSaleSuccess);
                result.setpID(New_pID);
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
        return result;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/api/ProductImagesUpload")
    public String ProductImagesUpload(@ModelAttribute ProductImagesUpload form)
    {
        if(ProductImagesUploadResult(form))
        {
            return PutOnSaleStatus.OnSaleSuccess;
        }
        else
        {
            return PutOnSaleStatus.OnSaleFail;
        }
    }
    private boolean ProductImagesUploadResult(ProductImagesUpload form)
    {
        // 获取封面
        MultipartFile cover = form.getcover();

        // 获取展示图
        MultipartFile[] gallery = form.getgallery();

        String projectRoot=System.getProperty("user.dir");
        String assetsRoot=projectRoot+ File.separator+"assets"+File.separator;
        String finalRoot=assetsRoot+"images"+File.separator;

        File director=new File(finalRoot);
        if(!director.exists())//如果该文件夹不存在就创建一个
        {
            director.mkdir();
        }

        try
        {
            //处理缩略图
            String coverUrl="";
            if(cover!=null && !cover.isEmpty())
            {
                coverUrl="/assets/images/"+saveFile(cover,finalRoot,form.getpID());

                //保存到数据库里面的图片URL:coverUrl
            }

            //处理展示图
            List<String> galleryUrls=new ArrayList<>();
            if(gallery != null && gallery.length > 0)
            {
                for(MultipartFile galleryFile : gallery)
                {
                    if(!galleryFile.isEmpty())
                    {
                        String galleryUrl="/assets/images/"+saveFile(galleryFile,finalRoot,form.getpID());
                        galleryUrls.add(galleryUrl);
                    }
                }
            }

            //将这些展示图的URL添加进数据库里面

            //检查URL地址
            System.out.println("**************ProductImagesUploadResult*************");
            System.out.println("保存路径: " + finalRoot);
            System.out.println(coverUrl);
            for(String str : galleryUrls)
            {
                System.out.println(str);
            }
            return true;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    private String saveFile(MultipartFile file,String saveDir,String pID) throws IOException
    {
        String originalFilename = file.getOriginalFilename();//文件名(带后缀)
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));//文件后缀名
        String newFileName =pID+"_"+UUID.randomUUID()+suffix;//由商品编号和随机乱码组成新的文件名称

        //文件保存到硬盘
        File des=new File(saveDir+newFileName);
        file.transferTo(des);
        String webPath=newFileName;

        return webPath;
    }

}
