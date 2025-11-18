以我这台惠普电脑为后端服务器,后端服务器监听8081端口

惠普电脑在GalaxyS下的ip地址是192.168.66.94

API

# **登录:api/Login_Manual 或者/api/Login_RequestBody

返回字符串，

密码错误返回"Wrong Password";

账号不存在返回"No such Account"

账号处于封禁状态返回"Banned"

账号处于注销状态返回"Deleted"

账号密码正确，账号类型是普通用户返回"RegularUser",进入购物界面

账号密码正确，账号类型是商家用户返回"Merchant"，进入商家界面

账号密码正确，账号类型是管理员用户返回"Administrator",进入管理员界面



http://192.168.66.94:8081/api/Login_Manual手动请求参数:格式形如

{         http://localhost:8081/api/Login_Manual?uID=1522788291&uPassword=qh20050908)     }

账户变量名必须是uID,密码变量名必须是uPassword,密码必须先经过SHA256哈希之后再提交给后端服务器



前端发送的json文件要有uID,uPassword这两个变量名

http://192.168.66.94:8081/api/Login_RequestBody参数是json形式,通过请求体传递json格式

json文件中账号键值对名必须是uID，密码键值对名必须是uPassword密码必须先经过SHA256哈希之后再提交给后端服务器



# 注册:/api/Registration

返回字符串

插入成功返回"RegistrationSuccess"

插入失败则返回报错信息，账户已经存在返回"AccountAlreadyExist"

插入失败则返回报错信息，某一个属性太长则返回已经存在返回"ValuetooLargeForColumn"

参数也是json形式,通过请求体传递json格式.

发送的json文件中

账号键值名uID

昵称键值名uNickName

密码键值名uPassword

电话键值名uPhone

邮箱键值名uEmail

性别键值名uGender

注册日期键值名uRegisterDate

账号类型键值名uAccountType

账号状态键值名uAccountStatus

## 用户类型的选项

如果是在登录界面注册的话只能是普通用户。

商家界面注册允许是普通用户或者商家用户。

在管理员界面注册则可以选择普通用户或者商家用户或者管理员用户



# 商品搜索:/api/ProductSearch

## 返回类型

```
ArrayList<ProductSearch_jsonSend>

    private String pID;
    private String pName;
    private String pType;
    private double pDiscount;
    private double pPrice;
    private String pProducer;
    private String pReleaseDate;
    private String pInfo;

    private String pIcon_path;                                                                                          //商品图标路径
存储ProductSearch_jsonSend这个类的数组
```



返回json类型的文件，形如

{  

"code": 200,  

"message": "success",  

"data": [

"pID":"xxxx"

"pName":"xxxx"

"pType":"xxxx"

"pProducer":"xxxx"

......

pIcon_path:"xxxxxx"        //这个存放缩略图的路径,每个商品只能有一个缩略图，url路径形如http://192.168.66.94:8081/+pIcon_path

http://192.168.66.94:8081/assets/images/TS-990S_2.png

],  

"timestamp": "2025-11-16T15:30:45.123+08:00" 

}

需要传递json形式参数

前端需要具有如下变量名

```
SeachDesciption   搜索框的文本内容，

筛选栏用户勾选或输入的信息
pID				筛选栏里面的商品编号输入					
pType			筛选栏里面的商品类型勾选
pPrice_f		筛选栏里面的商品价格区间输入
pPrice_r		筛选栏里面的商品价格区间输入
pProducer		筛选栏里面的商品生产商选择，也允许用户自己输入
pReleaseDate_f	筛选栏里面的商品上架日期区间
pReleaseDate_r	筛选栏里面的商品上架日期区间
pInfo			筛选栏里面的商品描述信息输入

```

接收的数据类型需要有如下的变量名

```
pID;			商品的编号
pName;			商品名
pType;			商品类型
pDiscount;		商品的折扣
pPrice;			商品的价格
pProducer;		商品的生产商
pReleaseDate;	商品的发售日期
pInfo;			商品的描述信息
pIcon_path;     商品缩略图的资源路径
```

当用户点进去的时候再次发送请求，这个时候会返回该商品的所有展示图url路径信息



## 传递参数

```
//用户输入的关键词，根据描述模糊查询包括（商品名，商品商品类型，生产商，商品描述信息）
private String SeachDesciption;

//筛选条件，没有勾选或者填写的话默认为空
private String pID;                                     //直接通过商品ID来查找

private String pType;                                   //筛选商品类别

private double pPrice_f;                                //筛选价格区间(前),只有这个，默认筛选条件是价格大于等于这个数值
private double pPrice_r;                                //筛选价格区间(后)

private String pProducer;                               //筛选生产商

//日期筛选格式YYYY-MM-DD
private String pReleaseDate_f;                          //筛选发售时间区间(前)
private String pReleaseDate_r;                          //筛选发售时间区间(后)

private String pInfo;                                   //通过描述信息筛选
```

# 商品点击:/api/ProductClick

## 返回类型

返回json类型的文件

```
    private String pID;
    private String pName;
    private String pType;
    private double pDiscount;
    private double pPrice;
    private String pProducer;
    private String pReleaseDate;
    private String pInfo;
    private ArrayList<String>pIcon_paths;                                                                               //该商品的图片路径

```

前端接收时，数据类型里面有特别有pIconPaths: string[]形如这个的string类型的数组用来接收多个展示图的路径url





下单模块

用户将商品加入购物车，

在购物车里面购买下单

点击购买，先检查收货信息(检查收货信息---函数)，前端获取用户的制定的收货信息，然后将订单的信息，商品的商品编号，购买的商品的数量传输到后端，检查购买数量是否合法(检查订单数量是否合法---函数),然后将该收货信息表单，以及下单的商品信息作为参数传递到后方(生成订单---函数)

## 传递参数

```
private String pID;
```

# 检查配送信息:/api/OrderConfirm_DeliveryCheck

## 返回类型

```
String
```

返回String类型的数据

如果有配送信息记录，返回	'DeliveryInfoExist';此时让用户选择一个收货记录。然后将这个收货记录表和订购信息表一起发给后端服务器



如果没有配送信息记录，返回	'DeliveryInfoNotExist';此时让用户填写一个收货表单然后将该收货记录表和订购信息一起发送给后端服务器.

前端传递的参数是一个json文件的方式，这个json文件只需要有uID这个成员，这个变量名，uID是该用户的账号

## 传递参数

```
private String uID;   
```

# 检查订单的订购数量:/api/OrderConfirm_AmountCheck

## 返回数据

这个函数返回的是

```
    public ArrayList<String> pIDs;                                                                                      //各个商品的商品编号
    public ArrayList<Integer> pAmounts;                                                                                 //各个商品的订购数量
    public ArrayList<Double>pPrices;                                                                                    //各个商品的购买单价
    public ArrayList<String>pFeedback;

    public String orderStatus;                                                                                          //如果订购数量超过库存量的话，这里就是ERROR,否则就是Accept
 
```

这个类型的json文件，因为一个订单下可能会有多个商品，这些数组的数据需要一一对应

当orderStatus的内容是'Accept'时，表示检查无误，订单正常

当orderStatus的内容是'Error'时，表示检查订单有问题，有一个商品的订购数量超过了库存的数量,需要提示用户重新调整订购数量，遍历pIDs和pFeedback如果对应值是'Error'则这个商品的订购数量有误

## 需要传递的参数

```
public String uID;                                                                                                  //下单的用户的账号
public ArrayList<String> pIDs;                                                                                      //各个商品的商品编号
public ArrayList<Integer> pAmounts;                                                                                 //各个商品的订购数量
public ArrayList<Double>pPrices;                                                                                    //各个商品的购买单价
```

# 生成订单编号:/api/OrderConfirm_OrderIDGenerate

## 返回类型

```
String
```

内容是'Accept'说明订单生成成功.

内容是'Error'说明订单没有成功生成

## 传递参数

```
public class OrderWrapper
{
    public OrderAmountCheck_jsonGet OrderInfo;
    public OrderDeliveryList DeliveryInfo;
}

OrderInfo
public class OrderAmountCheck_jsonGet
{
    public String uID;                                                                                                  //下单的用户的账号
    public ArrayList<String> pIDs;                                                                                      //各个商品的商品编号
    public ArrayList<Integer> pAmounts;                                                                                 //各个商品的订购数量
    public ArrayList<Double>pPrices;       
 }
 
 DeliveryInfo
 public class OrderDeliveryList
{
    private String uID;
    private String uDeliveryAddress;
    private String uContactPersonName;              //也是收货人的名字
    private String uContactPersonPhone;             //收货人电话
    private String uContactPersonGender;            //收货人性别

    private String uContactPersonEmail;             //收货人邮箱         --NEW
    private String oPostalCode;                     //收货人邮编         --NEW
    private String oDeliveryNote;                   //送货邮编           --NEW
 }
 
```

订单生成状态默认是Unpaid状态,只有当进入付款界面确认付款后订单状态才是Paid。

# 支付成功:/api/OrderConfirm_OrderPaid

## 返回类型

```
String
```

如果内容是'Accept'表明支付成功

如果内容是'Error'表明支付失败

# 传递参数

```
private String oOrderID;			该订单的订单编号
```