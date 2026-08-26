-- =============================================
-- Octopus-Mall 商品表字典数据
-- 作者: 64566
-- 创建日期: 2026/8/25
-- 说明: OTPS_DICT 表的商品相关枚举值
-- =============================================

spool otps_product_dict.log

prompt Inserting into OTPS_DICT (product:*)...

-- =============================================
-- 1. 商品分类 product:category
-- =============================================
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('product:category', 'fruit',      '水果',     1, 1, '新鲜水果', sysdate, sysdate);
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('product:category', 'vegetable', '蔬菜',     2, 1, '新鲜蔬菜', sysdate, sysdate);
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('product:category', 'food',      '食品',     3, 1, '零食、糕点、熟食', sysdate, sysdate);
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('product:category', 'drink',     '饮料',     4, 1, '饮品、矿泉水', sysdate, sysdate);
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('product:category', 'daily',     '日用品',   5, 1, '日常洗护、家居用品', sysdate, sysdate);

-- =============================================
-- 2. 商品状态 product:status
-- =============================================
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('product:status', 'onsale',   '上架',   1, 1, '商品上架销售中', sysdate, sysdate);
insert into otps_dict (dict_id, enum_code, enum_name, sort_order, is_enabled, remark, create_time, update_time) values
('product:status', 'offshelf', '下架',   2, 1, '商品下架停售', sysdate, sysdate);

prompt 2 dictionary types inserted.

-- 提交事务
commit;

spool off
