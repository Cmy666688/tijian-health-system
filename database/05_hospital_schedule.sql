-- 仅在尚未创建 hospital_schedule 时执行。
CREATE TABLE hospital_schedule (
 hpId INT NOT NULL, weekday TINYINT NOT NULL, dailyCapacity INT NOT NULL,
 PRIMARY KEY (hpId, weekday),
 FOREIGN KEY (hpId) REFERENCES hospital(hpId),
 CHECK (weekday BETWEEN 1 AND 7), CHECK (dailyCapacity > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 东软健康体检中心演示排班：周一至周五，每天 20 个名额。
INSERT INTO hospital_schedule (hpId,weekday,dailyCapacity)
SELECT hpId, weekdays.weekday, 20 FROM hospital
CROSS JOIN (SELECT 1 AS weekday UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5) weekdays
WHERE hpId=1 AND name='东软健康体检中心';
