-- 扩容以保存 BCrypt 哈希。仅扩大字段，不清除现有账号数据。
-- 已有明文密码在该账号首次成功登录时转换为 BCrypt。
ALTER TABLE users MODIFY COLUMN password VARCHAR(100) NOT NULL;
ALTER TABLE doctor MODIFY COLUMN password VARCHAR(100) NOT NULL;
