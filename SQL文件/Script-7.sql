CREATE DATABASE db_EBuyPlt2;

--第二版表的形式

--账号信息表(号主)
CREATE TABLE UserAccountTable
(
uID varchar(32) PRIMARY KEY,							--COMMENT'账号',
uNickName varchar(32) DEFAULT NULL,						--COMMENT'昵称',
uPassword varchar(128) NOT NULL,						--COMMENT'密码(Hash)',
uPhone varchar(11) NOT NULL,							--COMMENT'电话号码',
uEmail varchar(32) DEFAULT NULL,						--COMMENT'电子邮箱',
uGender varchar(64),									--COMMENT'性别',
uRegisterDate date NOT NULL,							--COMMENT'注册日期',
uAccountType varchar(16) NOT NULL, 						--COMMENT'账号类型'
uAccountStatus varchar(10) NOT NULL 					--COMMENT'账号状态'
);
--SELECT * FROM UserAccountTable;
--账号以及附带的信息不删除，标记为注销后，该账号的信息就不再更新了，账号密码登录失效，UserDeliveryInfoTable,UserShoppingCartTable,UserFavoritesTable上的记录删除


--用户收件地址,用户保存的收件人，收件地址，收件联系方式(与订单购买人信息表不一样)
CREATE TABLE UserDeliveryInfoTable
(
uID varchar(32), 										--COMMENT'账号',
uDeliveryAddress varchar(128) NOT NULL,					-- COMMENT'收货地址',
uContactPersonName varchar(64) NOT NULL,				-- COMMENT'收货人名称',
uContactPersonPhone varchar(11) NOT NULL,				-- COMMENT'收货人联系电话',
uContactPersonGender varchar(64)						-- COMMENT'性别',
);
ALTER TABLE UserDeliveryInfoTable ADD CONSTRAINT UserDeliveryInfoTableForeignKey FOREIGN KEY(uID) REFERENCES UserAccountTable(uID);
--若UserAccountTable中的账号被注销后，则在UserDeliveryInfoTable的收货信息记录也要被删除，所以uID仅作为外键

--用户购物车记录
CREATE TABLE UserShoppingCartTable
(
uID varchar(32),										--COMMENT'账号',
pID varchar(32),										--COMMENT'商品编号',
cAmount int16 NOT NULL									--COMMENT'加入购物车数量',
);
ALTER TABLE UserShoppingCartTable ADD CONSTRAINT UserShoppingCartTableForignKey FOREIGN KEY(uID) REFERENCES UserAccountTable(uID); 
ALTER TABLE UserShoppingCartTable ADD CONSTRAINT UUserShoppingCartTableForignKey FOREIGN KEY(pID) REFERENCES ProductTable(pID);

--用户收藏表
CREATE TABLE UserFavoritesTable
(
uID varchar(32),										--COMMENT'账号',
pID varchar(32)											--COMMENT'商品编号',
);
ALTER TABLE UserFavoritesTable ADD CONSTRAINT UserFavoritesTableForeignKey FOREIGN KEY(uID) REFERENCES UserAccountTable(uID);
ALTER TABLE UserFavoritesTable ADD CONSTRAINT UUserFavoritesTableForeignKey FOREIGN KEY(pID) REFERENCES ProductTable(pID);


--商品信息表
CREATE TABLE ProductTable
(
pID varchar(32) PRIMARY KEY,							-- COMMENT '商品编号',
pName varchar(128) NOT NULL,							-- COMMENT '商品名称',
pType varchar(32)NOT NULL,								-- COMMENT '商品类别',
pDiscount double DEFAULT 1.0,							-- COMMENT '折扣',
pPrice double DEFAULT NULL,								-- COMMENT '价格',
pProducer varchar(64) NOT NULL,							-- COMMENT '生产商',
pReleaseDate date ,										-- COMMENT '上架日期',
pInfo varchar(512) DEFAULT NULL							-- COMMENT '商品信息'
);
ALTER TABLE ProductTable ADD COLUMN pInventory int16 DEFAULT 0;--COMMENT '商品库存'
ALTER TABLE ProductTable ADD COLUMN pStatus varchar(16)DEFAULT '上架';--COMMENT '商品状态'
CREATE INDEX index_pReleaseDate_date ON ProductTable(date(pReleaseDate));
CREATE INDEX index_pType ON ProductTable(pType);
--商品状态:(上架，下架)下架之后不再更新数据但是仍然会保留在数据库中
--pReleaseDate的格式与下单时间的格式一致YYYY-MM-DD HH:mm:ss
--0000000000000000 pID长16位

SELECT * 
FROM pg_indexes 
WHERE tablename = 'producttable';  -- 只需要表名，不需要索引名

SELECT * FROM pg_indexes 
WHERE indexname = 'index_preleasedate_date';

--缩略图路径表
CREATE TABLE ProductImagesTable
(
pID varchar(32) ,										--COMMENT '商品号',
pType varchar(16)DEFAULT '展示图',						--COMMENT '图片类型',		
pImagePath varchar(64) 									--COMMENT '商品图片路径',
);
ALTER TABLE ProductImagesTable ADD CONSTRAINT ProductImagesTableForeignKey FOREIGN KEY (pID) REFERENCES ProductTable(pID);
CREATE INDEX index_pID ON ProductImagesTable(pID);
CREATE INDEX index_picType ON ProductImagesTable(pType);
--若ProductTable中的商品下架(这一版)缩略图保留,即使商品下架了收藏库里依然能看到该商品只是标记为下架
--pType图片类型:缩略图，展示图

--DROP TABLE ProductImagesTable;



--订单总体信息表
CREATE TABLE OrderGeneralInfoTable
(
oOrderID varchar(17) PRIMARY KEY ,						--COMMENT '订单编号',
oOrdererID varchar(32) NOT NULL 						--COMMENT '下单账号',
);
ALTER TABLE OrderGeneralInfoTable ADD CONSTRAINT OrderGeneralInfoTableForeignKey FOREIGN KEY (oOrdererID) REFERENCES UserAccountTable(uID);
--，订单编号为17位数字，前8位为当前日期，后9位为按订单提交顺序生成的编码，该编号能够唯一标识每一条销售记录
--oOrdererID这个是下单的账号
--oOrdererID作为外键但是不用delete on cascade级联删除,账号不真的从数据库上面删除，保存下单记录，以及下单者的信息


--订单基本信息表
CREATE TABLE OrderBasicInfoTable
(
oOrderID varchar(17) ,									--COMMENT '订单编号',
oDate date NOT NULL, 									--COMMENT '下单日期',
oStatus varchar(20) NOT NULL 							--COMMENT '订单状态'
);
ALTER TABLE OrderBasicInfoTable ADD CONSTRAINT OrderBasicInfoTableForeignKey FOREIGN KEY (OOrderID) REFERENCES OrderGeneralInfoTable(oOrderID);
--一次订单可以有多个商品，多个收货人

--订单收货人信息表
CREATE TABLE OrdererInfoTable
(
oOrderID varchar(17),									--COMMENT '订单编号',
oReceiverName varchar(128) NOT NULL,					--COMMENT '收货人',
oReceieverGender varchar(64),							--COMMENT '收货人性别',
oReceieverEmail varchar(32) DEFAULT NULL				--COMMENT '收货人邮箱',
);
ALTER TABLE OrdererInfoTable ADD CONSTRAINT OrdererInfoForeignKey FOREIGN KEY (oOrderID) REFERENCES OrderGeneralInfoTable(oOrderID);


--订单配送信息表
CREATE TABLE OrderDeliveryInfo
(
oOrderID varchar(17),									--COMMENT '订单编号',
oDeliveryAddress varchar(128) NOT NULL,					--COMMENT '收货地址',
oPostalCode varchar(16) NOT NULL,						--COMMENT '邮编号码',
oContactPhone varchar(11) NOT NULL,						--COMMENT '联系电话',
oDeliveryNote varchar(512) DEFAULT NULL 				--COMMENT '配送备注',
);
ALTER TABLE OrderDeliveryInfo ADD CONSTRAINT OrderDeliveryInfoForeignKey FOREIGN KEY (oOrderID) REFERENCES OrderGeneralInfoTable(oOrderID);


--订单产品信息表
CREATE TABLE OrderProductInfoTable
(
oOrderID varchar(17),									--COMMENT '订单编号',	
pID varchar(32) NOT NULL,								--COMMENT '商品编号',
--pName varchar(128)NOT NULL,		可以通过pID来联合查询
--pType varchar(32)NOT NULL,
--pImagePath varchar(64),
oPrice double NOT NULL,									--COMMENT '购买单价',
oAmount int16 NOT NULL									--COMMENT '购买数量',
);
ALTER TABLE OrderProductInfoTable ADD CONSTRAINT OrderProductInfoTableForeignKey FOREIGN KEY (oOrderID) REFERENCES OrderGeneralInfoTable(oOrderID);
ALTER TABLE OrderProductInfoTable ADD CONSTRAINT OOrderProductInfoTableForeignKey FOREIGN KEY (pID) REFERENCES ProductTable(pID);



--添加账号信息
INSERT INTO UserAccountTable(uID,uNickName,uPassword,uPhone,uEmail,uGender,uRegisterDate,uAccountStatus,uAccountType)VALUES
('1522788291','LQ1-1','760508cf0e664fae4e28c9d837b4840ee6a21203d3894bbe18edbcc54378bd13',
'18074948512','1522788291@qq.com','male',
'2025-11-13 10:30:00','正常','管理员'),
(
'18775332736','LQHHH','760508cf0e664fae4e28c9d837b4840ee6a21203d3894bbe18edbcc54378bd13',
'18775332736','18074948512@163.com','male',
'2025-11-13 10:59:41','正常','普通用户'),
(
'0123456','测试账号_管理员','5f6121bc06e18e209920d57d2f16b17cc82dfc2ade1d375d6951b99c65d1b89d',
'11001123443','235879hjhg@gmail.com','armor helicopter',
'2025-11-13 11:39:41','正常','管理员'),
(
'01234567','测试账号_普通用户','924592b9b103f14f833faafb67f480691f01988aa457c0061769f58cd47311bc',
'00110056776','sdfdsg324@gmail.com','armor piercing',
'2025-11-13 11:29:13','正常','普通用户'),
--测试账号封禁和注销
(
'012345678','测试账号_普通用户_封禁','36f50957f5e0b6ee3ef455674da35a86667f3314209dc1514c510fe95e840831',
'07715088003','112332435@qq.com','female',
'2025-11-17 21:37:27','封禁','普通用户'),
(
'0123456789','测试账号_普通用户_注销','84d89877f0d4041efb6bf91a16f0248f2fd573e6af05c19f96bedb9f882f7882',
'10011867832','23456464@qq.com','male',
'2025-11-15 15:12:41','注销','普通用户');

SELECT * FROM UserAccountTable;
SELECT version();


INSERT INTO ProductTable(pID,pName,pType,pDiscount,pPrice,pProducer,pReleaseDate,pInfo,pInventory,pStatus)VALUES
('0000000000000000','iPhone 13 mini 128GB','手机',1.0,5199.0,'Apple Inc.','2021-09-15 01:00:00','iPhone 13 mini 128GB(info)',350,'上架'),
('0000000000000001','iPhone 13 mini 256GB','手机',1.0,5999.0,'Apple Inc.','2021-09-15 01:00:00','iPhone 13 mini 256GB(info)',634,'上架'),
('0000000000000002','iPhone 13 mini 512GB','手机',1.0,7599.0,'Apple Inc.','2021-09-15 01:00:00','iPhone 13 mini 512GB(info)',171,'上架'),
('0000000000000003','iPhone 13 128GB','手机',1.0,5999.0,'Apple Inc.','2021-09-15 01:00:00','iPhone 13 128GB(info)',567,'上架'),
('0000000000000004','iPhone 13 256GB','手机',1.0,6799.0,'Apple Inc.','2021-09-15 01:00:00','iPhone 13 256GB(info)',642,'上架'),
('0000000000000005','iPhone 13 512GB','手机',1.0,8399.0,'Apple Inc.','2021-09-15 01:00:00','iPhone 13 512GB(info)',891,'上架'),
('0000000000000006','iPhone 13 Pro 128GB','手机',1.0,7999.0,'Apple Inc.','2021-09-15 01:00:00','iPhone 13 Pro 128GB(info)',469,'上架'),
('0000000000000007','iPhone 13 Pro 256GB','手机',1.0,8799.0,'Apple Inc.','2021-09-15 01:00:00','iPhone 13 256GB(info)',1127,'上架'),
('0000000000000008','iPhone 13 Pro 512GB','手机',1.0,10399.0,'Apple Inc.','2021-09-15 01:00:00','iPhone 13 Pro 512GB(info)',514,'上架'),
('0000000000000009','iPhone 13 Pro 1TB','手机',1.0,11999.0,'Apple Inc.','2021-09-15 01:00:00','iPhone 13 Pro 1TB(info)',878,'上架'),
('0000000000000010','iPhone 13 Pro Max 128GB','手机',1.0,8999.0,'Apple Inc.','2021-09-15 01:00:00','iPhone 13 Pro Max 128GB(info)',478,'上架'),
('0000000000000011','iPhone 13 Pro Max 256GB','手机',1.0,9799.0,'Apple Inc.','2021-09-15 01:00:00','iPhone 13 Pro Max 256GB(info)',157,'上架'),
('0000000000000012','iPhone 13 Pro Max 512GB','手机',1.0,11399.0,'Apple Inc.','2021-09-15 01:00:00','iPhone 13 Pro Max 512GB(info)',137,'上架'),
('0000000000000013','iPhone 13 Pro Max 1TB','手机',1.0,12999.0,'Apple Inc.','2021-09-15 01:00:00','iPhone 13 Pro Max 1TB(info)',1127,'上架'),

('0000000000000014','MacBook Air M4 13.6 英寸 512GB','电脑',1.0,9999.0,'Apple Inc.','2024-03-10 01:00:00','MacBook Air M4 13.6 英寸 512GB (info)',1200,'上架'),
('0000000000000015','MacBook Pro M4 14 英寸 1TB','电脑',1.0,12999.0,'Apple Inc.','2024-03-10 01:00:00','MacBook Pro M4 14 英寸 1TB (info)',1050,'上架'),
('0000000000000016','iMac M4 24 英寸 2TB','电脑',1.0,14999.0,'Apple Inc.','2024-04-05 01:00:00','iMac M4 24 英寸 2TB (info)',980,'上架'),
('0000000000000017','MacBook Pro M4 16 英寸 1TB','电脑',1.0,16999.0,'Apple Inc.','2024-03-10 01:00:00','MacBook Pro M4 16 英寸 1TB (info)',1100,'上架'),
('0000000000000018','Galaxy S23 256GB','手机',1.0,5499.0,'Samsung Electronics','2023-02-01 01:00:00','Galaxy S23 256GB (info)',950,'上架'),
('0000000000000019','Galaxy S23 Ultra 512GB','手机',1.0,8999.0,'Samsung Electronics','2023-02-01 01:00:00','Galaxy S23 Ultra 512GB (info)',880,'上架'),

('0000000000000020','TS-990S (200W)','无线电电台',1.0,64564.0,'JVCKENWOOD Corporation','2013-02-28 00:00:00','200W (SSB/CW/FSK/PSK/FM),50W (AM)(info)',5,'上架')
;

SELECT * FROM ProductTable;





INSERT INTO UserAccountTable(uID,uNickName,uPassword,uPhone,uEmail,uGender,uRegisterDate,uAccountStatus,uAccountType)VALUES
('152271111111111111111111111111111111111111111111111111111111111111111111111111111111111188291','LQ1-1','760508cf0e664fae4e28c9d837b4840ee6a21203d3894bbe18edbcc54378bd13',
'18074948512','1522788291@qq.com','male',
'2025-11-13 10:30:00','正常','管理员');


SELECT * FROM ProductTable WHERE LOWER(pID) LIKE LOWER('%0000012%') AND LOWER(pName) LIKE LOWER('%iph%') AND LOWER(pType) LIKE LOWER('%手%');
-- 同一转成小写

SELECT * FROM ProductTable WHERE LOWER(pType) LIKE LOWER('%手%');

SELECT * FROM ProductTable WHERE LOWER(pType) LIKE LOWER('%%');


SELECT * FROM ProductTable WHERE pPrice>=7599 AND pPrice<=7599; 

--SELECT * FROM ProductTable WHERE LOWER(pName) LIKE LOWER(?) OR LOWER(pType) LIKE LOWER(?) OR LOWER(pProducer) LIKE LOWER(?) OR LOWER(pInfo) LIKE LOWER(?);
SELECT * FROM ProductTable WHERE LOWER(pName) LIKE LOWER('%apple%') OR LOWER(pType) LIKE LOWER('%apple%') OR LOWER(pProducer) LIKE LOWER('%apple%') OR LOWER(pInfo) LIKE LOWER('%apple%');

--CREATE TABLE ProductImagesTable
--(
--pID varchar(32) ,										--COMMENT '商品号',
--pType varchar(16)DEFAULT '展示图',						--COMMENT '图片类型',		
--pImagePath varchar(64) 									--COMMENT '商品图片路径',
--);
INSERT INTO ProductImagesTable(pID,pType,pImagePath)VALUES 
();
