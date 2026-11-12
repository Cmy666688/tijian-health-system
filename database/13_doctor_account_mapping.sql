-- 旧库迁移：将展示排班关联到医生账号，不改变已有预约记录。
ALTER TABLE doctor_schedule ADD COLUMN docCode VARCHAR(20) NULL, ADD UNIQUE KEY uk_schedule_doctor (docCode), ADD CONSTRAINT fk_schedule_doctor FOREIGN KEY (docCode) REFERENCES doctor(docCode);
UPDATE doctor_schedule s JOIN doctor d ON d.docCode=CASE s.profileId WHEN 'doctor-01' THEN 'doctor01' WHEN 'doctor-02' THEN 'doctor02' WHEN 'doctor-03' THEN 'doctor03' ELSE NULL END SET s.docCode=d.docCode;
