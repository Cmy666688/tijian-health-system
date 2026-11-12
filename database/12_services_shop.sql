-- 仅当以下表均不存在时执行。
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
