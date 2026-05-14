# EBuyPlt 电商平台

EBuyPlt 是一个前后端分离的电商平台课程/练习项目，包含普通用户购物、商家商品管理、管理员后台管理、购物车、收藏、订单、评论、商品图片上传等功能。

仓库当前主要保存后端 Spring Boot 工程、前端 Vue 页面源码、Kingbase 数据库建表/初始化脚本，以及一份历史 API 手册。

## 功能概览

### 普通用户

- 注册、登录、账号信息维护
- 收货地址新增、修改、删除、查询
- 商品推荐、商品搜索、商品详情查看
- 商品评论、回复、点赞
- 收藏商品、取消收藏、查看收藏列表
- 加入购物车、移出购物车、查看购物车
- 商品下单、支付状态更新、订单取消、订单记录查询

### 商家

- 发布商品基础信息
- 上传商品缩略图和展示图
- 查询自己上架的商品
- 更新商品名称、类型、价格、折扣、库存、状态、描述等信息
- 查询售出商品记录

### 管理员

- 查看用户、收货地址、购物车、收藏、商家商品、商品、商品图片、点击量、订单等数据表
- 支持分页查询
- 支持用户、商品、订单等条件搜索
- 修改用户账号信息、收货地址、商品信息、订单基础信息、订单商品信息
- 查看某个用户的收货信息、订单信息
- 查看某个订单的商品明细
- 查看某个商品的完整信息

## 技术栈

### 后端

- Java 21
- Spring Boot 3.5.7
- Spring Web
- Maven Wrapper
- MyBatis 3.5.19
- KingbaseES / Kingbase8 JDBC Driver
- Lombok

### 前端

- Vue 3 单文件组件
- Vue Router
- Axios
- Element Plus 风格组件

注意：当前仓库中的 `前端/` 目录只保存了页面源码和路由文件，没有完整的 `package.json`、`vite.config.js`、`main.js` 等 Vue 工程脚手架文件。若要运行前端，需要把这些页面迁移到一个 Vue 3 工程中，或补齐工程脚手架。

### 数据库

- KingbaseES，连接协议为 `jdbc:kingbase8`
- 默认数据库名：`db_ebuyplt2`
- 默认连接地址：`localhost:54321`
- 默认用户名：`system`

## 目录结构

```text
.
├── README.md
├── SQL文件/
│   ├── Script-5_version1.sql
│   ├── Script-7.sql
│   └── Script-7_version2.sql
├── 前端/
│   ├── CZY/
│   └── LQH/
│       ├── App.vue
│       ├── index.js
│       └── EBuyPlt/
│           ├── LoginView.vue
│           ├── ShoppingnbView.vue
│           ├── ProductDetailView.vue
│           ├── OrderListView.vue
│           ├── MerchantView.vue
│           ├── AdminView.vue
│           ├── UserProfileView.vue
│           ├── AdminUserDetailView.vue
│           ├── AdminProductDetailView.vue
│           └── AdminOrderDetailView.vue
├── 后端/
│   ├── pom.xml
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── API手册/API手册.md
│   └── src/
│       ├── main/java/com/lqh/ebuyplt_1001p/
│       ├── main/resources/
│       └── test/java/com/lqh/ebuyplt_1001p/
└── 测试意见/
```

## 后端配置

后端配置文件位于：

```text
后端/src/main/resources/application.properties
```

当前主要配置：

```properties
spring.application.name=EBuyPlt_1001p
server.port=8082
server.address=0.0.0.0
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=30MB
```

数据库连接在多处 Java 代码和 MyBatis 配置中硬编码：

```text
jdbc:kingbase8://localhost:54321/db_ebuyplt2
user=system
password=qh20050908
```

相关文件包括：

- `后端/src/main/java/com/lqh/ebuyplt_1001p/Controller/DBTools/DBUtil.java`
- `后端/src/main/resources/mybatis-config.xml`
- 多个 Controller 内部的静态数据库连接字段

如果本机数据库地址、端口、用户名或密码不同，需要同步修改这些位置。

## 数据库初始化

SQL 脚本位于 `SQL文件/`。

建议优先参考：

```text
SQL文件/Script-7.sql
```

该脚本包含相对完整的表、函数、索引和测试数据，例如：

- `UserAccountTable`
- `UserDeliveryInfoTable`
- `UserShoppingCartTable`
- `UserFavoritesTable`
- `MerchantsProductTable`
- `ProductTable`
- `ProductImagesTable`
- `ProductClicksInfoTable`
- `CommentOnProductTable`
- `CommentLikesTable`
- `OrderGeneralInfoTable`
- `OrderSequenceTable`
- `OrderBasicInfoTable`
- `OrdererInfoTable`
- `OrderDeliveryInfo`
- `OrderProductInfoTable`
- `MerchantManagementTable`

初始化步骤：

1. 安装并启动 KingbaseES。
2. 确认数据库监听在 `localhost:54321`。
3. 使用 `system` 用户连接数据库。
4. 执行 `SQL文件/Script-7.sql`。
5. 确认已创建 `db_ebuyplt2` 以及相关业务表、函数和初始数据。

部分 SQL 脚本之间存在版本差异。`Script-7_version2.sql` 开头建表较简化，缺少部分后端当前代码会访问的字段或表，例如 `pImgType`、`uDIndex`、`ProductClicksInfoTable`、评论相关表等。因此更建议优先使用 `Script-7.sql`。

## 启动后端

进入后端目录：

```bash
cd 后端
```

运行测试：

```bash
./mvnw test
```

启动服务：

```bash
./mvnw spring-boot:run
```

启动后默认监听：

```text
http://localhost:8082
```

健康检查/欢迎接口：

```text
GET 或 POST http://localhost:8082/api/Welcome
```

预期返回：

```text
Welcome to EBuyPlt
```

## 静态资源与图片上传

商品图片上传接口会把文件保存到后端运行目录下的：

```text
后端/assets/images/
```

后端通过 `WebConfig` 将静态资源映射为：

```text
/assets/**
```

例如数据库中保存：

```text
/assets/images/example.png
```

访问地址为：

```text
http://localhost:8082/assets/images/example.png
```

## 前端运行说明

当前 `前端/LQH/` 保存的是 Vue 页面源码，不是完整可直接启动的前端项目。要运行前端，可以新建一个 Vue 3 项目，然后迁移文件。

示例步骤：

```bash
npm create vite@latest ebuyplt-web -- --template vue
cd ebuyplt-web
npm install
npm install vue-router axios element-plus @element-plus/icons-vue
```

迁移文件建议：

```text
前端/LQH/App.vue              -> src/App.vue
前端/LQH/index.js             -> src/router/index.js
前端/LQH/EBuyPlt/*.vue        -> src/views/EBuyPlt/*.vue
```

同时需要在 `src/main.js` 中注册 Vue Router、Element Plus 和图标。示例：

```js
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(router)
app.use(ElementPlus)
app.mount('#app')
```

然后启动前端：

```bash
npm run dev
```

### 前端接口地址

前端页面中目前硬编码了后端地址：

```text
http://192.168.126.94:8082
```

如果后端运行在本机，请把各页面中的 `BASE_URL` / `API_BASE_URL` 改成：

```text
http://localhost:8082
```

可搜索替换：

```bash
rg "192.168.126.94" 前端/LQH
```

## 页面路由

路由文件：

```text
前端/LQH/index.js
```

当前路由：

| 路径 | 页面 | 说明 |
| --- | --- | --- |
| `/LoginView` | `LoginView.vue` | 登录/注册 |
| `/ShoppingnbView` | `ShoppingnbView.vue` | 商品浏览、搜索、购物车、收藏、下单 |
| `/ProductDetailView/:pID` | `ProductDetailView.vue` | 商品详情、评论、购买 |
| `/OrderListView` | `OrderListView.vue` | 用户订单列表 |
| `/MerchantView` | `MerchantView.vue` | 商家商品管理 |
| `/AdminView` | `AdminView.vue` | 管理员后台 |
| `/UserProfileView` | `UserProfileView.vue` | 用户资料与收货信息 |
| `/AdminUserDetailView/:uID` | `AdminUserDetailView.vue` | 管理员查看用户详情 |
| `/AdminProductDetailView/:pID` | `AdminProductDetailView.vue` | 管理员查看商品详情 |
| `/AdminOrderDetailView/:oOrderID` | `AdminOrderDetailView.vue` | 管理员查看订单详情 |
| `/` | redirect | 重定向到 `/LoginView` |

## API 概览

详细历史文档见：

```text
后端/API手册/API手册.md
```

以下为当前 Controller 中能看到的接口概览。

### 基础账号与个人信息

| 接口 | 说明 |
| --- | --- |
| `/api/Welcome` | 欢迎接口 |
| `/api/Login_Manual` | 使用 URL 参数登录 |
| `/api/Login_RequestBody` | 使用 JSON 请求体登录 |
| `/api/Registration` | 用户注册 |
| `/api/GetUserAccountInfo` | 获取用户账号信息 |
| `/api/SetUserAccountInfo` | 修改用户账号信息 |
| `/api/GetUserDeliveryInfo` | 获取用户收货地址 |
| `/api/SetUserDeliveryInfo` | 修改用户收货地址 |
| `/api/AddUserDeliveryInfo` | 新增用户收货地址 |
| `/api/DeleteUserDeliveryInfo` | 删除用户收货地址 |

登录返回值包括：

```text
RegularUser
Merchant
Administrator
Wrong Password
No such Account
Banned
Deleted
```

### 商品浏览与评论

| 接口 | 说明 |
| --- | --- |
| `/api/ProductRecommend` | 商品推荐，按点击量返回商品 |
| `/api/ProductSearch` | 商品搜索与筛选 |
| `/api/ProductClick` | 商品详情，同时增加点击量 |
| `/api/GetAllProductType` | 获取所有商品类型 |
| `/api/GetAllProductComment` | 获取某商品全部评论 |
| `/api/SendProductComment` | 发送商品评论 |
| `/api/GiveLikesProductComment` | 给评论点赞 |

### 购物车与收藏

| 接口 | 说明 |
| --- | --- |
| `/api/ShoppingCartAdd` | 添加购物车 |
| `/api/ShoppingCartRemove` | 移出购物车 |
| `/api/ShoppingCartMultipleRemove` | 批量移出购物车 |
| `/api/ShoppingCartRecords` | 查询购物车记录 |
| `/api/FavouriteAdd` | 添加收藏 |
| `/api/FavouriteRemove` | 取消收藏 |
| `/api/FavouriteMultipleRemove` | 批量取消收藏 |
| `/api/FavouriteRecords` | 查询收藏记录 |

### 订单

| 接口 | 说明 |
| --- | --- |
| `/api/OrderConfirm_DeliveryCheck` | 检查用户是否已有收货信息 |
| `/api/OrderConfirm_NewDeliveryRecord` | 下单时新增收货地址 |
| `/api/OrderConfirm_OrderGenerate` | 生成订单 |
| `/api/OrderStatus_Update` | 更新订单状态 |
| `/api/OrderSingularProductDeliveryStatus_Update` | 更新订单中单个商品配送状态 |
| `/api/OrderCancelled` | 取消订单 |
| `/api/GetOrderRecords` | 查询订单记录 |

### 商家

| 接口 | 说明 |
| --- | --- |
| `/api/ProductOnSale` | 商家发布商品文本信息 |
| `/api/ProductImagesUpload` | 商家上传商品图片 |
| `/api/ProductAllInfo` | 查询该商家的全部商品 |
| `/api/ProductInfoUpdate` | 更新商品信息 |
| `/api/ProductSaledInfo` | 查询售出商品记录 |

### 管理员

| 接口 | 说明 |
| --- | --- |
| `/api/AdminUserAccountTable` | 查询所有用户账号 |
| `/api/AdminUserAccountTablePagination` | 分页查询用户账号 |
| `/api/AdminUserDeliveryInfoTable` | 查询所有用户收货信息 |
| `/api/AdminUserDeliveryInfoTablePagination` | 分页查询用户收货信息 |
| `/api/AdminUserShoppingCartTable` | 查询所有购物车记录 |
| `/api/AdminUserShoppingCartTablePagination` | 分页查询购物车记录 |
| `/api/AdminUserFavoritesTable` | 查询所有收藏记录 |
| `/api/AdminUserFavoritesTablePagination` | 分页查询收藏记录 |
| `/api/AdminMerchantsProductTable` | 查询商家商品关系 |
| `/api/AdminMerchantsProductTablePagination` | 分页查询商家商品关系 |
| `/api/AdminProductTable` | 查询商品表 |
| `/api/AdminProductTablePagination` | 分页查询商品表 |
| `/api/AdminProductImagesTable` | 查询商品图片表 |
| `/api/AdminProductImagesTablePagination` | 分页查询商品图片表 |
| `/api/AdminProductClicksInfoTable` | 查询商品点击量 |
| `/api/AdminProductClicksInfoTablePagination` | 分页查询商品点击量 |
| `/api/AdminOrderFullInfoTable` | 查询订单完整信息 |
| `/api/AdminOrderFullInfoTablePagination` | 分页查询订单完整信息 |
| `/api/AdminOrderProductInfoTable` | 查询订单商品信息 |
| `/api/AdminOrderProductInfoTablePagination` | 分页查询订单商品信息 |
| `/api/AdminUserAccountTableSearch` | 搜索用户账号 |
| `/api/AdminProductTableSearch` | 搜索商品 |
| `/api/AdminOrderInfoSearch` | 搜索订单 |
| `/api/AdminUserAccountTableUpdate` | 更新用户账号 |
| `/api/AdminUserDeliveryInfoTableUpdate` | 更新用户收货地址 |
| `/api/AdminUserDeliveryInfoTableAdd` | 新增用户收货地址 |
| `/api/AdminUserDeliveryInfoTableDelete` | 删除用户收货地址 |
| `/api/AdminProductTableUpdate` | 更新商品信息 |
| `/api/AdminOrderBasicInfoTableUpdate` | 更新订单基础信息 |
| `/api/AdminOrderProductInfoTableUpdate` | 更新订单商品信息 |
| `/api/AdminGetUserDeliveryInfo` | 查询指定用户收货信息 |
| `/api/AdminGetUserOrderInfo` | 查询指定用户订单 |
| `/api/AdminGetOrderSpecificInfo` | 查询指定订单商品明细 |
| `/api/AdminGetSpecificProductInfo` | 查询指定商品详情 |

## 请求与响应约定

多数接口使用 `POST` + JSON 请求体。代码中大量使用 `@RequestMapping`，没有严格区分 GET/POST，但前端页面主要通过 `axios.post` 调用。

部分接口返回统一包装结构：

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": "2025-11-16T15:30:45.123+08:00"
}
```

部分登录/注册/上传类接口直接返回字符串，例如：

```text
Registration Success
Registration Fail
Account Exist
OnSaleSuccess
OnSaleFail
```

密码在前端提交前需要先做 SHA-256 哈希，后端按哈希值比对。

## 常用测试账号

SQL 脚本中包含一些初始账号。以下账号来自初始化脚本，密码字段为 SHA-256 后的哈希值，原始密码需要根据当时脚本/前端测试记录确认。

| 账号 | 昵称 | 类型 | 状态 |
| --- | --- | --- | --- |
| `1522788291` | `LQ1-1` | 管理员 | 正常 |
| `18775332736` | `LQHHH` | 普通用户 | 正常 |
| `0123456` | `测试账号_管理员` | 管理员 | 正常 |
| `01234567` | `测试账号_普通用户` | 普通用户 | 正常 |
| `012345678` | `测试账号_普通用户_封禁` | 普通用户 | 封禁 |
| `0123456789` | `测试账号_普通用户_注销` | 普通用户 | 注销 |

## 开发注意事项

- 当前后端数据库连接信息硬编码在代码和 MyBatis XML 中，不适合直接用于生产环境。
- 当前仓库包含数据库密码和测试账号数据，公开仓库前应改为环境变量或本地配置文件，并清理敏感信息。
- 前端接口地址硬编码为局域网 IP，换机器运行时需要修改。
- 前端目录缺少完整工程脚手架，不能直接在仓库根目录运行 `npm install`。
- 后端端口当前是 `8082`，历史 API 手册中有些地方写的是 `8081`，以 `application.properties` 和前端当前代码为准。
- 商品图片上传会写入后端运行目录的 `assets/images/`，重新部署或清理目录时要注意图片文件持久化。
- SQL 脚本存在版本差异，后端当前代码依赖的表字段以 `Script-7.sql` 更完整。
- Controller 中多处直接使用 JDBC，部分分页/评论功能使用 MyBatis。

## 后续可改进方向

- 将数据库连接改为 `application.properties` 配置并通过环境变量覆盖。
- 把前端整理为完整 Vite + Vue 3 工程。
- 抽取统一的 Axios API 层，避免页面中重复硬编码 `BASE_URL`。
- 将所有接口方法明确为 `@GetMapping` / `@PostMapping`。
- 为登录鉴权增加 Token / Session 机制。
- 为管理员、商家、普通用户接口增加权限校验。
- 增加后端 Service/Repository 分层，减少 Controller 中直接写 SQL。
- 补充自动化测试和接口测试集合。
