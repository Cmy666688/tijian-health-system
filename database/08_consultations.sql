-- 仅在 consultation 和 consultation_message 均不存在时执行。
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
