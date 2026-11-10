-- 教学用示例数据，非真实医疗数据；仅在空表中执行一次。
INSERT INTO hospital (hpId,name,picture,telephone,address,businessHours,deadline,`rule`,state) VALUES
(1,'东软健康体检中心','hospita1.png','010-55550001','健康路 1 号','周一至周五 08:00-17:00','10:00','请提前预约',1);
INSERT INTO setmeal (smId,name,type,price) VALUES (1,'男士基础体检',1,300),(2,'女士基础体检',0,300);
INSERT INTO checkitem (ciId,ciName,ciContent,meaning,remarks) VALUES
(1,'一般检查','身高、体重等基础检查','了解基础健康状况','示例数据'),
(2,'血常规','血液常规项目检查','了解血液指标','示例数据');
INSERT INTO setmealdetailed (smId,ciId) VALUES (1,1),(1,2),(2,1),(2,2);
INSERT INTO checkitemdetailed (cdId,name,unit,minrange,maxrange,normalValue,normalValueString,type,ciId,remarks) VALUES
(1,'身高','cm',NULL,NULL,NULL,'以实际情况为准',3,1,'示例'),
(2,'白细胞计数','10^9/L',4,10,NULL,'4-10',1,2,'示例范围');
INSERT INTO doctor (docId,docCode,realName,password,sex,deptno) VALUES (1,'doctor01','刘明','$2b$10$paZkwA6WNkQmO4cVUca3xOmo0TF.3n27zE90VT0zAzAQLKEOaASvq',1,1);

-- 东软健康体检中心演示排班：周一至周五，每天 20 个名额。
INSERT INTO hospital_schedule (hpId,weekday,dailyCapacity)
SELECT hpId, weekdays.weekday, 20 FROM hospital
CROSS JOIN (SELECT 1 AS weekday UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5) weekdays
WHERE hpId=1 AND name='东软健康体检中心';
