-- 仅当 password_reset_request 不存在时执行。
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
