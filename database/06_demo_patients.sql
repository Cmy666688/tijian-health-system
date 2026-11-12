-- 独立虚构演示数据，仅供本地教学。所有患者、体检结果、医院信息均为虚构。
-- 仅在演示账号 19900000001—19900000005 和 doctor02/doctor03 均不存在时执行一次。
-- 全部为 INSERT；如遇账号冲突，应回滚并检查，绝不覆盖已有数据。
START TRANSACTION;
INSERT INTO users(userId,password,realName,sex,identityCard,birthday,userType) VALUES ('19900000001','$2b$10$/GOLvbrAOvNXmY3TvOF0d.hcr5ALlpzwTdfSfQPu72rJ4m1w./ADe','林晨',1,'000000198001010011','1980-01-01',1),('19900000002','$2b$10$/GOLvbrAOvNXmY3TvOF0d.hcr5ALlpzwTdfSfQPu72rJ4m1w./ADe','苏雅',0,'000000199001010022','1990-01-01',1),('19900000003','$2b$10$/GOLvbrAOvNXmY3TvOF0d.hcr5ALlpzwTdfSfQPu72rJ4m1w./ADe','陆航',1,'000000198501010033','1985-01-01',1),('19900000004','$2b$10$/GOLvbrAOvNXmY3TvOF0d.hcr5ALlpzwTdfSfQPu72rJ4m1w./ADe','许宁',0,'000000197501010044','1975-01-01',1),('19900000005','$2b$10$/GOLvbrAOvNXmY3TvOF0d.hcr5ALlpzwTdfSfQPu72rJ4m1w./ADe','周安',1,'000000199501010055','1995-01-01',1);
INSERT INTO doctor(docCode,realName,password,sex,deptno) VALUES ('doctor02','陈宁','$2b$10$/GOLvbrAOvNXmY3TvOF0d.hcr5ALlpzwTdfSfQPu72rJ4m1w./ADe',0,2),('doctor03','林悦','$2b$10$/GOLvbrAOvNXmY3TvOF0d.hcr5ALlpzwTdfSfQPu72rJ4m1w./ADe',1,3);
INSERT INTO hospital(name,picture,telephone,address,businessHours,deadline,`rule`,state) VALUES ('东软体检中心·和平院区','hospita1.png','010-55550101','和平路 1 号','周一至周五 08:00-17:00','10:00','请提前预约',1);
SET @demo_hp1 = LAST_INSERT_ID();
INSERT INTO hospital(name,picture,telephone,address,businessHours,deadline,`rule`,state) VALUES ('东软体检中心·浑南院区','hospita2.png','010-55550102','健康路 2 号','周一至周五 08:00-17:00','10:00','请提前预约',1);
SET @demo_hp2 = LAST_INSERT_ID();
INSERT INTO hospital_schedule(hpId,weekday,dailyCapacity) SELECT h.hpId,w.day,20 FROM hospital h CROSS JOIN (SELECT 1 AS day UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5) w WHERE h.hpId IN (@demo_hp1,@demo_hp2);
INSERT INTO setmeal(name,type,price) VALUES ('男士健康体检套餐',1,350);
SET @demo_sm1 = LAST_INSERT_ID();
INSERT INTO setmeal(name,type,price) VALUES ('女士健康体检套餐',0,380);
SET @demo_sm2 = LAST_INSERT_ID();
INSERT INTO setmeal(name,type,price) VALUES ('男士肝功能体检套餐',1,420);
SET @demo_sm3 = LAST_INSERT_ID();
INSERT INTO setmeal(name,type,price) VALUES ('女士肾功能体检套餐',0,420);
SET @demo_sm4 = LAST_INSERT_ID();
INSERT INTO checkitem(ciName,ciContent,meaning,remarks) VALUES ('基础体格检查','身高与体重检查','了解基础身体状况','虚构数据，仅供演示');
SET @demo_ci1 = LAST_INSERT_ID();
INSERT INTO checkitem(ciName,ciContent,meaning,remarks) VALUES ('血液常规检查','血液常规项目检查','了解血液指标','虚构数据，仅供演示');
SET @demo_ci2 = LAST_INSERT_ID();
INSERT INTO checkitem(ciName,ciContent,meaning,remarks) VALUES ('肝功能检查','肝功能项目检查','了解相关检查项目','虚构数据，仅供演示');
SET @demo_ci3 = LAST_INSERT_ID();
INSERT INTO checkitem(ciName,ciContent,meaning,remarks) VALUES ('肾功能检查','肾功能项目检查','了解相关检查项目','虚构数据，仅供演示');
SET @demo_ci4 = LAST_INSERT_ID();
INSERT INTO setmealdetailed(smId,ciId) VALUES (@demo_sm1,@demo_ci1),(@demo_sm1,@demo_ci2),(@demo_sm2,@demo_ci1),(@demo_sm2,@demo_ci2),(@demo_sm3,@demo_ci1),(@demo_sm3,@demo_ci3),(@demo_sm4,@demo_ci1),(@demo_sm4,@demo_ci4);
INSERT INTO checkitemdetailed(name,unit,minrange,maxrange,normalValue,normalValueString,type,ciId,remarks) VALUES ('身高','cm',NULL,NULL,NULL,NULL,3,@demo_ci1,'非临床数据'),('体重','kg',NULL,NULL,NULL,NULL,3,@demo_ci1,'非临床数据'),('白细胞计数','单位',NULL,NULL,NULL,NULL,3,@demo_ci2,'非临床数据'),('血小板计数','单位',NULL,NULL,NULL,NULL,3,@demo_ci2,'非临床数据'),('肝功能指标','单位',NULL,NULL,NULL,NULL,3,@demo_ci3,'非临床数据'),('肾功能指标','单位',NULL,NULL,NULL,NULL,3,@demo_ci4,'非临床数据');
INSERT INTO orders(orderDate,userId,hpId,smId,state,patientType,patientName,patientSex,patientIdentityCard,patientBirthday) VALUES (DATE_SUB(CURDATE(), INTERVAL 21 DAY),'19900000001',@demo_hp1,@demo_sm1,2,1,'林晨',1,'000000198001010011','1980-01-01');
SET @demo_order1 = LAST_INSERT_ID();
INSERT INTO orders(orderDate,userId,hpId,smId,state,patientType,patientName,patientSex,patientIdentityCard,patientBirthday) VALUES (DATE_SUB(CURDATE(), INTERVAL 14 DAY),'19900000005',@demo_hp2,@demo_sm3,2,1,'周安',1,'000000199501010055','1995-01-01');
SET @demo_order2 = LAST_INSERT_ID();
INSERT INTO orders(orderDate,userId,hpId,smId,state,patientType,patientName,patientSex,patientIdentityCard,patientBirthday) VALUES (DATE_ADD(CURDATE(), INTERVAL (7-WEEKDAY(CURDATE())) DAY),'19900000002',@demo_hp1,@demo_sm2,1,1,'苏雅',0,'000000199001010022','1990-01-01'),(DATE_ADD(CURDATE(), INTERVAL (8-WEEKDAY(CURDATE())) DAY),'19900000003',@demo_hp2,@demo_sm4,1,2,'陆岚',0,'000000196001010066','1960-01-01'),(DATE_ADD(CURDATE(), INTERVAL (9-WEEKDAY(CURDATE())) DAY),'19900000004',@demo_hp1,@demo_sm2,1,1,'许宁',0,'000000197501010044','1975-01-01');
INSERT INTO cireport(ciId,ciName,orderId) SELECT c.ciId,c.ciName,o.orderId FROM orders o JOIN setmealdetailed sd ON sd.smId=o.smId JOIN checkitem c ON c.ciId=sd.ciId WHERE o.orderId IN (@demo_order1,@demo_order2);
INSERT INTO cidetailedreport(name,unit,minrange,maxrange,normalValue,normalValueString,type,value,isError,ciId,orderId) SELECT d.name,d.unit,d.minrange,d.maxrange,d.normalValue,d.normalValueString,d.type,CASE WHEN d.name='身高' THEN '170' WHEN d.name='体重' THEN '65' WHEN d.name='白细胞计数' THEN '偏高' ELSE '正常' END,CASE WHEN d.name='白细胞计数' THEN 1 ELSE 0 END,r.cirId,r.orderId FROM cireport r JOIN checkitemdetailed d ON d.ciId=r.ciId WHERE r.orderId IN (@demo_order1,@demo_order2);
INSERT INTO overallresult(title,content,orderId) VALUES ('总检结论','体检报告已完成，请查看各项检查结果。',@demo_order1),('总检结论','体检报告已完成，请查看各项检查结果。',@demo_order2);
COMMIT;

-- 关联项目内医生账号和排班编号。
UPDATE doctor_schedule SET docCode=CASE profileId WHEN 'doctor-01' THEN 'doctor01' WHEN 'doctor-02' THEN 'doctor02' WHEN 'doctor-03' THEN 'doctor03' ELSE NULL END;
