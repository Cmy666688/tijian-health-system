-- 根据实践手册第 9–12 页整理；在已选择的空 tijian 数据库中执行。
-- 不删除、不覆盖已有表。
CREATE TABLE hospital (
 hpId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(30) NOT NULL,
 picture MEDIUMTEXT NOT NULL, telephone VARCHAR(20) NOT NULL,
 address VARCHAR(100) NOT NULL, businessHours VARCHAR(100) NOT NULL,
 deadline VARCHAR(30) NOT NULL, `rule` VARCHAR(30) NOT NULL, state INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE setmeal (
 smId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(225) NOT NULL,
 type INT NOT NULL, price INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE checkitem (
 ciId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, ciName VARCHAR(30) NOT NULL,
 ciContent VARCHAR(200) NOT NULL, meaning VARCHAR(200), remarks VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE users (
 userId VARCHAR(11) NOT NULL PRIMARY KEY, password VARCHAR(100) NOT NULL,
 realName VARCHAR(20) NOT NULL, sex INT NOT NULL, identityCard VARCHAR(18) NOT NULL,
 birthday DATE NOT NULL, userType INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE doctor (
 docId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, docCode VARCHAR(20) NOT NULL UNIQUE,
 realName VARCHAR(20) NOT NULL, password VARCHAR(100) NOT NULL, sex INT NOT NULL, deptno INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE setmealdetailed (
 sdId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, smId INT NOT NULL, ciId INT NOT NULL,
 FOREIGN KEY (smId) REFERENCES setmeal(smId), FOREIGN KEY (ciId) REFERENCES checkitem(ciId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE checkitemdetailed (
 cdId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(40) NOT NULL,
 unit VARCHAR(20), minrange DOUBLE, maxrange DOUBLE, normalValue VARCHAR(20),
 normalValueString VARCHAR(20), type INT NOT NULL, ciId INT NOT NULL, remarks VARCHAR(100),
 FOREIGN KEY (ciId) REFERENCES checkitem(ciId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE orders (
 orderId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, orderDate DATE NOT NULL,
 userId VARCHAR(11) NOT NULL, hpId INT NOT NULL, smId INT NOT NULL, state INT NOT NULL,
 patientType TINYINT NOT NULL DEFAULT 1, patientName VARCHAR(20), patientSex INT,
 patientIdentityCard VARCHAR(18), patientBirthday DATE,
 paymentStatus VARCHAR(16) NOT NULL DEFAULT 'PAID', paymentAmount DECIMAL(10,2) NULL, paidAt DATETIME NULL,
 FOREIGN KEY (userId) REFERENCES users(userId), FOREIGN KEY (hpId) REFERENCES hospital(hpId),
 FOREIGN KEY (smId) REFERENCES setmeal(smId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE overallresult (
 orId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, title VARCHAR(40) NOT NULL,
 content VARCHAR(400), orderId INT NOT NULL, FOREIGN KEY (orderId) REFERENCES orders(orderId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE cireport (
 cirId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, ciId INT NOT NULL,
 ciName VARCHAR(30) NOT NULL, orderId INT NOT NULL, FOREIGN KEY (orderId) REFERENCES orders(orderId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE cidetailedreport (
 cidrId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(40) NOT NULL,
 unit VARCHAR(20), minrange DOUBLE, maxrange DOUBLE, normalValue VARCHAR(20),
 normalValueString VARCHAR(20), type INT NOT NULL, value VARCHAR(100),
 isError INT NOT NULL, ciId INT NOT NULL, orderId INT NOT NULL,
 FOREIGN KEY (ciId) REFERENCES cireport(cirId), FOREIGN KEY (orderId) REFERENCES orders(orderId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 可配置的医院每周预约排班（手册未给出明细表，此为实现预约名额所增加）。
CREATE TABLE hospital_schedule (
 hpId INT NOT NULL, weekday TINYINT NOT NULL, dailyCapacity INT NOT NULL,
 PRIMARY KEY (hpId, weekday),
 FOREIGN KEY (hpId) REFERENCES hospital(hpId),
 CHECK (weekday BETWEEN 1 AND 7), CHECK (dailyCapacity > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户和医生之间的站内咨询记录。
CREATE TABLE consultation (
 consultationId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 userId VARCHAR(11) NOT NULL, orderId INT NULL,
 status VARCHAR(16) NOT NULL DEFAULT 'WAITING',
 createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
 updatedAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY (userId) REFERENCES users(userId),
 FOREIGN KEY (orderId) REFERENCES orders(orderId),
 INDEX idx_consultation_user (userId,updatedAt), INDEX idx_consultation_status (status,updatedAt)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE consultation_message (
 messageId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 consultationId INT NOT NULL, senderType VARCHAR(10) NOT NULL, senderId VARCHAR(20) NOT NULL,
 content VARCHAR(1000) NOT NULL, createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY (consultationId) REFERENCES consultation(consultationId),
 INDEX idx_consultation_message (consultationId,messageId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 医生展示编号与项目内排班；原照片身份和实际出诊信息待核实。
CREATE TABLE doctor_schedule (
 profileId VARCHAR(20) NOT NULL PRIMARY KEY,
 docCode VARCHAR(20) NULL UNIQUE,
 FOREIGN KEY (docCode) REFERENCES doctor(docCode),
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

CREATE TABLE password_reset_request (
 requestId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 userId VARCHAR(11) NOT NULL,
 state VARCHAR(16) NOT NULL DEFAULT 'PENDING',
 activeMarker TINYINT NULL DEFAULT 1,
 createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
 completedAt DATETIME NULL,
 FOREIGN KEY (userId) REFERENCES users(userId),
 UNIQUE KEY uk_reset_pending (userId,activeMarker)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE service_request (
 requestId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, userId VARCHAR(11) NOT NULL,
 serviceType VARCHAR(24) NOT NULL, content VARCHAR(1000) NOT NULL,
 status VARCHAR(16) NOT NULL DEFAULT 'PENDING', reply VARCHAR(1000) NULL,
 createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, updatedAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY(userId) REFERENCES users(userId), INDEX idx_service_user(userId,createdAt)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE health_record (
 recordId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, userId VARCHAR(11) NOT NULL,
 recordDate DATE NOT NULL, category VARCHAR(20) NOT NULL, value VARCHAR(80) NOT NULL, note VARCHAR(500) NULL,
 createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(userId) REFERENCES users(userId), INDEX idx_health_user(userId,recordDate)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE shop_product (
 productId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, title VARCHAR(100) NOT NULL,
 description VARCHAR(500) NOT NULL, price DECIMAL(10,2) NOT NULL, digitalContent TEXT NOT NULL, active TINYINT NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE shop_order (
 shopOrderId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, userId VARCHAR(11) NOT NULL,
 productId INT NOT NULL, title VARCHAR(100) NOT NULL, amount DECIMAL(10,2) NOT NULL,
 state VARCHAR(16) NOT NULL DEFAULT 'UNPAID', createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, paidAt DATETIME NULL,
 FOREIGN KEY(userId) REFERENCES users(userId), FOREIGN KEY(productId) REFERENCES shop_product(productId), INDEX idx_shop_user(userId,createdAt)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO shop_product(title,description,price,digitalContent) VALUES
 ('健康记录表','用于整理日常健康记录的电子表格模板。',9.90,'日期：
记录项目：
测量结果：
备注：
下次计划：'),
 ('体检准备清单','帮助整理检查前问题和随身资料的电子清单。',6.90,'预约日期及地点：
既往病史：
近期用药：
需要咨询的问题：
携带证件和既往报告。检查准备以机构通知为准。');
