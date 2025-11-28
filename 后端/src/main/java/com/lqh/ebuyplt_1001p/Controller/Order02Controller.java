package com.lqh.ebuyplt_1001p.Controller;

import com.lqh.ebuyplt_1001p.Controller.OrderCheckPack.OrderCheckStatus;
import com.lqh.ebuyplt_1001p.Controller.OrderCheckPack.OrderStatus;
import com.lqh.ebuyplt_1001p.Controller.OrderPack2.ExistDeliveryRecords;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.lqh.ebuyplt_1001p.Controller.ResultPack.*;
import com.lqh.ebuyplt_1001p.Controller.OrderPack2.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class Order02Controller
{
    private static String url="jdbc:kingbase8://localhost:54321/db_ebuyplt2";
    private static String user="system";
    private static String password="qh20050908";

    @CrossOrigin(origins="*")
    @RequestMapping("/api/OrderConfirm_DeliveryCheck")
    public ApiResult<ExistDeliveryRecords> OrderConfirm_DeliveryCheck(@RequestBody OrderItem  orderItem)    //只需要用到uID,返回用户已经写过了的收货记录
    {
        {
            System.out.println("************OrderConfirm_DeliveryCheck************");
            System.out.println(orderItem.getuID());
            for(int i=0;i<orderItem.pProducts.size();i++)
            {
                System.out.println(orderItem.pProducts.get(i).getpID());
                System.out.println(orderItem.pProducts.get(i).getoPrice());
                System.out.println(orderItem.pProducts.get(i).getpAmount());
            }
            System.out.println(orderItem.getoDeliveryInfo());
            System.out.println("************OrderConfirm_DeliveryCheck************");
        }

        return ApiResult.success(OrderConfirm_DeliveryCheckResult(orderItem));
    }
    private ExistDeliveryRecords OrderConfirm_DeliveryCheckResult(OrderItem  orderItem)
    {
        ExistDeliveryRecords res=new ExistDeliveryRecords();
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con= DriverManager.getConnection(url,user,password);

            String sql1="SELECT * FROM UserDeliveryInfoTable WHERE uID=?;";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,orderItem.getuID());
            ResultSet rs=prepare.executeQuery();
            while(rs.next())
            {
                DeliveryInfo dInfo=new DeliveryInfo();
                dInfo.setuID(rs.getString("uID"));
                dInfo.setuDeliveryAddress(rs.getString("uDeliveryAddress"));
                dInfo.setoDeliveryNote(rs.getString("oDeliveryNote"));
                dInfo.setoPostalCode(rs.getString("oPostalCode"));

                dInfo.setuContactPersonEmail(rs.getString("oReceieverEmail"));
                dInfo.setuContactPersonName(rs.getString("uContactPersonName"));
                dInfo.setuContactPersonPhone(rs.getString("uContactPersonPhone"));
                dInfo.setuContactPersonGender(rs.getString("uContactPersonGender"));

                res.DeliveryInfos.add(dInfo);
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
    @RequestMapping("/api/OrderConfirm_NewDeliveryRecord")
    public String OrderConfirm_NewDeliveryRecord(@RequestBody DeliveryInfo  deliveryInfo)           //用户发来收货地址信息
    {
        {
            System.out.println("************OrderConfirm_NewDeliveryRecord************");
            System.out.println(deliveryInfo.getuID());
            System.out.println(deliveryInfo.getuDeliveryAddress());
            System.out.println(deliveryInfo.getoDeliveryNote());
            System.out.println(deliveryInfo.getoPostalCode());
            System.out.println(deliveryInfo.getuContactPersonEmail());
            System.out.println(deliveryInfo.getuContactPersonName());
            System.out.println(deliveryInfo.getuContactPersonPhone());
            System.out.println(deliveryInfo.getuContactPersonGender());
            System.out.println("************OrderConfirm_NewDeliveryRecord************");
        }

        if(OrderConfirm_NewDeliveryRecordResult(deliveryInfo))
        {
            return OrderPack2StatusCheck.RequestAccept;
        }
        else
        {
            return OrderPack2StatusCheck.RequestFail;
        }
    }
    public boolean OrderConfirm_NewDeliveryRecordResult(DeliveryInfo  deliveryInfo)                 //将这个收货信息存入数据库中，同时这个用户填写的新的收货信息将作为这次订单的收货信息；该收货信息在前端存着，发起订单Request时再次发送过来
    {
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con= DriverManager.getConnection(url,user,password);

            String sql1="INSERT INTO UserDeliveryInfoTable(uID,uDeliveryAddress," +
                    "uContactPersonName,uContactPersonPhone," +
                    "uContactPersonGender,oReceieverEmail," +
                    "oPostalCode,oDeliveryNote)" +
                    "VALUES(?,?,?,?," +
                    "?,?,?,?)";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,deliveryInfo.getuID());
            prepare.setString(2,deliveryInfo.getuDeliveryAddress());
            prepare.setString(3,deliveryInfo.getuContactPersonName());
            prepare.setString(4,deliveryInfo.getuContactPersonPhone());

            prepare.setString(5,deliveryInfo.getuContactPersonGender());
            prepare.setString(6,deliveryInfo.getuContactPersonEmail());
            prepare.setString(7,deliveryInfo.getoPostalCode());
            prepare.setString(8,deliveryInfo.getoDeliveryNote());

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

    @CrossOrigin(origins="*")
    @RequestMapping("/api/OrderConfirm_OrderGenerate")
    public ApiResult<OrderItem_Feedback> OrderConfirm_OrderGenerate(@RequestBody OrderItem  orderItem)
    {
        {
            System.out.println("************OrderConfirm_OrderGenerate************");
            System.out.println(orderItem.getuID());
            System.out.println(orderItem.pProducts.size());
            for(int i=0;i<orderItem.pProducts.size();i++)
            {
                System.out.println(orderItem.pProducts.get(i).getpID());
                System.out.println(orderItem.pProducts.get(i).getoPrice());
                System.out.println(orderItem.pProducts.get(i).getpAmount());
            }
            System.out.println("************OrderConfirm_OrderGenerate************");
        }

        return ApiResult.success(OrderConfirm_OrderGenerateResult(orderItem));
    }
    public OrderItem_Feedback OrderConfirm_OrderGenerateResult(OrderItem  orderItem)
    {
        //商品号与缩影的映射
        HashMap<String,Integer> map=new HashMap<String,Integer>();
        for(int i=0;i<orderItem.pProducts.size();i++)
        {
            map.put(orderItem.pProducts.get(i).getpID(),i);
        }

        OrderItem_Feedback res=new OrderItem_Feedback();
        res.setuID(orderItem.getuID());
        for(int i=0;i<orderItem.pProducts.size();i++)//res.setpProducts
        {
            ProductItem_jsonSend iniItem=new ProductItem_jsonSend();
            res.pProducts.add(iniItem);
        }
        res.setoDeliveryInfo(orderItem.getoDeliveryInfo());

        boolean flag1_AmountCheckisLegal=true;            //先检查订购的数量是否合法
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con= DriverManager.getConnection(url,user,password);

            StringBuilder sql1=new StringBuilder("SELECT ProductTable.pID,ProductTable.pInventory FROM ProductTable WHERE ");
            for(int i=0;i<orderItem.pProducts.size();i++)
            {
                if(i==orderItem.pProducts.size()-1)
                {
                    sql1.append("ProductTable.pID="+"'"+orderItem.pProducts.get(i).getpID()+"'"+";");
                }
                else
                {
                    sql1.append("ProductTable.pID="+"'"+orderItem.pProducts.get(i).getpID()+"'"+" OR ");
                }
            }

            ResultSet rs=con.createStatement().executeQuery(sql1.toString());
            while(rs.next())
            {
                String pID=rs.getString("pID");
                int pInventory=rs.getInt("pInventory");

                if(pInventory - orderItem.pProducts.get(map.get(pID)).getpAmount() >= 0)        //订购数量和法
                {
                    res.pProducts.get(map.get(pID)).setpID(pID);
                    res.pProducts.get(map.get(pID)).setpAmount(orderItem.pProducts.get(map.get(pID)).getpAmount());
                    res.pProducts.get(map.get(pID)).setpFeedback(OrderPack2StatusCheck.InventorySufficient);
                }
                else
                {
                    res.pProducts.get(map.get(pID)).setpID(pID);
                    res.pProducts.get(map.get(pID)).setpAmount(orderItem.pProducts.get(map.get(pID)).getpAmount());
                    res.pProducts.get(map.get(pID)).setpFeedback(OrderPack2StatusCheck.InventoryInsufficient);
                    res.setStatus(OrderPack2StatusCheck.InventorySufficient);
                    flag1_AmountCheckisLegal=false;
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

        if(!flag1_AmountCheckisLegal)
        {
            return res;
        }

        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con= DriverManager.getConnection(url,user,password);

            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String oOrderDate = currentDateTime.format(formatter);                                                      //下单的时间(8位)
            String oOrderDateTime = currentDateTime.format(formatter1);    //精确到hhMMss的时间                                             //下单时间(14位)

            String sql1="SELECT GetCurrentNumber(?);";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,oOrderDate);
            ResultSet rs=prepare.executeQuery();
            long sequence=-1;
            if(rs.next())
            {
                sequence=rs.getInt(1);
            }
            StringBuilder OrderID=new StringBuilder();                          //最终的订单编号
            OrderID.append(oOrderDate);                                         //前8位，下单的日期YYYYmmDD
            OrderID.append(GenerateOrderID_9digit(sequence));                   //后9位，该日的序列号，互斥访问该项数据

            boolean OrderGeneralInfoResult=InsertOrderGeneralInfoTable(OrderID.toString(),orderItem.getuID());
            boolean OrderBasicInfoResult=InsertOrderBasicInfoTable(OrderID.toString(),oOrderDateTime);
            boolean OrdererInfoResult=InsertOrdererInfoTable(OrderID.toString(),orderItem.getoDeliveryInfo().getuContactPersonName(),orderItem.getoDeliveryInfo().getuContactPersonGender(),orderItem.getoDeliveryInfo().getuContactPersonEmail());
            boolean OrderDeliveryInfoResult=InsertOrderDeliveryInfo(OrderID.toString(),orderItem.getoDeliveryInfo().getuDeliveryAddress(),orderItem.getoDeliveryInfo().getoPostalCode(),orderItem.getoDeliveryInfo().getuContactPersonPhone(),orderItem.getoDeliveryInfo().getoDeliveryNote());
            boolean OrderProductInfoTableResult=InsertOrderProductInfoTable(OrderID.toString(),orderItem);
            res.setoOrderID(OrderID.toString());
            System.out.println("最终订单号 : "+res.getoOrderID());
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
    private String GenerateOrderID_9digit(long iSequence)
    {
        StringBuilder res=new StringBuilder(String.valueOf(iSequence));
        while(res.length()<9)
        {
            res.append("0");
        }
        return res.toString();
    }
    private boolean InsertOrderGeneralInfoTable(String OrderID,String OrdererID)
    {
        boolean res=false;
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="INSERT INTO OrderGeneralInfoTable (oOrderID,oOrdererID) VALUES (?,?);";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1, OrderID);
            prepare.setString(2, OrdererID);
            int row=prepare.executeUpdate();
            if(row>0)
            {
                res=true;
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
    private boolean InsertOrderBasicInfoTable(String OrderID,String oDate)
    {
        boolean res=false;
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="INSERT INTO OrderBasicInfoTable (oOrderID,oDate,oStatus) VALUES (?,?,?);";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1, OrderID);
            prepare.setString(2, oDate);
            OrderStatus OrStatus=new OrderStatus();
            prepare.setString(3, OrStatus.EnumToString.get(OrderStatus.OrderStatusEnum.Unpaid));
            int row=prepare.executeUpdate();
            if(row>0)
            {
                res=true;
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
    private boolean InsertOrdererInfoTable(String OrderID,String oReceiverName,String oReceieverGender,String oReceieverEmail)
    {
        boolean res=false;
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="INSERT INTO OrdererInfoTable(oOrderID,oReceiverName,oReceieverGender,oReceieverEmail)VALUES (?,?,?,?);";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1, OrderID);
            prepare.setString(2, oReceiverName);
            prepare.setString(3, oReceieverGender);
            prepare.setString(4, oReceieverEmail);
            int row=prepare.executeUpdate();
            if(row>0)
            {
                res=true;
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
    private boolean InsertOrderDeliveryInfo(String OrderID,String oDeliveryAddress,String oPostalCode,String oContactPhone,String oDeliveryNote)
    {
        boolean res=false;
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="INSERT INTO OrderDeliveryInfo(oOrderID,oDeliveryAddress,oPostalCode,oContactPhone,oDeliveryNote)VALUES(?,?,?,?,?);";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1, OrderID);
            prepare.setString(2, oDeliveryAddress);
            prepare.setString(3, oPostalCode);
            prepare.setString(4, oContactPhone);
            prepare.setString(5, oDeliveryNote);

            int row=prepare.executeUpdate();
            if(row>0)
            {
                res=true;
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
    private boolean InsertOrderProductInfoTable(String OrderID,OrderItem content)
    {
        boolean res=false;
        try                             //往OrderProductInfoTable里面填入记录
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            StringBuilder sql1=new StringBuilder("INSERT INTO OrderProductInfoTable(oOrderID,pID,oPrice,oAmount)VALUES");
            for(int i=0;i<content.pProducts.size();i++)
            {
                if(i==content.pProducts.size()-1)
                {
                    sql1.append("("+"'"+OrderID+"'"+","+"'"+content.pProducts.get(i).getpID()+"'"+","+content.pProducts.get(i).getoPrice()+","+content.pProducts.get(i).getpAmount()+");");
                }
                else
                {
                    sql1.append("("+"'"+OrderID+"'"+","+"'"+content.pProducts.get(i).getpID()+"'"+","+content.pProducts.get(i).getoPrice()+","+content.pProducts.get(i).getpAmount()+"),");
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

        //库存减少
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            for(int i=0;i<content.pProducts.size();i++)
            {
                String pID=content.pProducts.get(i).getpID();
                int pAmount=content.pProducts.get(i).getpAmount();
                String sql2="SELECT ProductInventoryDecrease(?,?);";
                PreparedStatement prepare=con.prepareStatement(sql2);
                prepare.setString(1, pID);
                prepare.setInt(2, pAmount);
                ResultSet rs=prepare.executeQuery();
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

    @CrossOrigin(origins = "*")
    @RequestMapping("/api/OrderStatus_Update")              //用户通过这个更新是否支付的状态，配送的时候也可以通过这个来更新状态
    public String OrderStatus_Update(@RequestBody OrderStatusUpdate_jsonGet orderStatusUpdate)
    {
        {
            System.out.println("************OrderStatus_Update************");
            System.out.println(orderStatusUpdate);
            System.out.println("************OrderStatus_Update************");
        }


        if(OrderStatus_UpdateResult(orderStatusUpdate))
        {
            return OrderPack2StatusCheck.UpdateAccept;
        }
        else
        {
            return OrderPack2StatusCheck.UpdateFail;
        }
    }
    private boolean OrderStatus_UpdateResult(OrderStatusUpdate_jsonGet orderStatusUpdate)
    {
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            String sql1="UPDATE OrderBasicInfoTable SET oStatus=? WHERE oOrderID=?;";
            PreparedStatement prepare=con.prepareStatement(sql1);
            prepare.setString(1,orderStatusUpdate.getoOrderID());
            prepare.setString(2,orderStatusUpdate.getNewStatus());

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

    @CrossOrigin(origins = "*")
    @RequestMapping("/api/OrderCancelled")
    public ApiResult<OrderCancelled_jsonSend>OrderCancelled(@RequestBody OrderCancelled_jsonGet orderCancelled)         //处理订单取消的接口
    {
        return ApiResult.success(OrderCancelledResult(orderCancelled));
    }
    private OrderCancelled_jsonSend OrderCancelledResult(OrderCancelled_jsonGet orderCancelled)
    {
        OrderCancelled_jsonSend res=new OrderCancelled_jsonSend();
        res.setuID(orderCancelled.getuID());
        HashMap<String,Integer> map=new HashMap<>();//订单编号与索引的对应
        for(int i=0;i<orderCancelled.oOrderIDs.size();i++)
        {
            map.put(orderCancelled.oOrderIDs.get(i).getoOrderID(),i);
        }
        for(int i=0;i<orderCancelled.oOrderIDs.size();i++)//往反馈oOrderIDs里面放入填充物
        {
            res.oOrderIDs.add(null);
        }

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try
        {
            Class.forName("com.kingbase8.Driver");
            Connection con=DriverManager.getConnection(url,user,password);

            StringBuilder sql1=new StringBuilder("SELECT oOrderID,oDate FROM OrderBasicInfoTable WHERE ");
            for(int i=0;i<orderCancelled.oOrderIDs.size();i++)
            {
                if(i==orderCancelled.oOrderIDs.size()-1)
                {
                    sql1.append("oOrderID="+orderCancelled.oOrderIDs.get(i).getoOrderID()+" AND ");
                }
                else
                {
                    sql1.append("oOrderID="+orderCancelled.oOrderIDs.get(i).getoOrderID()+" ;");
                }
            }
            ResultSet rs=con.createStatement().executeQuery(sql1.toString());
            while(rs.next())
            {
                String oID=rs.getString("oOrderID");
                String oDate=rs.getString("oDate");

                LocalDateTime orderDateTime = LocalDateTime.parse(oDate,formatter);
                long hoursDiff= ChronoUnit.HOURS.between(orderDateTime,currentDateTime);
                if(Math.abs(hoursDiff)==0)          //差距在一个小时之内，该订单可以取消
                {
                    String sql2="UPDATE OrderBasicInfoTable SET oState='Cancelled' WHERE oOrderID=?;";
                    PreparedStatement prepare=con.prepareStatement(sql2);
                    prepare.setString(1,oID);
                    int row=prepare.executeUpdate();                                    //将这个订单的订单状态更新为取消
                    if(row>0)                   //更新成功
                    {
                        //将该订单的商品数量添加到库存里面
                        String sql3="SELECT OrderProductInfoTable.pID,OrderProductInfoTable.oAmount FROM OrderProductInfoTable WHERE oOrderID=?;";
                        PreparedStatement prepare2=con.prepareStatement(sql3);
                        prepare2.setString(1,oID);
                        ResultSet rs2=prepare2.executeQuery();                          //将这个订单中订购的商品的商品数量加回到库存里面,一个订单可以订购多个商品
                        while(rs2.next())
                        {
                            String pID=rs2.getString("pID");//该商品的编号
                            int oAmount=rs2.getInt("oAmount");//该商品的订购数量

                            String sql4="SELECT ProductInventoryAscend(?,?);";
                            PreparedStatement prepare3=con.prepareStatement(sql4);
                            prepare3.setString(1,pID);
                            prepare3.setInt(2,oAmount);
                            ResultSet rs3=prepare3.executeQuery();                  //将这个订单中这个商品的订购数量重新加回库存里面
                            if(rs3.next())
                            {
                                boolean AscendResult=rs3.getBoolean(1);
                            }
                        }
                        CancelledFeedBackItem item=new CancelledFeedBackItem();
                        item.setoOrderID(oID);
                        item.setFeedBack(OrderPack2StatusCheck.CancelledAccept);
                        res.oOrderIDs.set(map.get(oID),item);
                    }
                }
                else                                //差距在一小时之外，该订单不可以取消
                {
                    CancelledFeedBackItem item=new CancelledFeedBackItem();
                    item.setoOrderID(oID);
                    item.setFeedBack(OrderPack2StatusCheck.CancelledFail);
                    res.oOrderIDs.set(map.get(oID),item);
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

}
