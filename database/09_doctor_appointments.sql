-- 仅当两张表均不存在时执行。
-- 医生展示编号与项目内排班；原照片身份和实际出诊信息待核实。
CREATE TABLE doctor_schedule (
 profileId VARCHAR(20) NOT NULL PRIMARY KEY,
 dailyCapacity INT NOT NULL,
 active TINYINT NOT NULL DEFAULT 1,
 CHECK (dailyCapacity > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE doctor_appointment (
 appointmentId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 profileId VARCHAR(20) NOT NULL,
 userId VARCHAR(11) NOT NULL,
 appointmentDate DATE NOT NULL,
 queueNo INT NOT NULL,
 state VARCHAR(16) NOT NULL DEFAULT 'BOOKED',
 activeMarker TINYINT NULL DEFAULT 1,
 createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY (profileId) REFERENCES doctor_schedule(profileId),
 FOREIGN KEY (userId) REFERENCES users(userId),
 UNIQUE KEY uk_doctor_day_no (profileId,appointmentDate,queueNo),
 UNIQUE KEY uk_doctor_day_user (profileId,appointmentDate,userId,activeMarker),
 INDEX idx_doctor_appointment_user (userId,appointmentDate)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO doctor_schedule (profileId,dailyCapacity) VALUES
 ('doctor-01',20),('doctor-02',20),('doctor-03',20),('doctor-04',20),
 ('doctor-05',20),('doctor-06',20),('doctor-07',20),('doctor-08',20);
