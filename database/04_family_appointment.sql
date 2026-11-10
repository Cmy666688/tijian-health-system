-- 为预约增加体检人快照：1 为本人，2 为家属；原有数据保持原样。
ALTER TABLE orders
  ADD COLUMN patientType TINYINT NOT NULL DEFAULT 1,
  ADD COLUMN patientName VARCHAR(20) NULL,
  ADD COLUMN patientSex INT NULL,
  ADD COLUMN patientIdentityCard VARCHAR(18) NULL,
  ADD COLUMN patientBirthday DATE NULL;
