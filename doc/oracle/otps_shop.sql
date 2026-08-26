-- =============================================
-- Octopus-Mall 店铺表建表脚本
-- 作者: 64566
-- 创建日期: 2026/8/25
-- 说明: 我的小店店铺信息表
-- =============================================

spool otps_shop.log

prompt
prompt Creating table OTPS_SHOP
prompt ==============================
prompt
create table OTPS_SHOP
(
  -- 主键
  ID                   VARCHAR2(20 CHAR) not null,
  -- 外键：关联商家用户，OTPS_USER 表
  USER_ID              VARCHAR2(20 CHAR) not null,
  -- 店铺基本信息
  SHOP_NAME            VARCHAR2(100 CHAR) not null,
  PROVINCE             VARCHAR2(50 CHAR) not null,
  CITY                 VARCHAR2(50 CHAR) not null,
  DETAIL_ADDRESS       VARCHAR2(200 CHAR) not null,
  CATEGORY             VARCHAR2(20 CHAR) not null,
  DESCRIPTION          VARCHAR2(500 CHAR),
  -- 店铺图片
  AVATAR_URL           VARCHAR2(200 CHAR),
  -- 联系信息
  CONTACT_NAME         VARCHAR2(50 CHAR) not null,
  CONTACT_PHONE        VARCHAR2(20 CHAR) not null,
  -- 营业执照信息
  LICENSE_NO           VARCHAR2(50 CHAR) not null,
  LICENSE_PHOTO_URL    VARCHAR2(200 CHAR) not null,
  LICENSE_EXPIRE_DATE  DATE,
  -- 审核信息
  AUDIT_STATUS         VARCHAR2(10 CHAR) default 'wait' not null,
  AUDIT_REMARK         VARCHAR2(200 CHAR),
  AUDIT_TIME           DATE,
  -- 店铺经营状态
  SHOP_STATUS          VARCHAR2(10 CHAR) default 'normal' not null,
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
comment on table OTPS_SHOP is '我的小店店铺信息表';

-- 字段注释
comment on column OTPS_SHOP.ID                   is '店铺主键ID（雪花ID）';
comment on column OTPS_SHOP.USER_ID              is '商家用户ID，关联 OTPS_USER.ID';
comment on column OTPS_SHOP.SHOP_NAME            is '店铺名称';
comment on column OTPS_SHOP.PROVINCE            is '店铺所在省份';
comment on column OTPS_SHOP.CITY                is '店铺所在城市';
comment on column OTPS_SHOP.DETAIL_ADDRESS      is '店铺详细地址（街道/门牌号）';
comment on column OTPS_SHOP.CATEGORY            is '店铺分类: fresh-生鲜 food-美食 hardware-五金 daily-日用品 supermarket-超市 flower-花店';
comment on column OTPS_SHOP.DESCRIPTION         is '店铺简介';
comment on column OTPS_SHOP.AVATAR_URL          is '店铺头像/封面图（相对路径）';
comment on column OTPS_SHOP.CONTACT_NAME        is '店铺联系人姓名';
comment on column OTPS_SHOP.CONTACT_PHONE       is '店铺联系人电话';
comment on column OTPS_SHOP.LICENSE_NO          is '营业执照编号';
comment on column OTPS_SHOP.LICENSE_PHOTO_URL   is '营业执照照片（相对路径）';
comment on column OTPS_SHOP.LICENSE_EXPIRE_DATE is '营业执照有效期';
comment on column OTPS_SHOP.AUDIT_STATUS         is '审核状态: wait-待审核 pass-审核通过 reject-驳回';
comment on column OTPS_SHOP.AUDIT_REMARK         is '审核备注/驳回原因';
comment on column OTPS_SHOP.AUDIT_TIME          is '管理员审核时间';
comment on column OTPS_SHOP.SHOP_STATUS          is '店铺状态: normal-营业 closed-歇业 disabled-停业 banned-封禁';
comment on column OTPS_SHOP.CREATE_TIME         is '记录创建时间';
comment on column OTPS_SHOP.UPDATE_TIME        is '数据最后更新时间';

-- =============================================
-- 唯一约束：每个商家用户只能拥有一家小店
-- =============================================
alter table OTPS_SHOP
  add constraint UK_OTPS_SHOP_USER unique (USER_ID)
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
-- 商家ID查询（用于「我的小店」页面）
create index IDX_OTPS_SHOP_USER_ID on OTPS_SHOP (USER_ID)
  tablespace BAZHUAYU
  pctfree 10
  initrans 2
  maxtrans 255;

-- 审核状态查询（管理员审核列表）
create index IDX_OTPS_SHOP_AUDIT_STATUS on OTPS_SHOP (AUDIT_STATUS)
  tablespace BAZHUAYU
  pctfree 10
  initrans 2
  maxtrans 255;

-- 店铺状态查询
create index IDX_OTPS_SHOP_STATUS on OTPS_SHOP (SHOP_STATUS)
  tablespace BAZHUAYU
  pctfree 10
  initrans 2
  maxtrans 255;

-- 分类查询（店铺分类列表）
create index IDX_OTPS_SHOP_CATEGORY on OTPS_SHOP (CATEGORY)
  tablespace BAZHUAYU
  pctfree 10
  initrans 2
  maxtrans 255;

spool off
