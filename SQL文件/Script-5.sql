CREATE DATABASE db_EBuyPlt;

--SELECT VERSION();
--SELECT * FROM UserAccountTable;

--CREATE TABLE test
--(
--testid varchar(32) PRIMARY
--);
--SELECT * FROM test;

-- 第一版表的各种样式


--账号信息表(号主)
CREATE TABLE UserAccountTable
(
uID varchar(32) PRIMARY KEY,							--COMMENT '账号',
uNickName varchar(32) DEFAULT NULL,						--COMMENT'昵称',
uPassword varchar(32) NOT NULL,							--COMMENT'密码(Hash)',
uPhone varchar(11) NOT NULL,							--COMMENT'电话号码',
uEmail varchar(32) DEFAULT NULL,						--COMMENT'电子邮箱',
uGender varchar(64),									--COMMENT'性别',
uRegisterDate date NOT NULL,							--COMMENT'注册日期',
uAccountStatus varchar(10) NOT NULL 					--COMMENT'账号状态'
);
--uAccountStatus用户的账号状态:(正常,封号,注销);当状态为注销的时候账号密码登录失效
--用户注销账号后，从功能和权益上，账号确实 “被注销” 了（无法使用、个人信息被清除或脱敏）；但从技术细节上，账号的底层记录可能未被 “物理删除”，而是以 “标记失效 + 脱敏” 的形式留存，目的是满足法律合规、业务追溯和数据安全需求。

--用户收件地址,用户保存的收件人，收件地址，收件联系方式(与订单购买人信息表不一样)
CREATE TABLE UserDeliveryInfoTable
(
uID varchar(32), 										--COMMENT'账号',
uDeliveryAddress varchar(128) NOT NULL,					-- COMMENT'收货地址',
uContactPersonName varchar(64) NOT NULL,				-- COMMENT'收货人名称',
uContactPersonPhone varchar(11) NOT NULL,				-- COMMENT'收货人联系电话',
uContactPersonGender varchar(64)						-- COMMENT'性别',
);
ALTER TABLE UserDeliveryInfoTable ADD CONSTRAINT UserDeliveryInfoTableForeignKey FOREIGN KEY(uID) REFERENCES UserAccountTable(uID) ON DELETE CASCADE;
--若UserAccountTable中的账号被删除，则在UserDeliveryInfoTable的收货信息记录也要被删除

--用户购物车记录
CREATE TABLE UserShoppingCartTable
(
uID varchar(32),										--COMMENT'账号',
pID varchar(32),										--COMMENT'商品编号',
cAmount int16 NOT NULL									--COMMENT'加入购物车数量',
);
ALTER TABLE UserShoppingCartTable ADD CONSTRAINT UserShoppingCartTableForignKey FOREIGN KEY(uID) REFERENCES UserAccountTable(uID) ON DELETE CASCADE; 
ALTER TABLE UserShoppingCartTable ADD CONSTRAINT UUserShoppingCartTableForignKey FOREIGN KEY(pID) REFERENCES ProductTable(pID);

--用户收藏表
CREATE TABLE UserFavoritesTable
(
uID varchar(32),										--COMMENT'账号',
pID varchar(32)											--COMMENT'商品编号',
);
ALTER TABLE UserFavoritesTable ADD CONSTRAINT UserFavoritesTableForeignKey FOREIGN KEY(uID) REFERENCES UserAccountTable(uID) ON DELETE CASCADE;
ALTER TABLE UserFavoritesTable ADD CONSTRAINT UUserFavoritesTableForeignKey FOREIGN KEY(pID) REFERENCES ProductTable(pID);

--商品信息表
CREATE TABLE ProductTable
(
pID varchar(32) PRIMARY KEY,							-- COMMENT '商品编号',
pName varchar(128) NOT NULL,							-- COMMENT '商品名称',
pType varchar(32)NOT NULL,								-- COMMENT '商品类别',
pDiscount double DEFAULT 1.0,							-- COMMENT '折扣',
pPrice double DEFAULT NULL,								--COMMENT '价格',
pProducer varchar(64) NOT NULL,							-- COMMENT '生产商',
pReleaseDate date ,										--COMMENT '上架日期',
pInfo varchar(512) DEFAULT NULL							--COMMENT '商品信息'
);
ALTER TABLE ProductTable ADD COLUMN pInventory int16 DEFAULT 0;--COMMENT '商品库存'
ALTER TABLE ProductTable ADD COLUMN pStatus varchar(16)DEFAULT '上架';--COMMENT '商品状态'
--商品状态:(上架，下架)下架之后不再更新数据但是仍然会保留在数据库中

--缩略图路径表
CREATE TABLE ProductImagesTable
(
pID varchar(32) ,										--COMMENT '商品号',
pType varchar(16)DEFAULT '展示图',						--COMMENT '图片类型',		
pImagePath varchar(64) 									--COMMENT '商品图片路径',
);
ALTER TABLE ProductImagesTable ADD CONSTRAINT ProductImagesTableForeignKey FOREIGN KEY (pID) REFERENCES ProductTable(pID) ON DELETE CASCADE;
--若ProductTable中的商品被删除，则在ProductImagesTable的图片路径记录也要被删除
--pType图片类型:缩略图，展示图


--订单总体信息表
CREATE TABLE OrderGeneralInfoTable
(
oOrderID varchar(17) PRIMARY KEY ,						--COMMENT '订单编号',
oOrdererID varchar(32) NOT NULL 						--COMMENT '下单账号',
);
ALTER TABLE OrderGeneralInfoTable ADD CONSTRAINT OrderGeneralInfoTableForeignKey FOREIGN KEY (oOrdererID) REFERENCES UserAccountTable(uID);
--，订单编号为17位数字，前8位为当前日期，后9位为按订单提交顺序生成的编码，该编号能够唯一标识每一条销售记录
--oOrdererID这个是下单的账号

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





