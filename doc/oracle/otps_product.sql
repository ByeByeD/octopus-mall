-- =============================================
-- Octopus-Mall 商品表建表脚本
-- 作者: 64566
-- 创建日期: 2026/8/25
-- 说明: 店铺商品信息表
-- =============================================

spool otps_product.log

prompt
prompt Creating table OTPS_PRODUCT
prompt ================================
prompt
create table OTPS_PRODUCT
(
  -- 主键
  ID                   VARCHAR2(20 CHAR) not null,
  -- 外键：关联店铺，OTPS_SHOP 表
  SHOP_ID              VARCHAR2(20 CHAR) not null,
  -- 商品基本信息
  NAME                 VARCHAR2(100 CHAR) not null,
  CATEGORY             VARCHAR2(20 CHAR) not null,
  -- 价格与库存
  PRICE                NUMBER(20,2) not null,
  STOCK                NUMBER(10,0) not null,
  -- 商品详情
  IMAGE_URL            VARCHAR2(200 CHAR),
  REMARK               VARCHAR2(200 CHAR),
  -- 商品状态
  STATUS               VARCHAR2(10 CHAR) default 'onsale' not null,
  -- 通用字段
  CREATE_TIME          DATE default SYSDATE not null,
  UPDATE_TIME          DATE default SYSDATE not null
)
tablespace BAZHUAYU
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

-- =============================================
-- 表注释
-- =============================================
comment on table OTPS_PRODUCT is '店铺商品信息表';

-- 字段注释
comment on column OTPS_PRODUCT.ID          is '商品主键ID（雪花ID）';
comment on column OTPS_PRODUCT.SHOP_ID     is '店铺ID，关联 OTPS_SHOP.ID';
comment on column OTPS_PRODUCT.NAME        is '商品名称';
comment on column OTPS_PRODUCT.CATEGORY    is '商品分类: fruit-水果 vegetable-蔬菜 food-食品 drink-饮料 daily-日用品';
comment on column OTPS_PRODUCT.PRICE       is '商品单价（元）';
comment on column OTPS_PRODUCT.STOCK       is '库存数量';
comment on column OTPS_PRODUCT.IMAGE_URL    is '商品主图（相对路径）';
comment on column OTPS_PRODUCT.REMARK      is '商品简单描述';
comment on column OTPS_PRODUCT.STATUS      is '商品状态: onsale-上架 offshelf-下架';
comment on column OTPS_PRODUCT.CREATE_TIME is '记录创建时间';
comment on column OTPS_PRODUCT.UPDATE_TIME is '数据最后更新时间';

-- =============================================
-- 主键约束
-- =============================================
alter table OTPS_PRODUCT
  add constraint PK_OTPS_PRODUCT primary key (ID)
  using index
  tablespace BAZHUAYU
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

-- =============================================
-- 索引
-- =============================================
-- 店铺ID查询（查询某店铺所有商品）
create index IDX_OTPS_PRODUCT_SHOP_ID on OTPS_PRODUCT (SHOP_ID)
  tablespace BAZHUAYU
  pctfree 10
  initrans 2
  maxtrans 255;

spool off
