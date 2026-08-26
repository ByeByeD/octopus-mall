---------------------------------------------
-- Export file for user BAZHUAYU           --
-- Created by 64566 on 2026/8/24, 10:24:16 --
---------------------------------------------

spool octopus-mall.log

prompt
prompt Creating table OTPS_PERMISSION
prompt ==============================
prompt
create table OTPS_PERMISSION
(
  permission_name VARCHAR2(128) not null,
  remark          VARCHAR2(255) default '',
  type            CHAR(1) default 'G',
  create_time     DATE default SYSTIMESTAMP,
  update_time     DATE default SYSTIMESTAMP
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
comment on column OTPS_PERMISSION.type
  is '类型：B-Business，C-business，R-Rider，G-Common';
alter table OTPS_PERMISSION
  add constraint PK_OTPS_PERMISSION primary key (PERMISSION_NAME)
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

prompt
prompt Creating table OTPS_USER
prompt ========================
prompt
create table OTPS_USER
(
  id           VARCHAR2(20 CHAR) not null,
  account      VARCHAR2(20 CHAR) not null,
  password     VARCHAR2(100 CHAR) not null,
  nickname     VARCHAR2(20 CHAR),
  user_image   VARCHAR2(200 CHAR),
  phone_number VARCHAR2(20 CHAR),
  email        VARCHAR2(300 CHAR),
  type         CHAR(1),
  status       VARCHAR2(10 CHAR) default 'normal',
  audit_status VARCHAR2(10 CHAR) default 'pass',
  audit_remark VARCHAR2(200 CHAR),
  create_time  DATE default SYSDATE not null,
  update_time  DATE default SYSDATE not null,
  is_deleted   NUMBER(1) default 0 not null
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
comment on table OTPS_USER
  is '平台用户表：商家、顾客、骑手统一账号';
comment on column OTPS_USER.type
  is '用户类型 B:商家，C:顾客，R:骑手';
comment on column OTPS_USER.audit_status
  is '审核状态 wait待审核 pass审核通过 reject驳回';
alter table OTPS_USER
  add primary key (ID)
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
alter table OTPS_USER
  add constraint UK_OTPS_USER_ACCOUNT unique (ACCOUNT)
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
alter table OTPS_USER
  add constraint UK_OTPS_USER_PHONE unique (PHONE_NUMBER)
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

prompt
prompt Creating table OTPS_USER_HIS
prompt ============================
prompt
create table OTPS_USER_HIS
(
  his_id          VARCHAR2(20 CHAR) not null,
  id              VARCHAR2(20 CHAR) not null,
  account         VARCHAR2(20 CHAR) not null,
  password        VARCHAR2(100 CHAR) not null,
  nickname        VARCHAR2(20 CHAR),
  user_image      VARCHAR2(200 CHAR),
  phone_number    VARCHAR2(20 CHAR),
  email           VARCHAR2(300 CHAR),
  type            CHAR(1),
  status          VARCHAR2(10 CHAR),
  audit_status    VARCHAR2(10 CHAR),
  audit_remark    VARCHAR2(200 CHAR),
  create_time     DATE not null,
  update_time     DATE not null,
  is_deleted      NUMBER(1) not null,
  his_create_time DATE default SYSDATE not null
)
tablespace BAZHUAYU
  pctfree 10
  initrans 1
  maxtrans 255;
comment on table OTPS_USER_HIS
  is '用户表历史快照表，otps_user更新/逻辑删除时保存变更前旧数据';
create index IDX_OTPS_USER_HIS_UID on OTPS_USER_HIS (ID)
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255;
alter table OTPS_USER_HIS
  add primary key (ID_HIS)
  using index 
  tablespace BAZHUAYU
  pctfree 10
  initrans 2
  maxtrans 255;

prompt
prompt Creating table OTPS_USER_PERMISSION
prompt ===================================
prompt
create table OTPS_USER_PERMISSION
(
  id              VARCHAR2(20 CHAR) not null,
  user_id         VARCHAR2(20 CHAR) not null,
  permission_name VARCHAR2(128) not null,
  create_time     DATE default SYSTIMESTAMP,
  expire_time     DATE
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
alter table OTPS_USER_PERMISSION
  add constraint PK_OTPS_USER_PERMISSION primary key (ID)
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


spool off
