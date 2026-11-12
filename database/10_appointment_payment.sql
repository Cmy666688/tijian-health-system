-- 对已存在的 orders 表执行一次：保留历史预约为已支付状态。
ALTER TABLE orders ADD COLUMN paymentStatus VARCHAR(16) NOT NULL DEFAULT 'PAID', ADD COLUMN paymentAmount DECIMAL(10,2) NULL, ADD COLUMN paidAt DATETIME NULL;
UPDATE orders o JOIN setmeal s ON s.smId=o.smId SET o.paymentAmount=s.price WHERE o.paymentAmount IS NULL;
