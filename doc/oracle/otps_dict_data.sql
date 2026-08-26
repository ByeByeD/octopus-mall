-- =============================================
-- Octopus-Mall 字典表初始数据
-- 作者: 64566
-- 创建日期: 2026/8/25
-- 说明: OTPS_DICT 表的初始枚举值数据
--       dict_id 格式：表名:字段名（小写）
--       仅插入 is_enabled = 1 的正常枚举值
--       停用数据可通过 update is_enabled = 0 逻辑删除
-- =============================================

spool otps_dict_data.log

prompt Inserting into OTPS_DICT...

-- =============================================
-- 1. 店铺分类 shop:category
-- =============================================
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('shop:category', 'fresh',       '生鲜',     1, 1, '生鲜果蔬、肉禽蛋类', sysdate, sysdate);
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('shop:category', 'food',         '美食',     2, 1, '餐饮、熟食、烘焙', sysdate, sysdate);
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('shop:category', 'hardware',    '五金',     3, 1, '工具、五金件、建材', sysdate, sysdate);
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('shop:category', 'daily',       '日用品',   4, 1, '日常洗护、家居用品', sysdate, sysdate);
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('shop:category', 'supermarket', '超市',     5, 1, '综合超市、便利店', sysdate, sysdate);
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('shop:category', 'flower',      '花店',     6, 1, '鲜花、绿植、园艺', sysdate, sysdate);

-- =============================================
-- 2. 店铺审核状态 shop:audit_status
-- =============================================
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('shop:audit_status', 'wait',   '待审核',   1, 1, '商家提交后进入待审核状态', sysdate, sysdate);
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('shop:audit_status', 'pass',   '审核通过', 2, 1, '管理员审核通过', sysdate, sysdate);
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('shop:audit_status', 'reject', '审核驳回', 3, 1, '管理员驳回，商家需修改后重新提交', sysdate, sysdate);

-- =============================================
-- 3. 店铺经营状态 shop:shop_status
-- =============================================
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('shop:shop_status', 'normal',   '营业中', 1, 1, '店铺营业中', sysdate, sysdate);
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('shop:shop_status', 'closed',   '关闭中', 2, 1, '店铺关闭', sysdate, sysdate);
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('shop:shop_status', 'banned',   '已封禁', 3, 1, '管理员强制封禁', sysdate, sysdate);

-- =============================================
-- 4. 用户类型 user:type
-- =============================================
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('user:type', 'B', '商家', 1, 1, '商家用户', sysdate, sysdate);
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('user:type', 'C', '顾客', 2, 1, '消费者', sysdate, sysdate);
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('user:type', 'R', '骑手', 3, 1, '配送骑手', sysdate, sysdate);

-- =============================================
-- 5. 用户状态 user:status
-- =============================================
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('user:status', 'normal',  '正常',  1, 1, '账号正常使用', sysdate, sysdate);
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('user:status', 'disable', '禁用',  2, 1, '账号被禁用', sysdate, sysdate);

prompt 5 dictionary types inserted.

-- 提交事务
commit;

spool off
