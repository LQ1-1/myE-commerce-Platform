package com.lqh.ebuyplt_1001p.Controller;


import com.lqh.ebuyplt_1001p.Controller.ResultPack.ApiResult;
import org.springframework.web.bind.annotation.*;

import com.lqh.ebuyplt_1001p.Controller.MerchantPack.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
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
            Class.forName("com.kingbase8.Driver");
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
                boolean insertResult=insertIntoProductImagesTable(form.getpID(),"缩略图",coverUrl);
                if(insertResult)
                {
                    System.out.println("缩略图插入成功 : "+coverUrl);
                }
                else
                {
                    System.out.println("缩略图插入失败");
                }
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

                //将这些展示图的URL添加进数据库里面
                for(String str : galleryUrls)
                {
                    boolean insertResult=insertIntoProductImagesTable(form.getpID(),"展示图",str);
                    if(insertResult)
                    {
                        System.out.println("展示图插入成功 : "+str);
                    }
                    else
                    {
                        System.out.println("展示图插入失败");
                    }
                }
            }

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
    private String saveFile(MultipartFile file,String saveDir,String pID) throws IOException    //返回加工过的文件名，函数里面将图片文件保存至磁盘里面
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
    private boolean insertIntoProductImagesTable(String ipID,String ipImgType,String ipImagePath)
    {
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="INSERT INTO ProductImagesTable(pID,pImgType,pImagePath)VALUES(?,?,?);";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,ipID);
            prepare.setString(2,ipImgType);
            prepare.setString(3,ipImagePath);

            int row=prepare.executeUpdate();
            if(row>0)
            {
                return true;
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

    //从后端返回该用户的所有商品的所有信息给前端
    @CrossOrigin(origins="*")
    @RequestMapping("/api/ProductAllInfo")
    public ApiResult<ProductAllInfo_jsonSend> ProductAllInfo(@RequestBody ProductRecord_jsonGet recordCall)
    {
        return ApiResult.success(ProductAllInfoResult(recordCall));
    }
    private ProductAllInfo_jsonSend ProductAllInfoResult(ProductRecord_jsonGet recordCall)
    {
        ProductAllInfo_jsonSend res=new ProductAllInfo_jsonSend();
        ArrayList<String>pIDs=new ArrayList<>();
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="SELECT * FROM MerchantsProductTable WHERE uID=?;";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,recordCall.getuID());
            ResultSet rs=prepare.executeQuery();
            while(rs.next())
            {
                String pID=rs.getString("pID");
                pIDs.add(pID);
            }

            for(String pID : pIDs)
            {
                ProductIAllnfoItem item=new ProductIAllnfoItem();

                String sub_sql="SELECT * FROM ProductTable WHERE pID=?;";
                PreparedStatement prepare2=con.prepareStatement(sub_sql);
                prepare2.setString(1,pID);
                ResultSet rs2=prepare2.executeQuery();
                if(rs2.next())  //设置文本类型的信息
                {
                    item.setpID(rs2.getString("pID"));
                    item.setpName(rs2.getString("pName"));
                    item.setpType(rs2.getString("pType"));
                    item.setpDiscount(rs2.getDouble("pDiscount"));
                    item.setpPrice(rs2.getDouble("pPrice"));
                    item.setpInfo(rs2.getString("pInfo"));
                    item.setpInventory(rs2.getInt("pInventory"));
                    item.setpStatus(rs2.getString("pStatus"));
                }

                String sub_sql2="SELECT * FROM ProductImagesTable WHERE pID=?;";
                PreparedStatement prepare3=con.prepareStatement(sub_sql2);
                prepare3.setString(1,pID);
                ResultSet rs3=prepare3.executeQuery();
                while(rs3.next())
                {
                    String pImgType=rs3.getString("pImgType");
                    String pImagePath=rs3.getString("pImagePath");

                    if(pImgType.equals("缩略图"))
                    {
                        item.setpThumbnail(pImagePath);
                    }
                    else if(pImgType.equals("展示图"))
                    {
                        item.pShowcaseImageList.add(pImagePath);
                    }
                }

                res.AllInfo.add(item);
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

    @CrossOrigin(origins="*")
    @RequestMapping("/api/ProductInfoUpdate")
    public String ProductStatusUpdate(@RequestBody ProductInfoUpdate_jsonGet updateInfo)    //ProductInfoUpdate_jsonGet是从后端返回该商品的所有信息，然后让商户修改，商户确认修改之后再将所有的信息发送给后端
    {
        if(ProductStatusUpdateResult(updateInfo))
        {
            return PutOnSaleStatus.UpdateSuccess;
        }
        else
        {
            return PutOnSaleStatus.UpdateFail;
        }
    }
    private boolean ProductStatusUpdateResult(ProductInfoUpdate_jsonGet updateInfo) //可能需要删除一些照片
    {
        boolean res1=false,res2=false,res3=false,res4=false;

        //检查缩略图是否更新
        boolean ThumbnailUpdate=true;
        if(updateInfo.getNewThumbnailPicURL().equals(updateInfo.getOldThumbnailPicURL()))//缩略图未更新
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
            int row=prepare.executeUpdate();
            if(row>0)
            {
                System.out.println("商品"+updateInfo.getpID()+"信息更新成功");
                res1=true;
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
            }
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

                for(ProductImageItem item:updateInfo.pShowcaseImageList)
                {
                    //服务器硬盘上保存新的展示图片
                    String newURL="/assets/images/"+saveFile(item.getpImagePath(), targetFolder,updateInfo.getpID());

                    //往数据库里面添加新的记录
                    String sql4="INSERT INTO (pID,pImgType,pImagePath)VALUES(?,?,?);";
                    PreparedStatement prepare4=con.prepareStatement(sql4);
                    prepare4.setString(1,updateInfo.getpID());
                    prepare4.setString(2,"展示图");
                    prepare4.setString(3,newURL);
                    int row=prepare4.executeUpdate();
                    if(row>0)
                    {
                        System.out.println("新的展示图"+newURL+"在数据库里保存成功");
                    }
                }
                res4=true;
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
        if(res1==true && res2==true && res3==true && res4==true)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
