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
ALTER TABLE UserAccountTable ADD COLUMN tLastUpdateTime TIMESTAMPTZ(1) DEFAULT CURRENT_TIMESTAMP(1);
SELECT * FROM UserAccountTable;
SELECT * FROM UserAccountTable WHERE uNickName LIKE '%%';
UPDATE UserAccountTable SET uPassword='932f3c1b56257ce8539ac269d7aab42550dacf8818d075f0bdf1990562aae3ef' WHERE uID='1522788291@163.com';


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
ALTER TABLE UserDeliveryInfoTable ADD COLUMN oReceieverEmail varchar(32) NOT NULL;
ALTER TABLE UserDeliveryInfoTable ADD COLUMN oPostalCode varchar(16) NOT NULL ;
ALTER TABLE UserDeliveryInfoTable ADD COLUMN oDeliveryNote varchar(512);
--若UserAccountTable中的账号被注销后，则在UserDeliveryInfoTable的收货信息记录也要被删除，所以uID仅作为外键
ALTER TABLE UserDeliveryInfoTable ADD COLUMN uDIndex int16 default 0;
ALTER TABLE UserDeliveryInfoTable ADD COLUMN tLastUpdateTime TIMESTAMPTZ(1) DEFAULT CURRENT_TIMESTAMP(1);
ALTER TABLE UserDeliveryInfoTable ALTER COLUMN uDIndex TYPE int2 USING uDIndex::int2;
ALTER TABLE UserDeliveryInfoTable ADD PRIMARY KEY (uID, uDIndex);

SELECT * FROM UserDeliveryInfoTable;


CREATE OR REPLACE FUNCTION UserDeliveryInfoTableadd(iuID UserDeliveryInfoTable.uID%TYPE, iuDeliveryAddress UserDeliveryInfoTable.uDeliveryAddress%TYPE,
iuContactPersonName UserDeliveryInfoTable.uContactPersonName%TYPE, iuContactPersonPhone UserDeliveryInfoTable.uContactPersonPhone%TYPE,
iuContactPersonGender UserDeliveryInfoTable.uContactPersonGender%TYPE, ioReceieverEmail UserDeliveryInfoTable.oReceieverEmail%TYPE,
ioPostalCode UserDeliveryInfoTable.oPostalCode%TYPE, ioDeliveryNote UserDeliveryInfoTable.oDeliveryNote%TYPE)
RETURN int16 AS
DECLARE
newDIndex int16;
BEGIN
	PERFORM 1 FROM UserDeliveryInfoTable WHERE uID = iuID FOR UPDATE;	--对该用户的所有收货记录进行加锁
	SELECT COALESCE(max(uDIndex),0)+1 INTO newDIndex FROM UserDeliveryInfoTable WHERE uID=iuID;
	INSERT INTO UserDeliveryInfoTable(uID,uDeliveryAddress,uContactPersonName,uContactPersonPhone,
	uContactPersonGender,oReceieverEmail,oPostalCode,
	oDeliveryNote,uDIndex)VALUES(iuID, iuDeliveryAddress, iuContactPersonName,
	iuContactPersonPhone, iuContactPersonGender, ioReceieverEmail,
	ioPostalCode, ioDeliveryNote, newDIndex);
	RETURN newDIndex;
END;

SELECT * FROM UserDeliveryInfoTable;
DELETE FROM UserDeliveryInfoTable WHERE uID='18775332736' AND uDIndex=4;


--用户购物车记录
CREATE TABLE UserShoppingCartTable
(
uID varchar(32),										--COMMENT'账号',
pID varchar(32),										--COMMENT'商品编号',
cAmount int16 NOT NULL									--COMMENT'加入购物车数量',
);
ALTER TABLE UserShoppingCartTable ADD CONSTRAINT UserShoppingCartTableForignKey FOREIGN KEY(uID) REFERENCES UserAccountTable(uID); 
ALTER TABLE UserShoppingCartTable ADD CONSTRAINT UUserShoppingCartTableForignKey FOREIGN KEY(pID) REFERENCES ProductTable(pID);
ALTER TABLE UserShoppingCartTable ADD PRIMARY KEY (uID,pID);
CREATE INDEX index_uID ON UserShoppingCartTable(uID);

SELECT * FROM UserShoppingCartTable;

DELETE FROM UserShoppingCartTable WHERE uID IN ;


--用户收藏表
CREATE TABLE UserFavoritesTable
(
uID varchar(32),										--COMMENT'账号',
pID varchar(32)											--COMMENT'商品编号',
);
ALTER TABLE UserFavoritesTable ADD CONSTRAINT UserFavoritesTableForeignKey FOREIGN KEY(uID) REFERENCES UserAccountTable(uID);
ALTER TABLE UserFavoritesTable ADD CONSTRAINT UUserFavoritesTableForeignKey FOREIGN KEY(pID) REFERENCES ProductTable(pID);
ALTER TABLE UserFavoritesTable ADD PRIMARY KEY (uID,pID);
CREATE INDEX index_uID_UFiT ON UserFavoritesTable(uID);

CREATE TABLE MerchantsProductTable
(
uID varchar(32),										--COMMENT'商户账号'
pID varchar(32)											--COMMENT'该商户上架的商品'
);
ALTER TABLE MerchantsProductTable ADD CONSTRAINT MerchantsProductTableForeignKey FOREIGN KEY(uID) REFERENCES UserAccountTable(uID);
CREATE INDEX index_uID_MerchantsProductTable ON MerchantsProductTable(uID);
CREATE INDEX index_pID_MerchantsProductTable ON MerchantsProductTable(pID);
ALTER TABLE MerchantsProductTable ADD PRIMARY KEY (uID,pID);

SELECT * FROM MerchantsProductTable;

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
ALTER TABLE ProductTable ALTER COLUMN pName TYPE varchar(1024);
ALTER TABLE ProductTable ALTER COLUMN pInfo TYPE varchar(4096);
ALTER TABLE ProductTable ADD COLUMN tLastUpdateTime TIMESTAMPTZ(1) DEFAULT CURRENT_TIMESTAMP(1);
--商品状态:(上架,缺货,下架)下架之后不再更新数据但是仍然会保留在数据库中
--pReleaseDate的格式与下单时间的格式一致YYYY-MM-DD HH:mm:ss
--0000000000000000 pID长16位

SELECT * FROM ProductTable;


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
ALTER TABLE ProductImagesTable RENAME COLUMN pType TO pImgType;
CREATE INDEX index_pID ON ProductImagesTable(pID);
CREATE INDEX index_picType ON ProductImagesTable(pType);
ALTER TABLE ProductImagesTable ALTER COLUMN pImagePath TYPE varchar(1024);
--若ProductTable中的商品下架(这一版)缩略图保留,即使商品下架了收藏库里依然能看到该商品只是标记为下架
--pType图片类型:缩略图，展示图
--static目录是默认映射的根路径,images文件夹放在static文件夹下
--缩略图只能有一个
--DROP TABLE ProductImagesTable;

--统计商品的点击次数
CREATE TABLE ProductClicksInfoTable
(
pID varchar(32),										--COMMENT '商品号',
pClicksAmount int16 DEFAULT 0							--COMMENT '商品的点击次数',
);
ALTER TABLE ProductClicksInfoTable ADD CONSTRAINT ProductClicksInfoTableForeignKey FOREIGN KEY (pID) REFERENCES ProductTable(pID);
ALTER TABLE ProductClicksInfoTable ADD CONSTRAINT uk_pid UNIQUE (pID);
ALTER TABLE ProductClicksInfoTable ALTER COLUMN pClicksAmount TYPE bigint;
CREATE INDEX index_producet_clicks_amount ON ProductClicksInfoTable(pClicksAmount DESC);

SELECT * FROM ProductClicksInfoTable;

CREATE TABLE CommentOnProductTable
(
cID varchar(46) PRIMARY KEY, 							--COMMENT'评论编号'
uID varchar(32) NOT NULL, 								--COMMENT'评论员'
pID varchar(32) NOT NULL,                               --COMMENT'评论的商品'
cDate date NOT NULL,									--COMMENT'评论时间'
rReplyID varchar(46) DEFAULT NULL									--COMMENT'回复那一条评论'
);
ALTER TABLE CommentOnProductTable ADD COLUMN cContent varchar(4096);	--COMMENT'评论内容'
ALTER TABLE CommentOnProductTable ADD CONSTRAINT CommentOnProductTableForeignKeyuID FOREIGN KEY (uID) REFERENCES UserAccountTable(uID);
ALTER TABLE CommentOnProductTable ADD CONSTRAINT CommentOnProductTableForeignKeypID FOREIGN KEY	(pID) REFERENCES ProductTable(pID);

CREATE OR REPLACE FUNCTION CommentOnProductTableAdd(iuID UserAccountTable.uID%TYPE, ipID ProductTable.pID%TYPE, 
irReplyID CommentOnProductTable.rReplyID%TYPE, icContent CommentOnProductTable.cContent%TYPE)
RETURNS void AS
DECLARE 
	currentTime varchar(14);
BEGIN
	SELECT to_char(now(), 'YYYYMMDDHH24MISS') INTO currentTime;
	INSERT INTO CommentOnProductTable(cID, uID, pID, cDate, rReplyID, cContent)VALUES (currentTime||iuID, iuID, ipID, CURRENT_DATE, irReplyID, icContent);
	INSERT INTO CommentLikesTable(cID)VALUES (currentTime||iuID);
END;


SELECT CommentOnProductTableAdd('18775332736','0000000000000013',NULL,'Test Comment2');
SELECT * FROM CommentOnProductTable;
SELECT * FROM CommentLikesTable;

CREATE TABLE CommentLikesTable
(
cID varchar(46),										--COMMENT'评论编号'	
cLikes int16 DEFAULT 0									--COMMENT'点赞数'
);
ALTER TABLE CommentLikesTable ADD CONSTRAINT CommentLikesTableForeignKeycID FOREIGN KEY (cID) REFERENCES CommentOnProductTable(cID);
ALTER TABLE CommentLikesTable ADD PRIMARY KEY (cID);

CREATE OR REPLACE FUNCTION CommentLiksIncrease(icID CommentLikesTable.cID%TYPE)
RETURNS void AS 
BEGIN 
	INSERT INTO CommentLikesTable(cID,cLikes)VALUES (icID,1) ON CONFLICT (cID)DO UPDATE SET cLikes=CommentLikesTable.cLikes+1;
END;

	
	
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

SELECT * FROM OrderGeneralInfoTable;
DELETE FROM OrderGeneralInfoTable WHERE oOrdererID='18775332736';

--订单序列表以(8位YYYYmmDD)日期为主键
--存储当日的下单序列
CREATE TABLE OrderSequenceTable
(
	UniDate varchar(8) PRIMARY KEY, 				 	--COMMENT '前8位日期',		
	CurrentNumber bigint NOT NULL DEFAULT 0				--COMMENT '当日下单次序',
);
SELECT * FROM OrderSequenceTable FOR UPDATE;			--FOR UPDATE 排他锁，防止两个线程读到同一个旧的数值
INSERT INTO OrderSequenceTable(UniDate,CurrentNumber) VALUES ('20251118',0);
INSERT INTO OrderSequenceTable(UniDate,CurrentNumber) VALUES ('20251118',0) ON CONFLICT (UniDate)DO UPDATE SET CurrentNumber=OrderSequenceTable.CurrentNumber+1;	--存在冲突就会自动更新CurrentNumber+1
INSERT INTO OrderSequenceTable(UniDate,CurrentNumber) VALUES ('20251117',0) ON CONFLICT (UniDate)DO UPDATE SET CurrentNumber=OrderSequenceTable.CurrentNumber+1;
--先查询 + 加锁 + 更新
BEGIN;
SELECT * FROM OrderSequenceTable WHERE UniDate='20251119' FOR UPDATE;
INSERT INTO OrderSequenceTable(UniDate,CurrentNumber) VALUES ('20251119',0) ON CONFLICT (UniDate)DO UPDATE SET CurrentNumber=OrderSequenceTable.CurrentNumber+1;
COMMIT;

--定义函数获取当日的订单序列
CREATE OR REPLACE FUNCTION GetCurrentNumber(UniDatePeri varchar(8))
RETURN bigint AS DECLARE res bigint;
BEGIN
	INSERT INTO OrderSequenceTable(UniDate,CurrentNumber) VALUES (UniDatePeri,0) ON CONFLICT (UniDate)DO UPDATE SET CurrentNumber=OrderSequenceTable.CurrentNumber+1;
	SELECT CurrentNumber INTO res FROM OrderSequenceTable WHERE UniDate=UniDatePeri FOR UPDATE;
	RETURN res;
END;
--GetCurrentNumber函数检测
SELECT GetCurrentNumber('20251119');

--订单基本信息表
CREATE TABLE OrderBasicInfoTable
(
oOrderID varchar(17) ,									--COMMENT '订单编号',
oDate date NOT NULL, 									--COMMENT '下单日期',
oStatus varchar(20) NOT NULL 							--COMMENT '订单状态'
);
ALTER TABLE OrderBasicInfoTable ADD CONSTRAINT OrderBasicInfoTableForeignKey FOREIGN KEY (OOrderID) REFERENCES OrderGeneralInfoTable(oOrderID);
ALTER TABLE OrderBasicInfoTable ADD COLUMN tLastUpdateTime TIMESTAMPTZ(1) DEFAULT CURRENT_TIMESTAMP(1);
--一次订单可以有多个商品，多个收货人
ALTER TABLE OrderBasicInfoTable ADD PRIMARY KEY (oOrderID);


SELECT * FROM OrderBasicInfoTable;
DELETE FROM OrderBasicInfoTable WHERE oOrderID='20251129160000000';

SELECT * FROM OrderGeneralInfoTable,OrderBasicInfoTable WHERE OrderGeneralInfoTable.oOrderID=OrderBasicInfoTable.oOrderID AND OrderGeneralInfoTable.oOrderID='20251129170000000';


--订单收货人信息表
CREATE TABLE OrdererInfoTable
(
oOrderID varchar(17),									--COMMENT '订单编号',
oReceiverName varchar(128) NOT NULL,					--COMMENT '收货人',
oReceieverGender varchar(64),							--COMMENT '收货人性别',
oReceieverEmail varchar(32) DEFAULT NULL				--COMMENT '收货人邮箱',
);
ALTER TABLE OrdererInfoTable ADD CONSTRAINT OrdererInfoForeignKey FOREIGN KEY (oOrderID) REFERENCES OrderGeneralInfoTable(oOrderID);
ALTER TABLE OrdererInfoTable ADD COLUMN tLastUpdateTime TIMESTAMPTZ(1) DEFAULT CURRENT_TIMESTAMP(1);
ALTER TABLE OrdererInfoTable ADD PRIMARY KEY (oOrderID);

SELECT * FROM OrdererInfoTable;
DELETE FROM OrdererInfoTable WHERE oOrderID='20251129900000000';
DELETE FROM OrdererInfoTable WHERE oReceiverName='蓝钱后';

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
ALTER TABLE OrderDeliveryInfo ADD COLUMN tLastUpdateTime TIMESTAMPTZ(1) DEFAULT CURRENT_TIMESTAMP(1);
ALTER TABLE OrderDeliveryInfo ADD PRIMARY KEY (oOrderID);

SELECT * FROM OrderDeliveryInfo;
DELETE FROM OrderDeliveryInfo WHERE oOrderID='20251129160000000';

SELECT * FROM OrdererInfoTable,OrderDeliveryInfo WHERE OrdererInfoTable.oOrderID=OrderDeliveryInfo.oOrderID AND OrdererInfoTable.oOrderID='20251129170000000';

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
ALTER TABLE OrderProductInfoTable ADD COLUMN oProductDeliveryStatus varchar(32) default'Submitted';		--单个商品的交付状态
ALTER TABLE OrderProductInfoTable ADD COLUMN tLastUpdateTime TIMESTAMPTZ(1) DEFAULT CURRENT_TIMESTAMP(1);
ALTER TABLE OrderProductInfoTable ADD PRIMARY KEY (oOrderID,pID);

SELECT * FROM OrderProductInfoTable;
DELETE FROM OrderProductInfoTable WHERE oOrderID='20251129160000000';

CREATE TABLE MerchantManagementTable
(
uID varchar(32),						--COMMENT'商人账户'
pID varchar(32)							--COMMENT'商人上架的商品的编号'
);
CREATE INDEX index_uID_MerchantManagementTable ON MerchantManagementTable(uID);
CREATE INDEX index_pID_MerchantManagementTable ON MerchantManagementTable(pID);
ALTER TABLE MerchantManagementTable ADD PRIMARY KEY (uID,pID);

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
INSERT INTO UserAccountTable(uID,uNickName,uPassword,uPhone,uEmail,uGender,uRegisterDate,uAccountStatus,uAccountType)VALUES
('206914893@163.com','Merchant002','48fcd3b9d4fe5e77dfc18bc50c1d98ae10e63f5a1853e3fe40eb5f96f3396b7d','18890816389','206914893@qq.com','某购物袋','2025-12-05 16:07:11','正常','商户');
INSERT INTO UserAccountTable(uID,uNickName,uPassword,uPhone,uEmail,uGender,uRegisterDate,uAccountStatus,uAccountType)VALUES
('Admin001','Admin001','ac1943286401ef7bf3e0609032171386b2265952f5000f3fc246bbab7c6021ae','123456789','123456789@qq.com','Observer','2025-12-15 16:07:11','正常','管理员');



DELETE FROM UserAccountTable WHERE uID='206914893@163.com';


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
SELECT * FROM ProductTable WHERE pName LIKE '% %' AND pReleaseDate >= '2010-12-25 00:00:00' AND pReleaseDate <= '2029-11-30 23:59:59';
UPDATE ProductTable SET ProductTable.pInventory=120 WHERE ProductTable.pID='0000000000000020';0000000000000021
UPDATE ProductTable SET ProductTable.pStatus='上架' WHERE ProductTable.pID='0000000000000022';




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
--pType varchar(16)DEFAULT '展示图',						--COMMENT '图片类型',	--pType图片类型:缩略图，展示图	
--pImagePath varchar(64) 									--COMMENT '商品图片路径',
--);

--assets/images/
INSERT INTO ProductImagesTable(pID,pImgType,pImagePath)VALUES 
('0000000000000000','缩略图','assets/images/iphone13_16.png'),
('0000000000000000','展示图','assets/images/iphone13_15.png'),

('0000000000000001','缩略图','assets/images/iphone13_14.png'),
('0000000000000001','展示图','assets/images/iphone13_13.png'),

('0000000000000002','缩略图','assets/images/iphone13_12.png'),
('0000000000000002','展示图','assets/images/iphone13_11.png'),

('0000000000000003','缩略图','assets/images/iphone13_10.png'),
('0000000000000003','展示图','assets/images/iphone13_9.png'),

('0000000000000004','缩略图','assets/images/iphone13_8.png'),
('0000000000000004','展示图','assets/images/iphone13_7.png'),

('0000000000000005','缩略图','assets/images/iphone13_6.png'),
('0000000000000005','展示图','assets/images/iphone13_5.png'),

('0000000000000006','缩略图','assets/images/iphone13_4.png'),
('0000000000000006','展示图','assets/images/iphone13_3.png'),

('0000000000000007','缩略图','assets/images/iphone13_2.png'),
('0000000000000007','展示图','assets/images/iphone13_1.png'),

('0000000000000008','缩略图','assets/images/iphone13_1.png'),
('0000000000000008','展示图','assets/images/iphone13_2.png'),

('0000000000000009','缩略图','assets/images/iphone13_3.png'),
('0000000000000009','展示图','assets/images/iphone13_4.png'),

('0000000000000010','缩略图','assets/images/iphone13_5.png'),
('0000000000000010','展示图','assets/images/iphone13_6.png'),

('0000000000000011','缩略图','assets/images/iphone13_7.png'),
('0000000000000011','展示图','assets/images/iphone13_8.png'),

('0000000000000012','缩略图','assets/images/iphone13_9.png'),
('0000000000000012','展示图','assets/images/iphone13_10.png'),

('0000000000000013','缩略图','assets/images/iphone13_11.png'),
('0000000000000013','展示图','assets/images/iphone13_12.png'),

('0000000000000014','缩略图','assets/images/macbookair_1.png'),
('0000000000000015','缩略图','assets/images/macbookpro_1.png'),
('0000000000000016','缩略图','assets/images/imac_1.png'),
('0000000000000017','缩略图','assets/images/macbookpro_4.png'),
('0000000000000018','缩略图','assets/images/Galaxy_1.png'),
('0000000000000019','缩略图','assets/images/GalaxySultra_1.png'),
('0000000000000020','缩略图','assets/images/TS-990S_1.png'),

('0000000000000014','展示图','assets/images/macbookair_2.png'),
('0000000000000015','展示图','assets/images/macbookpro_2.png'),
('0000000000000016','展示图','assets/images/imac_2.png'),
('0000000000000017','展示图','assets/images/macbookpro_3.png'),
('0000000000000018','展示图','assets/images/Galaxy_2.png'),
('0000000000000019','展示图','assets/images/GalaxySultra_2.png'),
('0000000000000020','展示图','assets/images/TS-990S_2.png');
INSERT INTO ProductImagesTable(pID,pImgType,pImagePath)VALUES 
('0000000000000021','缩略图','/assets/images/0000000000000021_5e87f95e-7972-44a0-bb8c-4224d0b4dc92.jpg'),
('0000000000000021','展示图','/assets/images/0000000000000021_44bfe769-c7eb-48f8-9784-091b6ab087c2.jpg'),
('0000000000000021','展示图','/assets/images/0000000000000021_e8c3c5c0-9dd5-4550-bebe-886cd9c10f86.jpg');


SELECT * FROM ProductImagesTable;


SELECT ProductTable.*,ProductImagesTable.pImgType,ProductImagesTable.pImagePath FROM ProductTable,ProductImagesTable WHERE LOWER(ProductTable.pName) LIKE LOWER('%iph%') AND ProductTable.pID=ProductImagesTable.pID AND ProductImagesTable.pImgType='缩略图';

--后续可以建立一个视图

SELECT * FROM ProductTable WHERE pID='0000000000000000';
SELECT * FROM ProductImagesTable WHERE pID='0000000000000000';

SELECT * FROM UserDeliveryInfoTable WHERE uID='';

SELECT * FROM ProductTable WHERE pID=0000000000000007 OR pID='0000000000000020' OR;

UPDATE OrderBasicInfoTable SET oStatus='Paid' WHERE oOrderID='20251119';


--注册函数，检查账号是否存在，同时同账号注册添加反馈
CREATE OR REPLACE FUNCTION RegistrationResult(iuID varchar(32),iuNickName varchar(32),iuPassword varchar(128),
iuPhone varchar(11),iuEmail varchar(32),iuGender varchar(64),
iuRegisterDate date,iuAccountType varchar(16),iuAccountStatus varchar(10))
RETURN varchar AS DECLARE res varchar;exist boolean; 
BEGIN
	SELECT EXISTS(SELECT UserAccountTable.uID FROM UserAccountTable WHERE UserAccountTable.uID=iuID) INTO exist;
	IF exist THEN
		RETURN 'Account Exist';
	ELSE 
	 INSERT INTO UserAccountTable VALUES(iuID,iuNickName,iuPassword,
	 iuPhone,iuEmail,iuGender,
	 iuRegisterDate,iuAccountType,iuAccountStatus);
	 RETURN 'Registration Success';
	 END IF;
	 
	 EXCEPTION
	 	WHEN unique_violation THEN 
	 		RETURN 'Account Exist';
	 	WHEN OTHERS THEN 
	 		RETURN 'Registration Fail';
END;

--RegistrationResult函数检测
SELECT RegistrationResult('079754646484','test1','5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9',
'12345678911','asdw564651@gmail.com','Armor Helicopter',
'2025-11-21 18:21:54','普通用户','正常');
SELECT * FROM UserAccountTable;



CREATE OR REPLACE FUNCTION ProductInventoryDecrease(ipID varchar(32),iNum int16)
RETURN boolean AS DECLARE currentInventory int16;
BEGIN 
	SELECT ProductTable.pInventory INTO currentInventory FROM ProductTable WHERE ProductTable.pID=ipID FOR UPDATE;
	IF currentInventory-iNum>=0 THEN 
		UPDATE ProductTable SET ProductTable.pInventory=currentInventory-iNum WHERE ProductTable.pID=ipID;
		RETURN true;
	ELSE 
		RETURN false;
	END IF;
END

--ProductInventoryDecrease函数检查
SELECT ProductInventoryDecrease('0000000000000000',1115);
SELECT * FROM ProductTable;

--ProductInventoryAscend库存数量增加函数
CREATE OR REPLACE FUNCTION ProductInventoryAscend(ipID varchar(32),iNum int16)
RETURN void AS DECLARE currentInventory int16;
BEGIN 
	SELECT ProductTable.pInventory INTO currentInventory FROM ProductTable WHERE ProductTable.pID=ipID FOR UPDATE;
	UPDATE ProductTable SET ProductTable.pInventory=currentInventory+iNum WHERE ProductTable.pID=ipID;
END

--ProductInventoryAscend函数检查
SELECT ProductInventoryAscend('0000000000000000',15);
SELECT * FROM ProductTable;
SELECT * FROM ProductImagesTable;
SELECT * FROM ProductClicksInfoTable;
--UPDATE ProductTable SET pInfo='能帮你做任何事情的生活助手' WHERE pID='0000000000000032';

SELECT OrderProductInfoTable.pID,OrderProductInfoTable.oAmount FROM OrderProductInfoTable WHERE OrderProductInfoTable.oOrderID='';

UPDATE OrderBasicInfoTable SET oState='Cancelled' WHERE oOrderID='';
SELECT OrderProductInfoTable.pID,OrderProductInfoTable.oAmount FROM OrderProductInfoTable WHERE oOrderID='';


INSERT INTO ProductClicksInfoTable(pID,pClicksAmount)VALUES
('0000000000000004',47),
('0000000000000013',35),
('0000000000000014',10),
('0000000000000016',21),
('0000000000000017',12),
('0000000000000018',123),
('0000000000000019',40);

--当点击就添加点击次数的函数
CREATE OR REPLACE FUNCTION ProductClicksInfoTableClickAdd(ipID varchar(32))
RETURN void AS DECLARE currentClickAmount int16;
BEGIN 
	INSERT INTO ProductClicksInfoTable(pID,pClicksAmount)VALUES (ipID,1) ON CONFLICT (pID)DO UPDATE SET pClicksAmount=ProductClicksInfoTable.pClicksAmount+1;
END
--ProductClicksInfoTableClickAdd函数测试
SELECT ProductClicksInfoTableClickAdd('0000000000000007');

SELECT * FROM ProductClicksInfoTable;

--查找前50个
SELECT pID FROM ProductClicksInfoTable ORDER BY pClicksAmount DESC LIMIT 50;


--查找点击量前50的点击商品并按照点击量一次下降
SELECT * FROM ProductTable
INNER JOIN ProductClicksInfoTable ON ProductTable.pID = ProductClicksInfoTable.pID
INNER JOIN ProductImagesTable ON ProductImagesTable.pID=ProductClicksInfoTable.pID AND ProductImagesTable.pImgType='缩略图'
ORDER BY ProductClicksInfoTable.pClicksAmount DESC 
LIMIT 50 ;

--商品添加进购物车函数，重复添加就在原来的基础上继续添加数字
CREATE OR REPLACE FUNCTION UserShoppingCartTableAdd(iuID varchar(32),ipID varchar(32),icAmount int16)
RETURN boolean AS DECLARE currentCartAmount int16;
BEGIN
--	SELECT cAmount INTO currentCartAmount FROM UserShoppingCartTable WHERE uID=iuID AND pID=ipID FOR UPDATE;
	INSERT INTO UserShoppingCartTable(uID,pID,cAmount)VALUES(iuID,ipID,icAmount) ON CONFLICT (uID,pID) DO UPDATE SET cAmount=UserShoppingCartTable.cAmount+icAmount;
	RETURN TRUE;
EXCEPTION
	WHEN OTHERS THEN
	RETURN FALSE;
END

--UserShoppingCartTableAdd函数检查
SELECT UserShoppingCartTableAdd('18775332736','0000000000000010',1);
SELECT * FROM UserShoppingCartTable;
SELECT * FROM UserFavoritesTable;

SELECT UserShoppingCartTable.pID,UserShoppingCartTable.cAmount,ProductTable.pName,ProductTable.pType,ProductTable.pPrice,ProductImagesTable.pImagePath FROM UserShoppingCartTable,ProductTable,ProductImagesTable WHERE UserShoppingCartTable.pID=ProductTable.pID AND ProductImagesTable.pID=UserShoppingCartTable.pID AND ProductImagesTable.pImgType='缩略图' AND uID='18775332736';

--上架商品函数
CREATE OR REPLACE FUNCTION NewProductOnSaleFunction(iuID UserAccountTable.uID%Type,ipName ProductTable.pName%Type,ipType ProductTable.pType%Type,
ipDiscount ProductTable.pDiscount%Type,ipPrice ProductTable.pPrice%Type,ipProducer ProductTable.pProducer%Type,
ipRelease ProductTable.pReleaseDate%Type,ipInfo ProductTable.pInfo%Type,ipInventory ProductTable.pInventory%Type,ipStatus ProductTable.pStatus%Type)
RETURN varchar AS 
DECLARE 
	lock_id INT:=12345;
	New_pID varchar(32);
BEGIN 
		PERFORM pg_advisory_lock(lock_id);		--开启锁
		
		SELECT lpad(to_number(MAX(pID)+1)::TEXT,16,'0') INTO New_pID FROM ProductTable;
		 RAISE NOTICE '生成的New_pID为：%', New_pID;  
		 INSERT INTO ProductTable(pID,pName,pType,pDiscount,pPrice,pProducer,pReleaseDate,pInfo,pInventory,pStatus)
		 values(New_pID,ipName,ipType,ipDiscount,ipPrice,ipProducer,ipRelease,ipInfo,ipInventory,ipStatus);
		 INSERT INTO MerchantsProductTable(uID,pID)VALUES (iuID,New_pID);
		
		PERFORM pg_advisory_unlock(lock_id);	--关闭锁
		RETURN New_pID;
	EXCEPTION 
		WHEN OTHERS THEN
		PERFORM pg_advisory_unlock(lock_id);	--异常释放锁
        RAISE;
		RETURN NULL ;
END


--商品上架函数测试
SELECT NewProductOnSaleFunction('1522788291@163.com','test1','test1',1.0,1.0,'test1','2025-11-28 21:55:10','test',10,'上架');
SELECT * FROM ProductTable;
DELETE FROM ProductTable WHERE pID='0000000000000021';


SELECT * FROM UserDeliveryInfoTable;
DELETE FROM UserDeliveryInfoTable WHERE uID='18775332736';

SELECT DISTINCT ProductTable.pType FROM ProductTable WHERE ProductTable.pStatus='上架';


INSERT INTO UserAccountTable(uID,uNickName,uPassword,uPhone,uEmail,uGender,uRegisterDate,uAccountStatus,uAccountType)VALUES
('1522788291@163.com','Merchant001','760508cf0e664fae4e28c9d837b4840ee6a21203d3894bbe18edbcc54378bd13',
'18074948512','18074948512@gmail.com','male',
'2025-12-04 20:04:00','正常','商户');


SELECT * FROM MerchantsProductTable;
INSERT INTO MerchantsProductTable(uID,pID)values('1522788291@163.com','0000000000000021');
INSERT INTO MerchantsProductTable(uID,pID)values('1522788291@163.com','0000000000000022');


SELECT * FROM OrderProductInfoTable WHERE pID IN(SELECT DISTINCT  MerchantsProductTable.pID FROM MerchantsProductTable WHERE MerchantsProductTable.uID='1522788291@163.com');
SELECT MerchantsProductTable.pID FROM MerchantsProductTable WHERE MerchantsProductTable.uID='1522788291@163.com';
SELECT * FROM OrderProductInfoTable;

--触发器函数
CREATE OR REPLACE FUNCTION updateTimestamp()
RETURN TRIGGER AS 
BEGIN 
	NEW.tLastUpdateTime=CURRENT_TIMESTAMP(1);
	RETURN NEW;
END;
--定义触发器(UserAccountTable上的)
CREATE TRIGGER trigger_UserAccountTable_update
BEFORE UPDATE ON UserAccountTable
FOR EACH ROW 
EXECUTE FUNCTION updateTimestamp();

--定义触发器(UserDeliveryInfoTable上的)
CREATE TRIGGER trigger_UserDeliveryInfoTable_update
BEFORE UPDATE ON UserDeliveryInfoTable
FOR EACH ROW 
EXECUTE FUNCTION updateTimestamp();

--定义触发器(ProductTable上的)
CREATE TRIGGER trigger_ProductTable_update
BEFORE UPDATE ON ProductTable
FOR EACH ROW 
EXECUTE FUNCTION updateTimestamp();

--定义触发器(OrderBasicInfoTable上的)
CREATE TRIGGER trigger_OrderBasicInfoTable_update
BEFORE UPDATE ON OrderBasicInfoTable
FOR EACH ROW 
EXECUTE FUNCTION updateTimestamp();

--定义触发器(OrderProductInfoTable上的)
CREATE TRIGGER trigger_OrderProductInfoTable_update
BEFORE UPDATE ON OrderProductInfoTable
FOR EACH ROW 
EXECUTE FUNCTION updateTimestamp();

SELECT 
OrderGeneralInfoTable.oOrderID, OrderGeneralInfoTable.oOrdererID, 
OrderBasicInfoTable.oDate, OrderBasicInfoTable.oStatus, 
OrdererInfoTable.oReceiverName, OrdererInfoTable.oReceieverGender, OrdererInfoTable.oReceieverEmail, 
OrderDeliveryInfo.oDeliveryAddress, OrderDeliveryInfo.oPostalCode, OrderDeliveryInfo.oContactPhone, OrderDeliveryInfo.oDeliveryNote 
FROM OrderGeneralInfoTable 
INNER JOIN OrderBasicInfoTable ON OrderGeneralInfoTable.oOrderID=OrderBasicInfoTable.oOrderID 
INNER JOIN OrdererInfoTable ON OrdererInfoTable.oOrderID=OrderGeneralInfoTable.oOrderID 
INNER JOIN OrderDeliveryInfo ON OrderDeliveryInfo.oOrderID=OrderGeneralInfoTable.oOrderID ;
