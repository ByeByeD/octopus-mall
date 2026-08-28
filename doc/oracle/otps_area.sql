-- Create table
create table OTPS_AREA
(
    id          NUMBER(10) not null,
    area_code   VARCHAR2(10 CHAR) not null,
    area_name   VARCHAR2(100 CHAR) not null,
    parent_code VARCHAR2(10 CHAR),
    area_level  NUMBER(1) not null,
    create_time DATE default SYSDATE,
    update_time DATE default SYSDATE
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
-- Add comments to the columns
comment on column OTPS_AREA.area_level
  is '层级：1-省份,2-城市,3-区县';
-- Create/Recreate indexes
create unique index UK_OTPS_AREA_CODE on OTPS_AREA (AREA_CODE)
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
-- Create/Recreate primary, unique and foreign key constraints
alter table OTPS_AREA
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
