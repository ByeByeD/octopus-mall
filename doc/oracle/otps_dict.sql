-- =============================================
-- Octopus-Mall 公用字典表建表脚本
-- 作者: 64566
-- 创建日期: 2026/8/25
-- 说明: 公用字典表，存储系统枚举类型值，供各业务表关联查询
--       替代硬编码枚举，前后端统一从字典表获取枚举列表
-- =============================================

spool otps_dict.log

prompt
prompt Creating table OTPS_DICT
prompt ==================================
prompt
create table otps_dict
(
  dict_id      varchar2(64 char) not null,
  enum_code    varchar2(32 char) not null,
  enum_name    varchar2(64 char) not null,
  sort_order   number(3,0) default 0 not null,
  is_enabled   number(1) default 1 not null,
  remark       varchar2(128 char) default '',
  create_time  date default sysdate not null,
  update_time  date default sysdate not null,
  constraint pk_otps_dict primary key (dict_id, enum_code)
)
tablespace bazhuayu
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
comment on table otps_dict is '公用字典表：存储系统枚举类型值，供各业务表关联查询';

-- =============================================
-- 字段注释
-- =============================================
comment on column otps_dict.dict_id is '字典标识，格式：表名:字段名，如 shop:category、user:type';
comment on column otps_dict.enum_code is '枚举编码（具体枚举值），如 fresh、food、wait、pass';
comment on column otps_dict.enum_name is '枚举名称（中文含义），如 生鲜、待审核';
comment on column otps_dict.sort_order is '排序序号，控制前端下拉列表的展示顺序';
comment on column otps_dict.is_enabled is '是否启用：1-启用（默认），0-停用';
comment on column otps_dict.remark is '备注说明，如枚举值的补充解释';
comment on column otps_dict.create_time is '记录创建时间';
comment on column otps_dict.update_time is '数据最后更新时间';

prompt otps_dict table created.

spool off
